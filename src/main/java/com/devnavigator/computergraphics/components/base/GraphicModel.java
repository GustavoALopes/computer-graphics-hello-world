package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.OBJLoader;
import com.devnavigator.computergraphics.engine.components.Texture;
import org.javatuples.Quartet;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GraphicModel {

    protected final RawModel model;

    private float shineDamper;

    private float reflectivity;

    private final int numVertex;

    private Vector3f position;

    private Matrix4f translate;

    private float scale;

    private Vector3f rotate;

    public GraphicModel(
            final RawModel model,
            final int numVertex,
            final float shineDamper,
            final float reflectivity
    ) {
        this.model = model;
        this.numVertex = numVertex;
        this.position = new Vector3f();
        this.translate = new Matrix4f();
        this.rotate = new Vector3f(0, 0, 0);
        this.scale = 1f;

        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
    }

    public static GraphicModel create(
            final Quartet<float[], float[], float[], int[]> rawModel,
            final Texture texture
    ) {
        final var model = new GraphicModel(
                RawModel.create(
                        rawModel.getValue0(),
                        3
                ).addIndexBuffer(
                        rawModel.getValue3()
                )
                .addNormals(3, rawModel.getValue2()),
                rawModel.getValue3().length,
                1,
                0
        );

        if(Objects.nonNull(texture)) {
            model.addTexture(texture, rawModel.getValue1());
        }

        return model;
    }

    public RawModel getModel() {
        return this.model;
    }

    public int getNumVertex() {
        return this.numVertex;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
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

//    public GraphicModel rotate(
//            final float angle
//    ) {
//        this.rotate = new Matrix4f().rotate(
//                angle,
//                0f,
//                0f,
//                1f
//        );
//        return this;
//    }

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

    public GraphicModel changeScale(final float scale) {
        this.scale = scale;
        return this;
    }

    public GraphicModel translate(final float x, final float y, final float z) {
//        this.position = new Vector3f(x, y, z);
//        this.translate = new Matrix4f().translate(new Vector3f(x, y, z));
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        return this;
    }

    public Matrix4f getTransformationMatrix() {
        var matrix = new Matrix4f();
//        matrix.setIdentity();

        matrix.translate(this.position);
//        var matrix = Optional.ofNullable(this.translate)
//                .orElse(new Matrix4f());
//        final var tranformationMatrix = new Matrix4f();
//        tranformationMatrix.setIdentity();

        //Translate
//        tranformationMatrix.multiply(new Vector4f(this.position.x, this.position.y, this.position.z, 0));


        //Rotate
//        if(Objects.nonNull(this.rotate)) {
//            tranformationMatrix.multiply(this.rotate);
//        }
//
//        if(Objects.nonNull(this.scale)) {
//            tranformationMatrix.multiply(this.scale);
//        }

//        if(!Objects.isNull(this.rotate)) {
//            final var angleVector = new Vector3f(
//                (float)Math.toRadians(this.rotate.x),
//                (float)Math.toRadians(this.rotate.y),
//                (float)Math.toRadians(this.rotate.z)
//            );
//            matrix.rotate(angleVector);
            matrix.rotate((float)Math.toRadians(this.rotate.x), new Vector3f(1, 0, 0), matrix);
            matrix.rotate((float)Math.toRadians(this.rotate.y), new Vector3f(0, 1, 0), matrix);
            matrix.rotate((float)Math.toRadians(this.rotate.z), new Vector3f(0, 0, 1), matrix);
//        }

        matrix.scale(this.scale);


        return matrix;
    }

    public GraphicModel increasePosition(
            final float x,
            final float y,
            final float z
    ) {
//        this.translate = this.translate.translate(x, y, z);
//        this.translate = this.translate.translate(Matrix4f.translate(x, y, z));
//        this.position = this.position.add(new Vector3f(x, y, z));
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        return this;
    }

    public GraphicModel increaseRotation(
            final float x,
            final float y,
            final float z
    ) {
//        this.rotate((float)Math.toRadians(x))
//            .rotate((float)Math.toRadians(y))
//            .rotate((float)Math.toRadians(z));

        this.rotate.x += x;
        this.rotate.y += y;
        this.rotate.z += z;

//        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(x), 1,  0, 0));
//        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(y), 0, 1, 0));
//        this.rotate = this.rotate.multiply(Matrix4f.rotate((float)Math.toRadians(z), 0, 0, 1));

        return this;
    }

    public GraphicModel setPosition(final Vector3f vector3f) {
        this.position = vector3f;
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
        return loadFromObj(modelPath, texture, 1, 0);
    }

    public static GraphicModel loadFromObj(
        final Path modelPath,
        final Texture texture,
        final float shineDamper,
        final float reflectivity
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
                values.getValue3().length,
                shineDamper,
                reflectivity
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
