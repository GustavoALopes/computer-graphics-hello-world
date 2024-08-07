package com.devnavigator.computergraphics.engine.components.renderer;

import org.lwjgl.opengl.GL33;

public class TerrainShader extends ProgramShader {

    public static int POSITION_LAYER = 0;
    public static int TEXT_COORDS_LAYER = 1;
    public static int NORMALS_LAYER = 2;

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;

    private int blendMapLocation;

    private int backgroundColorLocation;

    private int rTextureLocation;

    private int gTextureLocation;

    private int bTextureLocation;

    private int shineDamperLocation;

    private int lightColorPositionLocation;

    private int reflectivityLocation;

    public TerrainShader() {
        super();
    }

    @Override
    public void enableAllVertexPointer() {
        GL33.glEnableVertexAttribArray(POSITION_LAYER);
        GL33.glEnableVertexAttribArray(TEXT_COORDS_LAYER);
        GL33.glEnableVertexAttribArray(NORMALS_LAYER);
    }

    @Override
    public void disableAllVertexPointer() {
        GL33.glDisableVertexAttribArray(POSITION_LAYER);
        GL33.glDisableVertexAttribArray(TEXT_COORDS_LAYER);
        GL33.glDisableVertexAttribArray(NORMALS_LAYER);
    }

    @Override
    protected void loadAllAttributePosition() {
        this.transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
        this.lightPositionLocation = super.getUniformLocation("lightPosition");
        this.lightColorPositionLocation = super.getUniformLocation("lightColorPosition");

        this.blendMapLocation = super.getUniformLocation("blendMap");
        this.backgroundColorLocation = super.getUniformLocation("backgroundColor");
        this.rTextureLocation = super.getUniformLocation("rTexture");
        this.gTextureLocation = super.getUniformLocation("gTexture");
        this.bTextureLocation = super.getUniformLocation("bTexture");
    }

    public void connectTextures() {
        this.updateUniformValue(this.blendMapLocation, 4);

        this.updateUniformValue(this.backgroundColorLocation, 0);
        this.updateUniformValue(this.rTextureLocation, 1);
        this.updateUniformValue(this.gTextureLocation, 2);
        this.updateUniformValue(this.bTextureLocation, 3);
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
        return this.lightPositionLocation;
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
        return -1;
    }

    @Override
    public void updateUseFakeLighting(boolean useFakeLight) {
        return;
    }
}
