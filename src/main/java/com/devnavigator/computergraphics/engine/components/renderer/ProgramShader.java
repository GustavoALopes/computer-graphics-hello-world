package com.devnavigator.computergraphics.engine.components.renderer;

import com.devnavigator.computergraphics.components.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public abstract class ProgramShader {

    private final int id;


    public ProgramShader() {
        this.id = GL33.glCreateProgram();
    }

    public void attachShader(final Shader shader) {
        GL33.glAttachShader(
                this.id,
                shader.getId()
        );
    }

    public void link() {
        GL33.glLinkProgram(this.id);
        this.loadAllAttributePosition();
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
            value.get(buffer);
            GL33.glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void updateUniformValue(final int location, final float value) {
        GL33.glUniform1f(location, value);
    }

    public void updateUniformValue(final int location, final boolean value) {
        final float finalValue = value ? 1 : 0;
        GL33.glUniform1f(location, finalValue);
    }


    public void updateUniformValue(final int location, final Vector3f vector) {
        GL33.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    abstract void enableAllVertexPointer();

    abstract void disableAllVertexPointer();

    protected abstract void loadAllAttributePosition();

    public void updateProjection(final Matrix4f projection) {
        final var location = this.getProjectionLocation();
        this.updateUniformValue(location, projection);
    }

    public void updateView(final Matrix4f viewMatrix) {
        final var location = this.getViewLocation();
        this.updateUniformValue(location, viewMatrix);
    }

    public void updateTransformation(final Matrix4f transformation) {
        final var location = this.getTransformationLocation();
        this.updateUniformValue(location, transformation);
    }

    public void updateLight(final Light light) {
        final var lightPosition = this.getLightPosition();
        final var lightColorPosition = this.getLightColorPosition();

        this.updateUniformValue(lightPosition, light.getPosition());
        this.updateUniformValue(lightColorPosition, light.getColor());
    }

    public void updateShineDamper(final float shineDamper) {
        final var location = this.getShineDamperLocation();
        this.updateUniformValue(location, shineDamper);
    }

    public void updateReflectivity(final float reflectivity) {
        final var location = this.getReflectivityLocation();
        this.updateUniformValue(location, reflectivity);
    }

    protected abstract int getLightColorPosition();

    protected abstract int getLightPosition();

    protected abstract int getTransformationLocation();

    protected abstract int getViewLocation();

    protected abstract int getProjectionLocation();

    protected abstract int getShineDamperLocation();

    protected abstract int getReflectivityLocation();

    protected abstract int getUseFakeLightingLocation();

    public abstract void updateUseFakeLighting(final boolean useFakeLight);
}
