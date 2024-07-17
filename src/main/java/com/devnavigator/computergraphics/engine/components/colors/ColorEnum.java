package com.devnavigator.computergraphics.engine.components.colors;


import org.joml.Vector3f;

public enum ColorEnum {
    WHITE(new Vector3f(1f, 1f, 1f));

    private Vector3f value;

    ColorEnum(final Vector3f value) {
        this.value = value;
    }

    public Vector3f getValue() {
        return value;
    }
}
