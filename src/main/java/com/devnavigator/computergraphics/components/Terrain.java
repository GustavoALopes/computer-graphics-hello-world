package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.*;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.javatuples.Quartet;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends NewGraphicModel {

    private static final int TERRAIN_SIZE = 240;

    private List<Texture> textures;

    private Texture blendMap;

    public Terrain(
            final float x,
            final float z,
            final float[] vertices,
            final float[] textureCoords,
            final float[] normals,
            final int[] indices,
            final Texture texture
    ) {
        super(
                TexturedModel.create(
                    ResourceManager.createRawModel(
                      vertices,
                      textureCoords,
                      normals,
                      indices,
                      ProgramShader.Types.TERRAIN
                    ),
                    texture
                ),
                new Vector3f(x, 0, z),
                new Vector3f(0, 0, 0),
                1
        );
//        super(
//                RawModel.create(
//                        vertices,
//                        3
//                )
//                .addTexture(1, texture, textureCoord)
//                .addNormals(2, normals)
//                .addIndexBuffer(indices),
//                indices.length,
//                0,
//                0
//        );

        this.textures = new ArrayList<>();

//        this.setPosition(new Vector3f(x, 0, z));
    }

    public static Terrain create(
            final float size
    ) {
        return create(0, 0, size, null);
    }

    public static Terrain create(
            final float x,
            final float z,
            final float size,
            final Texture texture
    ) {
        final var terrainInfo = generateTerrain(size);
        return new Terrain(
                x * size,
                z * size,
                terrainInfo.getValue0(),
                terrainInfo.getValue1(),
                terrainInfo.getValue2(),
                terrainInfo.getValue3(),
                texture
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
}
