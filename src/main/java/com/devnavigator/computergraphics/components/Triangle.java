package com.devnavigator.computergraphics.components;

import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.RawModel;
import com.devnavigator.computergraphics.components.base.TexturedModel;
import com.devnavigator.computergraphics.engine.components.IndexBuffer;
import com.devnavigator.computergraphics.engine.components.renderer.EntityShader;
import com.devnavigator.computergraphics.engine.components.renderer.VertexArrayObject;
import com.devnavigator.computergraphics.engine.components.renderer.VertexBufferObject;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import java.util.Map;

public class Triangle extends GraphicModel {

    public Triangle(
            final VertexArrayObject vao,
            final VertexBufferObject vbo,
            final IndexBuffer index,
            final int numberOfVertex
    ) {
        super(
                TexturedModel.create(
                        RawModel.create(
                                vao,
                                Map.of("position", vbo),
                                index,
                                numberOfVertex
                        ),
                        null
                ),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0),
                1L
        );
    }

    public static Triangle create(
            final Point one,
            final Point two,
            final Point three
    ) {
        final var vao = new VertexArrayObject();
        vao.bind();

        final var vbo = new VertexBufferObject();
        vbo.bind();

        final float[] points = {
                one.getX(), one.getY(),
                two.getX(), two.getY(),
                three.getX(), three.getY()
        };

        vbo.updateData(points);

        GL33.glVertexAttribPointer(
                EntityShader.POSITION_LAYER,
                2,
                GL33.GL_FLOAT,
                false,
                0,
                0L
        );

        GL33.glEnableVertexAttribArray(EntityShader.POSITION_LAYER);

        final var indexValue = new int[]{
                0, 1, 2
        };

        final var index = new IndexBuffer(indexValue.length);
        index.bind();
        index.updateData(indexValue);

        return new Triangle(vao, vbo, index, indexValue.length);
    }
}
