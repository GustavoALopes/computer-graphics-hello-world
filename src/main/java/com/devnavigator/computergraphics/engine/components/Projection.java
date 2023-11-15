package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;

public class Projection {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;

    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;


    public Projection(
        final float aspectRation
    ) {
        this.projectionMatrix = Matrix4f.perspective(
                FOV,
                aspectRation,
                NEAR_PLANE,
                FAR_PLANE
        );
    }

    public static Projection create(
            final float displayWidth,
            final float displayHeight
    ) {
        final var aspectRation = displayWidth / displayHeight;
        return new Projection(aspectRation);
    }

    public void init(final ProgramShader programShader) {
        final var location = programShader.getUniformLocation("projectionMatrix");
        programShader.updateUniformValue(
                location,
                this.projectionMatrix
        );
    }
}
