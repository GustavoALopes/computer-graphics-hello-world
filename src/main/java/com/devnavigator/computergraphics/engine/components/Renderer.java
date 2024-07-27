package com.devnavigator.computergraphics.engine.components;

import com.devnavigator.computergraphics.components.Light;
import com.devnavigator.computergraphics.components.Terrain;
import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.NewGraphicModel;
import com.devnavigator.computergraphics.engine.components.renderer.EntityShader;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.TerrainShader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Renderer {

    private final Projection projection;

    private final Camera camera;

    private EntityShader entityShaders;

    private TerrainShader terrainShader;


    public Renderer(
            final float displayWidth,
            final float displayHeight,
            final KeyboardListener keyboardListener
    ) {
        GL.createCapabilities();
        this.projection = Projection.create(
                displayWidth,
                displayHeight
        );

        this.camera = new Camera(keyboardListener);
    }

    public TerrainShader getTerrainShader() {
        return this.terrainShader;
    }

    public void init() {
        //Try to implement it to handle with alpha channel and apply transparecy
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.enableCullFace(GL33.GL_BACK);
        this.projection.update(this.entityShaders);
        this.projection.update(this.terrainShader);

        this.entityShaders.use();
    }

    public void attachProgramShader(
            final ProgramShader programShader
    ) {
        if(programShader instanceof EntityShader sh) {
            this.entityShaders = sh;
            this.entityShaders.link();
        } else if(programShader instanceof TerrainShader sh) {
            this.terrainShader = sh;
            this.terrainShader.link();
        }
    }

    private void enableCullFace(final int mode) {
        GL33.glEnable(GL33.GL_CULL_FACE);
        GL33.glCullFace(mode);
    }

    private void disableCullFace() {
        GL33.glDisable(GL33.GL_CULL_FACE);
    }

    public void renderEntity(
//            final Map<Texture, List<GraphicModel>> models,
            final Map<Texture, List<NewGraphicModel>> models,
            final Collection<Light> lights
    ) {
        final var light = lights.stream().findFirst().orElse(null);
        for(final var texture : models.keySet()) {
            this.prepareTexture(texture);
            final var entities = models.get(texture);
            for(final var entity : entities) {
                this.render(entity, light, this.entityShaders);
            }
            this.cleanup();
        }
        this.entityShaders.disableAllVertexPointer();
    }

    private void prepareTexture(final Texture texture) {
        GL33.glActiveTexture(GL33.GL_TEXTURE0);
        if(texture.isHasTransparency()) {
            this.disableCullFace();
        }
        texture.bind();
    }

    public void render(
//        final GraphicModel model,
        final NewGraphicModel model,
        final Light light,
        final ProgramShader shader
    ) {
        GL33.glBindVertexArray(model.getModel().getRawModel().getVao().getId());

//        model.increasePosition(0f, 0f, -0.01f);
//        model.increaseRotation(0f, 10f, 0);
//        model.rotate(
//            (float)Math.toRadians(
//                    GLFW.glfwGetTime() * 360.0
//            )
//        );

        shader.updateTransformation(model.getTransformationMatrix());
        shader.updateLight(light);
        shader.updateUseFakeLighting(model.getModel().getTexture().isUseFakeLight());
        shader.updateShineDamper(0);//model.getShineDamper());
        shader.updateReflectivity(0);//model.getReflectivity());


//        final var lightPosition = shader.getUniformLocation("lightPosition");
//        final var lightColor = shader.getUniformLocation("lightColor");

//        final var shineDamper = shader.getUniformLocation("shineDamper");
//        final var reflectivity = shader.getUniformLocation("reflectivity");

//        this.entityShaders.updateUniformValue(
//                lightPosition,
//                light.getPosition()
//        );
//
//        this.entityShaders.updateUniformValue(
//                lightColor,
//                light.getColor()
//        );
//
//        this.entityShaders.updateUniformValue(
//                shineDamper,
//                model.getShineDamper()
//        );
//
//        this.entityShaders.updateUniformValue(
//                reflectivity,
//                model.getReflectivity()
//        );

        this.flush(model.getModel().getRawModel().getNumbersOfVertex());
    }

    public void flush(final int numVertex) {
        GL33.glDrawElements(
                GL33.GL_TRIANGLES,
                numVertex,
                GL33.GL_UNSIGNED_INT,
                0
        );
    }

    private void cleanup() {
        this.enableCullFace(GL33.GL_BACK);
        GL33.glBindVertexArray(0);
    }


    public void dispose() {
        this.entityShaders.dispose();
    }

    public void renderTerrain(
            final List<Terrain> terrains,
            final List<Light> lights
    ) {
        final var light = lights.get(0);
        terrains.stream().findAny().ifPresent(terrain -> {
            this.prepareTexture(terrain.getModel().getTexture());
        });

        for(final var terrain: terrains) {
            this.render(terrain, light, this.terrainShader);
            this.cleanup();
        }
        this.terrainShader.disableAllVertexPointer();
    }

    public void prepareRenderEntity() {
        this.entityShaders.use();
        this.entityShaders.enableAllVertexPointer();
        this.camera.update(this.entityShaders);
    }

    public void prepareRenderTerrain() {
        this.terrainShader.use();
        this.terrainShader.enableAllVertexPointer();
        this.camera.update(this.terrainShader);
    }
}
