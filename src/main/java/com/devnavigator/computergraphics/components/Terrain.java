package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;
import org.javatuples.Quartet;

public class Terrain extends GraphicModel {

    private static final int VERTEX_COUNT = 128;

    private float x;

    private float y;

    public Terrain(
            final float x,
            final float y,
            final float[] vertices,
            final float[] textureCoord,
            final float[] normals,
            final int[] indices
    ) {
        super(
                RawModel.create(
                        vertices,
                        3
                )
//                .addTexture(2, textureCoord)
                .addNormals(3, normals)
                .addIndexBuffer(indices),
                VERTEX_COUNT,
                0,
                0
        );

        this.x = x;
        this.y = y;
    }

    public static Terrain create(
            final float size
    ) {
        final var terrainInfo = generateTerrain(size);
        final var terrain = new Terrain(
                0,
                0,
                terrainInfo.getValue0(),
                terrainInfo.getValue1(),
                terrainInfo.getValue2(),
                terrainInfo.getValue3()
        );
        return terrain;
    }

    private static Quartet<float[], float[], float[], int[]> generateTerrain(
            final float size
    ){
        int vertexCount = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] textureCoords = new float[vertexCount * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;

        for (int row = 0; row < VERTEX_COUNT; row++) {
            for (int col = 0; col < VERTEX_COUNT; col++) {
                float x = (float) col / (VERTEX_COUNT - 1) * size;
                float y = 0;
                float z = (float) row / (VERTEX_COUNT - 1) * size;

                int normalPointer = vertexPointer * 3;
                int texturePointer = vertexPointer * 2;

                vertices[normalPointer] = x;
                vertices[normalPointer + 1] = y;
                vertices[normalPointer + 2] = z;

                normals[normalPointer] = 0;
                normals[normalPointer + 1] = 1;
                normals[normalPointer + 2] = 0;

                textureCoords[texturePointer] = (float) col / (VERTEX_COUNT - 1);
                textureCoords[texturePointer + 1] = (float) row / (VERTEX_COUNT - 1);

                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int row = 0; row < VERTEX_COUNT - 1; row++) {
            for (int col = 0; col < VERTEX_COUNT - 1; col++) {
                int topLeft = (row * VERTEX_COUNT) + col;
                int topRight = topLeft + 1;
                int bottomLeft = ((row + 1) * VERTEX_COUNT) + col;
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
}
