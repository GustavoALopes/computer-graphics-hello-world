package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.lwjgl.opengl.GL33;

import java.util.Map;

public class RawModel {

    private final VertexArrayObject vao;

    private final Map<String, VertexBufferObject> vbo;

    private Texture texture;

    private IndexBuffer indexes;

    private RawModel(
            final VertexArrayObject vao,
            final Map<String, VertexBufferObject> vbo
    ) {
        this.vao = vao;
        this.vbo = vbo;
        this.texture = null;
    }

    public int getVaoId() {
        return this.vao.getId();
    }

    public int getVboId(final String type) {
        return this.vbo.get(type).getId();
    }

    public static RawModel create(
            final float[] data,
            final int coordSize
    ) {
        final var vao = new VertexArrayObject();
        vao.bind();

        final var vbo = new VertexBufferObject();
        vbo.bind();

        vbo.updateData(data);

        GL33.glVertexAttribPointer(
                0,
                coordSize,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        return new RawModel(vao, Map.of("position", vbo));
    }

    public RawModel addIndexBuffer(final int[] indexes) {
        this.indexes = new IndexBuffer(indexes.length);
        this.indexes.bind();
        this.indexes.updateData(indexes);
        return this;
    }

    public RawModel addTexture(final Texture texture) {
        this.texture = texture;
        return this;
    }
}
