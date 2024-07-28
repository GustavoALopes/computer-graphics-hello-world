package com.devnavigator.computergraphics.components.base;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GraphicModel {

    private final TexturedModel model;

    private Vector3f position;

    private Vector3f rotation;

    private float scale;

    protected GraphicModel(
            final TexturedModel model,
            final Vector3f position,
            final Vector3f rotation,
            final float scale
    ) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public TexturedModel getModel() {
        return this.model;
    }

    public GraphicModel setPosition(
            final Vector3f newPosition
    ) {
        this.position = newPosition;
        return this;
    }

    public GraphicModel setRotation(
            final Vector3f newRotation
    ) {
        this.rotation = newRotation;
        return this;
    }

    public Matrix4f getTransformationMatrix() {
        final var matrix = new Matrix4f();

        matrix.translate(this.position);

        matrix.rotateX((float)Math.toRadians(this.rotation.x));
        matrix.rotateY((float)Math.toRadians(this.rotation.y));
        matrix.rotateZ((float)Math.toRadians(this.rotation.z));

        matrix.scale(this.scale);

        return matrix;
    }

    public static Builder create(
            final TexturedModel texturedModel
    ) {
        return new Builder(texturedModel);
    }

    public static class Builder {

        private final TexturedModel texturedModel;

        private Vector3f position;

        private Vector3f rotation;

        private float scale;

        private Builder(TexturedModel texturedModel) {
            this.texturedModel = texturedModel;
            this.position = new Vector3f(0, 1, 0);
            this.rotation = new Vector3f(0, 0, 0);
            this.scale = 1;
        }

        public Builder setPosition(final Vector3f newPosition) {
            this.position = newPosition;
            return this;
        }

        public Builder setRotation(final Vector3f rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder setScale(final float scale) {
            this.scale = scale;
            return this;
        }

        public GraphicModel build() {
            return new GraphicModel(
                    this.texturedModel,
                    this.position,
                    this.rotation,
                    this.scale
            );
        }

    }
}
