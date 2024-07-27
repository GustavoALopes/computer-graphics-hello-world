package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.OBJLoader;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.renderer.*;
import org.lwjgl.opengl.GL33;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private final Map<String, TexturedModel> resources;

    private final Map<String, NewRawModel> models;

    private final Map<String, Texture> textures;

    private ResourceManager() {
        this.resources = new HashMap<>();
        this.models = new HashMap<>();
        this.textures = new HashMap<>();
    }

    public static ResourceManager create() {
        return new ResourceManager();
    }

    public TexturedModel getResource(final String resourceName) {
        return this.resources.get(resourceName);
    }

    public TexturedModel getOrCreate(
            final Path objPath,
            final Path texturePath,
            final ProgramShader.Types shardeType
    ) {
        final var model = this.resources.get(String.join(
                ":",
                        objPath.getFileName().toString(),
                        texturePath.getFileName().toString()
        ));

        if(Objects.nonNull(model)) {
            return model;
        }

        var rawModel = this.models.get(objPath.getFileName().toString());
        if(Objects.isNull(rawModel)) {
            rawModel = this.createRawModel(objPath, shardeType);
            this.models.put(objPath.getFileName().toString(), rawModel);
        }

        var texture = this.textures.get(texturePath.getFileName().toString());
        if(Objects.isNull(texture)) {
            texture = Texture.loadTexture(texturePath.toString());
            this.textures.put(texturePath.getFileName().toString(), texture);
        }

        final var textureModel = TexturedModel.create(
                rawModel,
                texture
        );

        this.resources.put(String.join(
                ":",
                objPath.getFileName().toString(),
                texturePath.getFileName().toString()
        ), textureModel);

        return textureModel;
    }

//    private TexturedModel createModel(
//            final Path objPath,
//            final Path texturePath
//    ) {
//        final var texture = Texture.loadTexture(texturePath.toString());
//        final var rawModel = this.createRawModel(objPath);
//
//        return TexturedModel.create(
//                rawModel,
//                texture
//        );
//    }

    private NewRawModel createRawModel(
            final Path objPath,
            final ProgramShader.Types shaderType
    ) {
        final var props = OBJLoader.loadOBJ(objPath);
        return createRawModel(
                props.getValue0(),
                props.getValue1(),
                props.getValue2(),
                props.getValue3(),
                shaderType
        );

//        final var vao = new VertexArrayObject();
//        vao.bind();
//
//        final var vboPosition = this.setPosition(props.getValue0());
//        final var vboTexture = this.setTextureCoords(props.getValue1());
//        final var vboNormals = this.setNormals(props.getValue2());
//        final var index = this.setIndexes(props.getValue3());
//        final var numberOfVertex = props.getValue3().length;
//
//        return NewRawModel.create(
//                vao,
//                Map.of(
//                        "position", vboPosition,
//                        "texture", vboTexture,
//                        "normals", vboNormals
//                ),
//                index,
//                numberOfVertex
//        );
    }

    public static NewRawModel createRawModel(
            final float[] position,
            final float[] textureCoords,
            final float[] normals,
            final int[] indexes,
            final ProgramShader.Types shaderType
    ) {
        final var vao = new VertexArrayObject();
        vao.bind();

        final var vboPosition = setPosition(position, shaderType);
        final var vboTexture = setTextureCoords(textureCoords, shaderType);
        final var vboNormals = setNormals(normals, shaderType);
        final var index = setIndexes(indexes);
        final var numberOfVertex = indexes.length;

        return NewRawModel.create(
                vao,
                Map.of(
                        "position", vboPosition,
                        "texture", vboTexture,
                        "normals", vboNormals
                ),
                index,
                numberOfVertex
        );
    }

    private static IndexBuffer setIndexes(final int[] values) {
        final var index = new IndexBuffer(values.length);
        index.bind();
        index.updateData(values);
        return index;
    }

    private static VertexBufferObject setNormals(
            final float[] normals,
            final ProgramShader.Types shaderTypes
    ) {
        final var vbo = new VertexBufferObject();
        vbo.bind();

        vbo.updateData(normals);

        final var normalsIndex = getNormalIndexFromProgramShader(shaderTypes);

        GL33.glVertexAttribPointer(
                normalsIndex,
                3,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(normalsIndex);

        return vbo;
    }

    private static int getNormalIndexFromProgramShader(final ProgramShader.Types shardeTypes) {
        return Objects.equals(ProgramShader.Types.ENTITY, shardeTypes) ? EntityShader.NORMALS_LAYER : TerrainShader.NORMALS_LAYER;
    }

    private static VertexBufferObject setTextureCoords(
            final float[] textureCoords,
            final ProgramShader.Types shaderType
    ) {
        final var vbo = new VertexBufferObject();
        vbo.bind();

        vbo.updateData(textureCoords);

        final var textCoordIndex = getTextCoordsIndexFromProgramShader(shaderType);

        GL33.glVertexAttribPointer(
                textCoordIndex,
                2,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(textCoordIndex);

        return vbo;
    }

    private static int getTextCoordsIndexFromProgramShader(final ProgramShader.Types shaderType) {
        return Objects.equals(ProgramShader.Types.ENTITY, shaderType) ? EntityShader.TEXT_COORD_LAYER : TerrainShader.TEXT_COORDS_LAYER;
    }

    private static VertexBufferObject setPosition(
            final float[] positions,
            final ProgramShader.Types shaderType
    ) {
        final var vbo = new VertexBufferObject();
        vbo.bind();

        vbo.updateData(positions);

        final var positionIndex = getPositionIndexFromProgramShader(shaderType);

        GL33.glVertexAttribPointer(
                positionIndex,
                3,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(positionIndex);

        return vbo;
    }

    private static int getPositionIndexFromProgramShader(final ProgramShader.Types shaderType) {
        return Objects.equals(ProgramShader.Types.ENTITY, shaderType) ? EntityShader.POSITION_LAYER : TerrainShader.POSITION_LAYER;
    }

    public Texture getTexture(final Path pathTexture) {
        var texture = this.textures.get(pathTexture.getFileName().toString());
        if(Objects.nonNull(texture)) {
            return texture;
        }

        texture = Texture.loadTexture(pathTexture.toString());

        this.textures.put(
                pathTexture.getFileName().toString(),
                texture
        );

        return texture;
    }
}
