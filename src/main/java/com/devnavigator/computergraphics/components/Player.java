package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.TexturedModel;
import com.devnavigator.computergraphics.engine.components.Clock;
import com.devnavigator.computergraphics.engine.components.KeyboardListener;
import org.joml.Vector3f;

public class Player extends GraphicModel {

    private static final float FORWARD_ACCELERATION = 500f;

    private static final float TURN_ACCELERATION = 140f;

    private static final float JUMP_FORCE = 500f;

    private static final float GRAVITY = -10f;

    private final KeyboardListener keyboardListener;

    private float currentForwardAcceleration;

    private float currentTurnAcceleration;

    private float currentJumpForce;

    private boolean inTheAir;


    public Player(
            final TexturedModel model,
            final Vector3f position,
            final Vector3f rotation,
            final float scale,
            final KeyboardListener keyboardListener
    ) {
        super(model, position, rotation, scale);

        this.currentForwardAcceleration = 0;
        this.currentTurnAcceleration = 0;
        this.currentJumpForce = 0;
        this.inTheAir = false;
        this.keyboardListener = keyboardListener;
    }

    public void move() {
        handleInputs();

        this.increaseRotation(0, this.currentTurnAcceleration * Clock.getTimeFrameSecond(), 0);
        final float distance = this.currentForwardAcceleration * Clock.getTimeFrameSecond();
        final float dx = (float) (distance * Math.sin(Math.toRadians(this.rotation.y)));
        final float dz = (float) (distance * Math.cos(Math.toRadians(this.rotation.y)));

        this.increasePosition(dx, 0, dz);

        if(this.inTheAir) {
            this.currentJumpForce += GRAVITY;
            System.out.println(this.currentJumpForce);
            this.increasePosition(dx, this.currentJumpForce * Clock.getTimeFrameSecond(), dz);
        }

    }

    private void handleInputs() {
        if(this.keyboardListener.isKeyDown(KeyboardListener.Key.A)) {
            this.currentTurnAcceleration += TURN_ACCELERATION;
        } else if(this.keyboardListener.isKeyDown(KeyboardListener.Key.D)) {
            this.currentTurnAcceleration -= TURN_ACCELERATION;
        } else {
            this.currentTurnAcceleration = 0;
        }

        if(this.keyboardListener.isKeyDown(KeyboardListener.Key.W) && this.currentForwardAcceleration < 80) {
            this.currentForwardAcceleration += FORWARD_ACCELERATION;
        } else if(this.keyboardListener.isKeyDown(KeyboardListener.Key.S) && this.currentForwardAcceleration > -80) {
            this.currentForwardAcceleration -= FORWARD_ACCELERATION;
        } else {
            this.currentForwardAcceleration = 0;
        }

        if(this.keyboardListener.isKeyDown(KeyboardListener.Key.SPACE) && !this.inTheAir) {
            this.currentJumpForce = JUMP_FORCE;
            this.inTheAir = true;
        } else if(this.position.y < 0) {
            this.inTheAir = false;
            this.position.y = 0;
        }
    }
}
