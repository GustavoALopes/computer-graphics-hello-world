package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.BaseGraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;

public class Square extends BaseGraphicModel {

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
            6
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
