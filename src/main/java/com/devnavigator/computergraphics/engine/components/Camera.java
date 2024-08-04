package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.engine.components.camera.interfaces.ICameraTarget;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final KeyboardListener keyboard;

    private final ICameraTarget target;

    private Vector3f position;

    private float pitch;

    private float yaw;

    private float roll;

    private float distanceFromTarget;

    private float angleAroundTarget;


    public Camera(
            final KeyboardListener keyboardListener,
            final ICameraTarget target
    ) {
        this.position = new Vector3f(0, 1, 0);
        this.pitch = 20;
        this.yaw = 180;
        this.roll = 0;
        this.distanceFromTarget = 10;
        this.target = target;
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
        calculatePitch();
        calculateZoom();
        calculateAngleAroundTarget();
        calculateCameraPosition();
        this.yaw = 180 - (this.target.getYRotation() + this.angleAroundTarget);

        final var viewMatrix = this.createViewMatrix();

        programShader.updateView(viewMatrix);
    }

    private void calculateCameraPosition() {
        final var theta = this.target.getYRotation() + this.angleAroundTarget;
        final var horizontalDistanceFromTarget = this.calculateHorizontalDistance();
        final var verticalDistanceFromTarget = this.calculateVerticalDistance();
        final var offsetX = horizontalDistanceFromTarget * Math.sin(Math.toRadians(theta));
        final var offsetZ = horizontalDistanceFromTarget * Math.cos(Math.toRadians(theta));

        this.position.x = (float) (this.target.getPosition().x - offsetX);
        this.position.z = (float) (this.target.getPosition().z - offsetZ);
        this.position.y = this.target.getPosition().y + verticalDistanceFromTarget;
    }

    private float calculateHorizontalDistance() {
        final var hD = (float) (this.distanceFromTarget * Math.cos(Math.toRadians(this.pitch)));
        if(hD < 0) {
            return 0;
        } else if (hD > 50) {
            return 50;
        }
        return hD;
    }

    private float calculateVerticalDistance() {
        final float vD = (float) (this.distanceFromTarget * Math.sin(Math.toRadians(this.pitch)));
        if(vD < 1) {
            return 1;
        } else if(vD > 15) {
            return 15;
        }
        return vD;
    }

    private Matrix4f createViewMatrix() {
        return new Matrix4f()
                            .rotate((float) Math.toRadians(this.pitch), 1, 0, 0)
                            .rotate((float) Math.toRadians(this.yaw), 0, 1, 0)
                            .translate(-this.position.x, -this.position.y, -this.position.z);
    }

    private void calculateZoom() {
        final var zoomLevel = MouseListener.getScrollOffSet();
        this.distanceFromTarget -= zoomLevel;
    }

    private void calculatePitch() {
        if(MouseListener.isDown(MouseListener.Buttons.Right)) {
            final var pitchChange = MouseListener.getDY() * .1f;
            this.pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundTarget() {
        if(MouseListener.isDown(MouseListener.Buttons.Left)) {
            final var angleChange = MouseListener.getDX() * .3f;
            this.angleAroundTarget -= angleChange;
        }
    }
}
