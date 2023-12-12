package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.math.Vector3f;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;

public class Camera {

    private final KeyboardListener keyboard;

    private Vector3f position;


    private float pitch;

    private float yaw;

    private float roll;


    public Camera(final KeyboardListener keyboardListener) {
        this.position = new Vector3f();
        this.keyboard = keyboardListener;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void update(final ProgramShader programShader) {
        if(this.keyboard.isKeyDown(KeyboardListener.Key.A)) {
            this.position.x += 0.02f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.D)) {
            this.position.x -= 0.02f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.W)) {
            this.position.z += 0.02f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.S)) {
            this.position.z -= 0.02f;
        }

        final var viewMatrix = createViewMatrix();

        final var location = programShader.getUniformLocation("viewMatrix");
        programShader.updateUniformValue(
                location,
                viewMatrix
        );
    }

    private Matrix4f createViewMatrix() {
        var viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        viewMatrix = viewMatrix.rotate((float) Math.toRadians(this.pitch), 1, 0, 0);
        viewMatrix = viewMatrix.rotate((float) Math.toRadians(this.yaw), 0, 1, 0);


        final var negativeCameraPosition = new Vector3f(-this.position.x, -this.position.y, -this.position.z);
        return viewMatrix.multiply(Matrix4f.translate(
                negativeCameraPosition.x,
                negativeCameraPosition.y,
                negativeCameraPosition.z
        ));
    }
}
