package com.devnavigator.computergraphics.engine.components.renderer;

import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.math.Vector3f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class ProgramShader {

    private final int id;


    public ProgramShader() {
        this.id = GL33.glCreateProgram();
    }

    public void attachShader(final Shader shader) {
        GL33.glAttachShader(
                this.id,
                shader.getId()
        );

        shader.delete();
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
        return GL33.glGetUniformLocation(
                this.id,
                name
        );
    }

    public void updateUniformValue(final int location, final Matrix4f value) {
        try(final var stack = MemoryStack.stackPush()) {
            final var buffer = stack.mallocFloat(4*4);
            value.toBuffer(buffer);
            GL33.glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void updateUniformValue(final int location, final float value) {
        GL33.glUniform1f(location, value);
    }


    public void updateUniformValue(final int location, final Vector3f vector) {
        GL33.glUniform3f(location, vector.x, vector.y, vector.z);
    }
}
