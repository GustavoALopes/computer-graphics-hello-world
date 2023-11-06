package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.util.Objects;
import java.util.Optional;

public abstract class BaseGraphicModel {

    protected final RawModel model;

    private final int numVertex;

    private Matrix4f translate;

    private Matrix4f scale;

    private Matrix4f rotate;

    public BaseGraphicModel(
            final RawModel model,
            final int numVertex
    ) {
        this.model = model;
        this.numVertex = numVertex;
    }

    public RawModel getModel() {
        return this.model;
    }

    public int getNumVertex() {
        return this.numVertex;
    }

    public BaseGraphicModel addTexture(
            final String texturePath,
            final float[] coordinates
    ) {
        return this.addTexture(
                Texture.loadTexture(texturePath),
                coordinates
        );
    }

    public BaseGraphicModel addTexture(
            final Texture texture,
            final float[] coordinates
    ) {
        this.model.addTexture(
                2,
                texture,
                coordinates
        );
        return this;
    }

    public BaseGraphicModel rotate(
            final float angle
    ) {
        this.rotate = Matrix4f.rotate(
                angle,
                0f,
                0f,
                1f
        );
        return this;
    }

    public BaseGraphicModel changeColor(
            final int shaderIndexAttrib,
            final int[] color
    ) {
        this.model.changeColor(
                shaderIndexAttrib,
                color
        );
        return this;
    }

    public BaseGraphicModel changeColor(
            final int shaderIndexAttrib,
            final int red,
            final int green,
            final int blue
    ) {
        if(red > 255) {
            throw new RuntimeException("The red value color need to be into range of 0 at 255");
        } else if (green > 255) {
            throw new RuntimeException("The green value color need to be into range of 0 at 255");
        } else if (blue > 255) {
            throw new RuntimeException("The blue value color need to be into range of 0 at 255");
        }

        return this.changeColor(shaderIndexAttrib, new int[]{ red, green, blue });
    }

    public BaseGraphicModel changeScale(final float x, final float y, final float z) {
        this.scale = Matrix4f.scale(x, y, z);
        return this;
    }

    public BaseGraphicModel translate(final float x, final float y, final float z) {
        this.translate = Matrix4f.translate(x, y, z);
        return this;
    }

    public Matrix4f render() {
        var matrix = Optional.ofNullable(this.translate)
                .orElse(new Matrix4f());

        if(!Objects.isNull(this.rotate)) {
            matrix = matrix.multiply(this.rotate);
        }
        if(!Objects.isNull(this.scale)) {
            matrix = matrix.multiply(this.scale);
        }

        return matrix;
    }

    public BaseGraphicModel incrementPosition(
            final float x,
            final float y,
            final float z
    ) {
        this.translate = this.translate.multiply(Matrix4f.translate(x, y, z));
        return this;
    }
}
