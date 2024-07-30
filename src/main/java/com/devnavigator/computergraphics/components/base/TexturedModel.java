package com.devnavigator.computergraphics.components.base;

import com.devnavigator.computergraphics.engine.components.Texture;

public class TexturedModel {

    private final RawModel rawModel;

    private final Texture texture;

    private TexturedModel(
            final RawModel rawModel,
            final Texture texture
    ) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return this.rawModel;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public static TexturedModel create(
            final RawModel rawModel,
            final Texture texture
    ) {
        return new TexturedModel(
                rawModel,
                texture
        );
    }
}
