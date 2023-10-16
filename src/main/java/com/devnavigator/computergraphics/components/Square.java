package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.engine.components.interfaces.IGraphicModel;

public class Square implements IGraphicModel {

    private final float[] data;

    private final int numVertex;

    private final RawModel model;

    public Square(
            final float[] data
    ) {
        this.data = data;
        this.numVertex = 6;

        this.model = RawModel.create(
                data,
                2
        )
        .addIndexBuffer(new int[]{
                0, 1, 3,
                3, 1, 2
        });
    }

    @Override
    public float[] getData() {
        return this.data;
    }

    @Override
    public int getNumVertex() {
        return this.numVertex;
    }

    @Override
    public RawModel getModel() {
        return this.model;
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
