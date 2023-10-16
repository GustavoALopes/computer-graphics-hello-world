package com.devnavigator.computergraphics.components;

public class Point {

    private final float x;

    private final float y;

    private Point(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static Point create(
            final float x,
            final float y
    ) {
        return new Point(x, y);
    }
}
