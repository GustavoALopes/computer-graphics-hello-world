package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL33;

public class VertexBufferObject {

    private final int id;

    public VertexBufferObject() {
        this.id = GL33.glGenBuffers();
    }

    public void bind() {
        GL33.glBindBuffer(
                GL33.GL_ARRAY_BUFFER,
                this.id
        );
    }

    public void updateData(final float[] data) {
        GL33.glBufferData(
                GL33.GL_ARRAY_BUFFER,
                data,
                GL33.GL_STATIC_DRAW
        );
    }

    public void updateData(final int target, final float[] data, final int type) {
        GL33.glBufferData(
                GL33.GL_ARRAY_BUFFER,
                data,
                GL33.GL_STATIC_DRAW
        );
    }

    public int getId() {
        return this.id;
    }
}
