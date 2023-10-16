package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.lwjgl.opengl.GL33;

public class RawModel {

    private final VertexArrayObject vao;

    private final VertexBufferObject vbo;

    private IndexBuffer indexes;

    private RawModel(
            final VertexArrayObject vao,
            final VertexBufferObject vbo
    ) {
        this.vao = vao;
        this.vbo = vbo;
    }

    public int getVaoId() {
        return this.vao.getId();
    }

    public int getVboId() {
        return this.vbo.getId();
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

        return new RawModel(vao, vbo);
    }

    public RawModel addIndexBuffer(final int[] indexes) {
        this.indexes = new IndexBuffer(indexes.length);
        this.indexes.bind();
        this.indexes.updateData(indexes);
        return this;
    }
}
