package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

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

    public BaseGraphicModel addTexture(final Texture texture) {
//        this.model.addTexture(
//                texture.
//        )
        return this;
    }

    public BaseGraphicModel rotate(
            final int shaderLocation,
            final float angle
    ) {
        this.rotate = Matrix4f.rotate(
                angle,
                0f,
                0f,
                1f
        );

//        try(final var stack = MemoryStack.stackPush()) {
//            final var buffer = stack.mallocFloat(4*4);
//            rotate.toBuffer(buffer);
//            GL33.glUniformMatrix4fv(shaderLocation, false, buffer);
//        }
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
        return this.translate.multiply(this.rotate).multiply(this.scale);
    }
}
