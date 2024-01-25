package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;

public class Triangle extends GraphicModel {

    public Triangle(
            final float[] points
    ) {
        super(
            RawModel.create(
                points,
                2
            )
            .addIndexBuffer(new int[]{
                    0, 1, 2
            }),
            3
        );
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
