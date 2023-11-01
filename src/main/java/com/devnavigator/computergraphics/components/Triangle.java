package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;

public class Triangle extends BaseGraphicModel {

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
