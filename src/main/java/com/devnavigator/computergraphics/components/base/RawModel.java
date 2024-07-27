package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RawModel {

    private final VertexArrayObject vao;

    private final Map<String, VertexBufferObject> vbos;

    private Texture texture;

    private IndexBuffer indexes;

    private RawModel() {
        this.vao = new VertexArrayObject();
        this.vao.bind();
        this.vbos = new HashMap<>();
    }

    public int getVaoId() {
        return this.vao.getId();
    }

    public int getVboId(final String type) {
        return this.vbos.get(type).getId();
    }

    public Texture getTexture() {
        return this.texture;
    }

    public RawModel setPosition(
            final int shaderPositionIndex,
            final float[] position,
            final int coordSize
    ) {
//        this.vao.bind();

        final var vbo = this.getOrCreateVBO("vertices");
        vbo.bind();

        vbo.updateData(position);

        GL33.glVertexAttribPointer(
                shaderPositionIndex,
                coordSize,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(shaderPositionIndex);

        return this;
    }

    public RawModel addIndexBuffer(final int[] indexes) {
        this.indexes = new IndexBuffer(indexes.length);
        this.indexes.bind();
        this.indexes.updateData(indexes);
        return this;
    }

    public RawModel addTexture(
            final int shaderTextCoordsIndex,
            final Texture texture,
            final float[] coordinates
    ) {
//        this.vao.bind();

        final var textureAttrib = this.getOrCreateVBO("textureCoordinates");
        textureAttrib.bind();

        textureAttrib.updateData(coordinates);

        GL33.glVertexAttribPointer(
                shaderTextCoordsIndex,
                2,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        this.texture = texture;

        GL33.glActiveTexture(GL33.GL_TEXTURE0);

        GL33.glBindTexture(
            GL33.GL_TEXTURE_2D,
            texture.getId()
        );

        GL33.glEnableVertexAttribArray(
            shaderTextCoordsIndex
        );

        return this;
    }

    public RawModel addNormals(
            final int shaderNormalAttrPosition,
            final float[] normals
    ) {
//        this.vao.bind();

        final var normalsAttr = this.getOrCreateVBO("normals");
        normalsAttr.bind();

        normalsAttr.updateData(normals);

        GL33.glVertexAttribPointer(
                shaderNormalAttrPosition,
                3,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(shaderNormalAttrPosition);

        return this;
    }

    public RawModel changeColor(
            final int shaderIndexAttrib,
            final int[] color
    ) {
//        this.vao.bind();

        final var vbo = this.getOrCreateVBO("color");
        vbo.bind();
        vbo.updateData(
            color
        );

        GL33.glVertexAttribPointer(
                shaderIndexAttrib,
                3,
                GL33.GL_UNSIGNED_BYTE,
                true,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(shaderIndexAttrib);

        return this;
    }

    private VertexBufferObject getOrCreateVBO(final String name) {
        var vbo = this.vbos.get(name);
        if(Objects.isNull(vbo)) {
            vbo = this.createNewVBO(name);
        }

        return vbo;
    }

    private VertexBufferObject createNewVBO(final String name) {
        final var vbo = new VertexBufferObject();
        this.vbos.put(name, vbo);
        return vbo;
    }

    public static RawModel create() {
        return new RawModel();
    }

    public static RawModel create(
            final float[] vertices,
            final int coordSize
    ) {
        final var model = new RawModel();
        model.setPosition(
                0,
                vertices,
                coordSize
        );

        return model;
    }
}
