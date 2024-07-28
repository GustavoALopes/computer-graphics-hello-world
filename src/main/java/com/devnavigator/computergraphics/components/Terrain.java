package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.components.base.ResourceManager;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.javatuples.Quartet;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Terrain {

    private static final int TERRAIN_SIZE = 240;

    public static final int SCALE = 1;

    private static Matrix4f transformationMatrix;

    private final RawModel rawModel;

    private final Vector3f position;

    private TexturesPack texturesPack;

    private final Texture blendMap;

    public Terrain(
            final float x,
            final float z,
            final float[] vertices,
            final float[] textureCoords,
            final float[] normals,
            final int[] indices,
            final Texture blendMap,
            final TexturesPack texturesPack
    ) {
        this.rawModel = ResourceManager.createRawModel(
                vertices,
                textureCoords,
                normals,
                indices,
                ProgramShader.Types.TERRAIN
        );

        this.position = new Vector3f(x, 0, z);

        this.blendMap = blendMap;
        this.texturesPack = texturesPack;
    }

    public TexturesPack getTexturesPack() {
        return texturesPack;
    }

    public Texture getBlendMap() {
        return blendMap;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public static Terrain create(
            final float size
    ) {
        return create(0, 0, size, null, null);
    }

    public static Terrain create(
            final float x,
            final float z,
            final float size,
            final Texture blendMap,
            final TexturesPack texturesPack
    ) {
        final var terrainInfo = generateTerrain(size);
        return new Terrain(
                x * size,
                z * size,
                terrainInfo.getValue0(),
                terrainInfo.getValue1(),
                terrainInfo.getValue2(),
                terrainInfo.getValue3(),
                blendMap,
                texturesPack
        );
    }

    private static Quartet<float[], float[], float[], int[]> generateTerrain(
            final float size
    ){
        int vertexCount = TERRAIN_SIZE * TERRAIN_SIZE;
        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] textureCoords = new float[vertexCount*2];
        int[] indices = new int[6*(TERRAIN_SIZE -1)*(TERRAIN_SIZE -1)];

        int vertexPointer = 0;
        for(int row = 0; row < TERRAIN_SIZE; row++){
            for(int column = 0; column < TERRAIN_SIZE; column++){
                vertices[vertexPointer*3] = (float)column/((float) TERRAIN_SIZE - 1) * size;
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)row/((float) TERRAIN_SIZE - 1) * size;

                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;

                textureCoords[vertexPointer*2] = (float)column/((float) TERRAIN_SIZE - 1f);
                textureCoords[vertexPointer*2+1] = (float)row/((float) TERRAIN_SIZE - 1f);

                vertexPointer++;
            }
        }

        int pointer = 0;
        for(int gz = 0; gz< TERRAIN_SIZE -1; gz++){
            for(int gx = 0; gx< TERRAIN_SIZE -1; gx++){
                int topLeft = (gz* TERRAIN_SIZE)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)* TERRAIN_SIZE)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return new Quartet<>(
                vertices,
                textureCoords,
                normals,
                indices
        );
    }

    public Matrix4f getTransformationMatrix() {
       final var matrix = new Matrix4f();

        matrix.translate(this.position);

        return matrix;
    }

    public static class TexturesPack {

        private final Texture backgroudTexture;

        private final Texture rTexture;

        private final Texture gTexture;

        private final Texture bTexture;

        public TexturesPack(
                final Texture backgroudTexture,
                final Texture rTexture,
                final Texture gTexture,
                final Texture bTexture
        ) {
            this.backgroudTexture = backgroudTexture;
            this.rTexture = rTexture;
            this.gTexture = gTexture;
            this.bTexture = bTexture;
        }

        public Texture getBackgroudTexture() {
            return backgroudTexture;
        }

        public Texture getrTexture() {
            return rTexture;
        }

        public Texture getgTexture() {
            return gTexture;
        }

        public Texture getbTexture() {
            return bTexture;
        }

        public static TexturesPack create(
            final Texture backgroudTexture,
            final Texture rTexture,
            final Texture gTexture,
            final Texture bTexture
        ) {
            return new TexturesPack(
                    backgroudTexture,
                    rTexture,
                    gTexture,
                    bTexture
            );
        }
    }
}
