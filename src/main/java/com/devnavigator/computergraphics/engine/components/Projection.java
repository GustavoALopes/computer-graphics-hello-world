package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

public class Projection {

    private static final float FOV = (float) Math.toRadians(45f);
    private static final float NEAR_PLANE = 0.1f;

    private static final float FAR_PLANE = 100.0f;

    private Matrix4f projectionMatrix;


    public Projection(
        final float aspectRation
    ) {
        this.projectionMatrix = new Matrix4f().setPerspective(
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

    public void update(final ProgramShader programShader) {
        programShader.use();
        programShader.updateProjection(this.projectionMatrix);
        GL33.glUseProgram(0);
    }
}
