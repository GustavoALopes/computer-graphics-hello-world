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
        if(this.keyboard.isKeyDown(KeyboardListener.Key.A)) {
            this.position.x -= 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.Q)) {
            this.position.z += 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.E)) {
            this.position.z -= 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.D)) {
            this.position.x += 0.1f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.W)) {
            this.position.y += 0.05f;
        }

        if(this.keyboard.isKeyDown(KeyboardListener.Key.S)) {
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


//        final var negativeCameraPosition = new Vector3f(-this.position.x, -this.position.y, -this.position.z);
//        return viewMatrix.multiply(Matrix4f.translate(
//                negativeCameraPosition.x,
//                negativeCameraPosition.y,
//                negativeCameraPosition.z
//        ));
//        return new Matrix4f()
//                .lookAt(
//                        this.position,
//                        new Vector3f(0f, 0f, 0f), //look to origin
//                        new Vector3f(0f, 1f, 0f)
//                );
    }
}
