package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;

public class Square extends GraphicModel {

    public Square(
            final float[] data
    ) {
        super(
            RawModel.create(
                    data,
                    2
            )
            .addIndexBuffer(new int[]{
                    0, 1, 3,
                    3, 1, 2
            }),
            6,
                1,
                0
        );
    }

    public static Square create(
            final Point one,
            final Point two,
            final Point three,
            final Point four
    ) {
        final float[] points = {
                one.getX(), one.getY(),
                two.getX(), two.getY(),
                three.getX(), three.getY(),
                four.getX(), four.getY()
        };

        return new Square(points);
    }
}
