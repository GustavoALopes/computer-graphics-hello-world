package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;

public class Triangle implements IGraphicModel {


    private final RawModel model;

    private final float[] data;

    private final int numVertex;

    public Triangle(
            final float[] points
    ) {
        this.data = points;
        this.numVertex = 3;

        this.model = RawModel.create(
                points,
                2
        );
    }

    public float[] getData() {
        return this.data;
    }

    public int getNumVertex() {
        return this.numVertex;
    }

    public RawModel getModel() {
        return this.model;
    }

    public static Triangle create(
            final Point one,
            final Point two,
            final Point three
    ) {
        final float[] points = {
                one.getX(), one.getY(),
                two.getX(), two.getY(),
                three.getX(), three.getY()
        };

        return new Triangle(points);
    }
}
