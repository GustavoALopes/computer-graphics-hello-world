package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import com.devnavigator.computergraphics.engine.components.math.Vector3f;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private boolean keyAPress;

    private boolean keyDPress;

    private boolean keyWPress;

    private Vector3f position;


    private float pitch;

    private float yaw;

    private float roll;


    public Camera() {
        this.position = new Vector3f();
        this.keyAPress = false;
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

    public void move(final long windowId, final int key, final int scanCode, final int action, final int mods) {
        if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_A && !this.keyAPress) {
            this.keyAPress = true;
        } else if (action == GLFW.GLFW_RELEASE && key == GLFW.GLFW_KEY_A && this.keyAPress) {
            this.keyAPress = false;
        }

        if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_D && !this.keyDPress) {
            this.keyDPress = true;
        } else if (action == GLFW.GLFW_RELEASE && key == GLFW.GLFW_KEY_D && this.keyDPress) {
            this.keyDPress = false;
        }

        if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_W && !this.keyWPress) {
            this.keyWPress = true;
        } else if (action == GLFW.GLFW_RELEASE && key == GLFW.GLFW_KEY_W && this.keyWPress) {
            this.keyWPress = false;
        }
    }

    public void update(final ProgramShader programShader) {
        if(this.keyAPress) {
            this.position.x += 0.02f;
        }
        if(this.keyDPress) {
            this.position.x -= 0.02f;
        }

        if(this.keyWPress) {
            this.position.z += 0.02f;
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
