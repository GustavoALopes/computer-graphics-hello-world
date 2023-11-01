package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.Texture;
import com.devnavigator.computergraphics.engine.components.math.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public abstract class BaseGraphicModel {

    protected final RawModel model;

    private final int numVertex;

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
        final var rotate = Matrix4f.rotate(
                angle,
                0f,
                0f,
                1f
        );

        try(final var stack = MemoryStack.stackPush()) {
            final var buffer = stack.mallocFloat(4*4);
            rotate.toBuffer(buffer);
            GL33.glUniformMatrix4fv(shaderLocation, false, buffer);
        }
        return this;
    }
}
