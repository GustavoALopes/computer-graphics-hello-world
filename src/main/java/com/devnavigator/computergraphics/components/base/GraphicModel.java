package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.OBJLoader;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.math.Vector2f;
import com.devnavigator.computergraphics.engine.components.math.Vector3f;
import org.javatuples.Quartet;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GraphicModel {

    protected final RawModel model;

    private final int numVertex;

    private Matrix4f translate;

    private Matrix4f scale;

    private Matrix4f rotate;

    public GraphicModel(
            final RawModel model,
            final int numVertex
    ) {
        this.model = model;
        this.numVertex = numVertex;
        this.translate = new Matrix4f();
        this.rotate = new Matrix4f();
        this.scale = new Matrix4f();
    }

    public RawModel getModel() {
        return this.model;
    }

    public int getNumVertex() {
        return this.numVertex;
    }

    public GraphicModel addTexture(
            final String texturePath,
            final float[] coordinates
    ) {
        return this.addTexture(
                Texture.loadTexture(texturePath),
                coordinates
        );
    }

    public GraphicModel addTexture(
            final Texture texture,
            final float[] coordinates
    ) {
        this.model.addTexture(
                2,
                texture,
                coordinates
        );
        return this;
    }

    public GraphicModel rotate(
            final float angle
    ) {
        this.rotate = Matrix4f.rotate(
                angle,
                0f,
                0f,
                1f
        );
        return this;
    }

    public GraphicModel changeColor(
            final int shaderIndexAttrib,
            final int[] color
    ) {
        this.model.changeColor(
                shaderIndexAttrib,
                color
        );
        return this;
    }

    public GraphicModel changeColor(
            final int shaderIndexAttrib,
            final int red,
            final int green,
            final int blue
    ) {
        if(red > 255) {
            throw new RuntimeException("The red value color need to be into range of 0 at 255");
        } else if (green > 255) {
            throw new RuntimeException("The green value color need to be into range of 0 at 255");
        } else if (blue > 255) {
            throw new RuntimeException("The blue value color need to be into range of 0 at 255");
        }

        return this.changeColor(shaderIndexAttrib, new int[]{ red, green, blue });
    }

    public GraphicModel changeScale(final float x, final float y, final float z) {
        this.scale = Matrix4f.scale(x, y, z);
        return this;
    }

    public GraphicModel translate(final float x, final float y, final float z) {
        this.translate = Matrix4f.translate(x, y, z);
        return this;
    }

    public Matrix4f render() {
        var matrix = Optional.ofNullable(this.translate)
                .orElse(new Matrix4f());

        if(!Objects.isNull(this.rotate)) {
            matrix = matrix.multiply(this.rotate);
        }
        if(!Objects.isNull(this.scale)) {
            matrix = matrix.multiply(this.scale);
        }

        return matrix;
    }

    public GraphicModel increasePosition(
            final float x,
            final float y,
            final float z
    ) {
        this.translate = this.translate.multiply(Matrix4f.translate(x, y, z));
        return this;
    }

    public GraphicModel increaseRotation(
            final float x,
            final float y,
            final float z
    ) {
        this.rotate = Optional.ofNullable(this.rotate)
                .orElse(new Matrix4f());

        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(x), 1,  0, 0));
        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(y), 0, 1, 0));
        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(z), 0, 0, 1));

        return this;
    }

    public static GraphicModel loadFromObj(
            final Path modelPath
    ) {
       return loadFromObj(modelPath, null);
    }

    public static GraphicModel loadFromObj(
        final Path modelPath,
        final Texture texture
    ) {
        final var values = OBJLoader.loadOBJ(modelPath);

        final var model = new GraphicModel(
                RawModel.create(
                        values.getValue0(),
                        3
                ).addIndexBuffer(
                        values.getValue3()
                )
                .addNormals(3, values.getValue2()),
                values.getValue3().length
        );

        if(Objects.nonNull(texture)) {
            model.addTexture(texture, values.getValue1());
        }

        return model;
    }

    private record OBJModel(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices) {
        private final static int MODEL_TRIANGLE_BASE_FACE_COUNT = 3;

        public static OBJModel create(final Path modelPath) {
                try (final var stream = new BufferedReader(new FileReader(modelPath.toFile()))) {
                    final var objProperties = reader(stream);
                    final var vertices = new float[objProperties.getValue0().size() * 3];

                    int vertexPointer = 0;
                    for (final var vertex : objProperties.getValue0()) {
                        vertices[vertexPointer++] = vertex.x;
                        vertices[vertexPointer++] = vertex.y;
                        vertices[vertexPointer++] = vertex.z;
                    }
                    final var model = new OBJModel(
                            vertices,
                            toArray2DList(objProperties.getValue1()),
                            toArrayFloat(objProperties.getValue2()),
                            toArrayInt(objProperties.getValue3())
                    );
                    return model;
                } catch (final FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private static Quartet<List<Vector3f>, List<Vector2f>, List<Vector3f>, List<Integer>> reader(
                    final BufferedReader reader
            ) throws IOException {
                final var vertices = new ArrayList<Vector3f>();
                final var textureCoordinates = new ArrayList<Vector2f>();
                final var normals = new ArrayList<Vector3f>();
                final var indices = new ArrayList<Integer>();

                String line = reader.readLine();
                do {
                    if(Objects.isNull(line)) continue;

                    final var tokens = line.split("\\s+");

                    switch (tokens[0]) {
                        case "v":
                            vertices.add(new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])
                            ));
                            break;
                        case "vn":
                            normals.add(new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])
                            ));
                            break;
                        case "vt":
                            textureCoordinates.add(new Vector2f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2])
                            ));
                            break;
                        case "f":
                            for (var i = 1; i <= MODEL_TRIANGLE_BASE_FACE_COUNT; i++) {
                                final var faceTokens = tokens[i].split("/");
                                final var zeroIndexBase = Integer.parseInt(faceTokens[0]) - 1;
                                indices.add(zeroIndexBase);
                                break;
                            }
                    }

                    line = reader.readLine();
                } while (line != null);

                return new Quartet<>(
                        vertices,
                        textureCoordinates,
                        normals,
                        indices
                );
        }

        private static float[] toArray2DList(final List<Vector2f> list) {
            final var array = new float[list.size() * 2];
            for (var i = 0; i < list.size(); i++) {
                array[i * 2] = list.get(i).x;
                array[i * 2 + 1] = list.get(i).y;
            }
            return array;
        }

        private static float[] toArrayFloat(final List<Vector3f> list) {
                final var array = new float[list.size() * 3];
                for (int i = 0; i < list.size(); i++) {
                    array[i * 3] = list.get(i).x;
                    array[i * 3 + 1] = list.get(i).y;
                    array[i * 3 + 2] = list.get(i).z;
                }
                return array;
            }

            private static int[] toArrayInt(final List<Integer> list) {
                final var array = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                return array;
            }
        }
}
