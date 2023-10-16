package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;

import java.nio.IntBuffer;

public class IndexBuffer {

    private final int id;

    private final IntBuffer buffer;

    public IndexBuffer(final int size) {
        this.id = GL33.glGenBuffers();
        this.buffer = BufferUtils.createIntBuffer(size);
    }

    public int getId() {
        return this.id;
    }

    public void bind() {
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, this.id);
    }

    public void updateData(
            final int[] indexes
    ) {
        this.addDataInBuffer(indexes);
        GL33.glBufferData(
                GL33.GL_ELEMENT_ARRAY_BUFFER,
                this.buffer,
                GL33.GL_STATIC_DRAW
        );
    }

    private void addDataInBuffer(
            final int[] indexes
    ) {
        this.buffer.put(indexes);
        this.buffer.flip();
    }
}
