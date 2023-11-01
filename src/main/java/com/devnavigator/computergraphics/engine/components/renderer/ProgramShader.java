package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class ProgramShader {

    private final int id;


    public ProgramShader() {
        this.id = GL30.glCreateProgram();
    }

    public void attachShader(final Shader shader) {
        GL33.glAttachShader(
                this.id,
                shader.getId()
        );
    }

    public void link() {
        GL33.glLinkProgram(this.id);
    }

    public void use() {
        GL33.glUseProgram(this.id);
    }

    public void enableVertexAttribArray(final int index) {
        GL33.glEnableVertexAttribArray(index);
    }

    public void addVertexAttrib(
            final int index,
            final int size,
            final int stride,
            final long offset
    ) {
        GL33.glVertexAttribPointer(
                index,
                size,
                GL33.GL_FLOAT,
                false,
                stride,
                offset
        );
    }

    public void dispose() {
        GL33.glDeleteProgram(this.id);
    }

    public void disableVertexAttribArray(final int index) {
        GL33.glDisableVertexAttribArray(index);
    }

    public int getUniformLocation(
            final CharSequence name
    ) {
        return GL30.glGetUniformLocation(
                this.id,
                name
        );
    }
}
