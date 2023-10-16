package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL33;

public class VertexArrayObject {

    private final int id;

    public VertexArrayObject() {
        this.id = GL33.glGenVertexArrays();
    }

    public void bind() {
        GL33.glBindVertexArray(this.id);
    }

    public int getId() {
        return this.id;
    }
}
