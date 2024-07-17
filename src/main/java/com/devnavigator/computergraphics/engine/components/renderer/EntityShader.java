package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL33;

public class EntityShader extends ProgramShader {

    public static int POSITION_LAYER = 0;

    public static int TEXT_COORD_LAYER = 2;

    public static int NORMALS_LAYER = 3;

    private int transformationMatrixLocation;

    private int projectionMatrixLocation;

    private int viewMatrixLocation;

    private int lightPositionLocation;

    private int lightColorPositionLocation;

    private int shineDamperLocation;

    private int reflectivityLocation;

    private int useFakeLightingLocation;

    public EntityShader() {
        super();
    }

    @Override
    public void enableAllVertexPointer() {
        GL33.glEnableVertexAttribArray(POSITION_LAYER);
        GL33.glEnableVertexAttribArray(TEXT_COORD_LAYER);
        GL33.glEnableVertexAttribArray(NORMALS_LAYER);
    }

    @Override
    public void disableAllVertexPointer() {
        GL33.glDisableVertexAttribArray(POSITION_LAYER);
        GL33.glDisableVertexAttribArray(TEXT_COORD_LAYER);
        GL33.glDisableVertexAttribArray(NORMALS_LAYER);
    }

    @Override
    protected void loadAllAttributePosition() {
        this.transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
        this.lightPositionLocation = super.getUniformLocation("lightPosition");
        this.lightColorPositionLocation = super.getUniformLocation("lightColor");
        this.shineDamperLocation = super.getUniformLocation("shineDamper");
        this.reflectivityLocation = super.getUniformLocation("reflectivity");
        this.useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
    }

    @Override
    protected int getProjectionLocation() {
        return this.projectionMatrixLocation;
    }

    @Override
    protected int getViewLocation() {
        return this.viewMatrixLocation;
    }

    @Override
    protected int getTransformationLocation() {
        return this.transformationMatrixLocation;
    }

    @Override
    protected int getLightColorPosition() {
        return this.lightColorPositionLocation;
    }

    @Override
    protected int getLightPosition() {
        return this.lightPositionLocation;
    }

    @Override
    protected int getShineDamperLocation() {
        return this.shineDamperLocation;
    }

    @Override
    protected int getReflectivityLocation() {
        return this.reflectivityLocation;
    }

    @Override
    protected int getUseFakeLightingLocation() {
        return this.useFakeLightingLocation;
    }

    @Override
    public void updateUseFakeLighting(final boolean value) {
        final var location = this.getUseFakeLightingLocation();
        this.updateUniformValue(location, value);
    }
}
