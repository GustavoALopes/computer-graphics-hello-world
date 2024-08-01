package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final KeyboardListener keyboard;

    private Vector3f position;

    private float pitch;

    private float yaw;

    private float roll;


    public Camera(final KeyboardListener keyboardListener) {
        this.position = new Vector3f(0, 1f, 0);
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
        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_4)) {
            this.position.x -= 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_7)) {
            this.position.z += 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_9)) {
            this.position.z -= 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_6)) {
            this.position.x += 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_8)) {
            this.position.y += 0.05f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.NUM_KP_5)) {
            this.position.y -= 0.05f;
        }

        final var viewMatrix = this.createViewMatrix();

        programShader.updateView(viewMatrix);
    }

    private Matrix4f createViewMatrix() {

        return new Matrix4f()
                            .rotate((float) Math.toRadians(this.pitch), 1, 0, 0)
                            .rotate((float) Math.toRadians(this.yaw), 0, 1, 0)
                            .translate(-this.position.x, -this.position.y, -this.position.z);
    }
}
