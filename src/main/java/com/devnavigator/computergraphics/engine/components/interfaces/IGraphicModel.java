package com.devnavigator.computergraphics.engine.components.interfaces;

import com.devnavigator.computergraphics.components.base.RawModel;

public interface IGraphicModel {
    float[] getData();

    int getNumVertex();

    RawModel getModel();
}
