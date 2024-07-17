package com.devnavigator.computergraphics.components;

import org.joml.Vector3f;

public class Light {

    private Vector3f position;

    private Vector3f color;

    private Light(
            final Vector3f position,
            final Vector3f color
    ) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public static Light create(
            final Vector3f position,
            final Vector3f colour
    ) {
        return new Light(
                position,
                colour
        );
    }
}
