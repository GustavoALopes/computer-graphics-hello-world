package com.devnavigator.computergraphics.engine;

import com.devnavigator.computergraphics.components.Light;
import com.devnavigator.computergraphics.components.Point;
import com.devnavigator.computergraphics.components.Square;
import com.devnavigator.computergraphics.components.Terrain;
import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.NewGraphicModel;
import com.devnavigator.computergraphics.components.base.ResourceManager;
import com.devnavigator.computergraphics.engine.components.*;
import com.devnavigator.computergraphics.engine.components.colors.ColorEnum;
import com.devnavigator.computergraphics.engine.components.renderer.EntityShader;
import com.devnavigator.computergraphics.engine.components.renderer.ProgramShader;
import com.devnavigator.computergraphics.engine.components.renderer.Shader;
import com.devnavigator.computergraphics.engine.components.renderer.TerrainShader;
import com.devnavigator.computergraphics.engine.interfaces.IEngine;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import java.nio.file.Path;
import java.util.*;

public class Engine implements IEngine {

    private final Window window;

    private final KeyboardListener keyboardListener;

    private final Renderer renderer;

    private GraphicModel modelTest;

    private final ResourceManager resourceManager;

    private Map<Texture, List<NewGraphicModel>> models;

    private List<Light> lights;

    private List<Terrain> terrains;

    private boolean isRunning;

    public Engine() {
        this.resourceManager = ResourceManager.create();
        this.keyboardListener = new KeyboardListener();
        this.window = new Window(
                2048,
                1024,
                "Hello world"
        );

        this.renderer = new Renderer(
                1024,
                740,
                this.keyboardListener
        );

        this.models = new HashMap<>();
        this.lights = new ArrayList<>();
        this.terrains = new ArrayList<>();
    }

    public void start() {
        this.init();
        this.loop();
        this.dispose();
    }

    public void stop() {
        this.isRunning = false;
    }

    private void dispose() {
        this.window.dispose();
        this.renderer.dispose();
    }

    private void init() {
        this.keyboardListener.bindWindow(this.window);

        final var entityShader = this.createEntityShader(
                "src/main/resources/shaders/default.vert",
                "src/main/resources/shaders/default.frag"
        );

        final var terrainShader = this.createTerrainShader(
                "src/main/resources/shaders/terrain.vert",
                "src/main/resources/shaders/terrain.frag"
        );

        this.renderer.attachProgramShader(entityShader);
        this.renderer.attachProgramShader(terrainShader);

        this.renderer.init();

//        this.addModelToRender(
//                textureTest,
//                Square.create(
//                        Point.create(-0.5f, 0.5f),
//                        Point.create(-0.5f, -0.5f),
//                        Point.create(0.5f, -0.5f),
//                        Point.create(0.5f, 0.5f)
//                ).addTexture(textureTest, new float[] {
//                         0,0, //VO,
//                         0,1, //V1
//                         1,1, //V2
//                         1,0
//               })
//        );

////         .changeColor(1, 255, 255, 255)
////         .translate(0.5f, 0.5f, 0.f)
//         .translate(-0.1f, 0.0f, -1f)
////         .changeScale(0.35f, 0.35f, 1f)
//         .addTexture(
//                 "src/main/resources/textures/default.png",
//                 new float[] {
//                         0,0, //VO,
//                         0,1, //V1
//                         1,1, //V2
//                         1,0
//                 }
//         )
//        );

        this.renderer.getTerrainShader().use();


//        final var whiteTexture = Texture.loadTexture("src/main/resources/textures/white-texture.png");
//        final var entity = GraphicModel.loadFromObj(
//                Path.of("src/main/resources/models/dragon.obj"),
//                entityTexture,
//                10,
//                0
//        ).setPosition(new Vector3f(20, 0, 20));
//
//        this.addModelToRender(
//                entityTexture,
//                entity
//        );

//        this.modelTest = entity;

        final var treeModel = this.resourceManager.getOrCreate(
                Path.of("src/main/resources/models/tree.obj"),
                Path.of("src/main/resources/textures/tree.png"),
                ProgramShader.Types.ENTITY
        );


        final var tallGrassModel = this.resourceManager.getOrCreate(
                Path.of("src/main/resources/models/tall-grass.obj"),
                Path.of("src/main/resources/textures/tall-grass.png"),
                ProgramShader.Types.ENTITY
        );

        final var random = new Random();

        for (var i = 0; i < 100; i++) {
            final var treeEntity = NewGraphicModel.create(treeModel)
                .setPosition(new Vector3f(random.nextFloat()*128 - 64, 0, random.nextFloat() * -100))
                .build();

            this.addModelToRender(
                    treeEntity.getModel().getTexture(),
                    treeEntity
            );

            final var tallGrassEntity = NewGraphicModel.create(tallGrassModel)
                    .setPosition(new Vector3f(random.nextFloat()*128 - 64, 0, random.nextFloat() * -100))
                    .setScale(.5f)
                    .build();

            this.addModelToRender(
                    tallGrassEntity.getModel().getTexture(),
                    tallGrassEntity
            );
        }

        final var light = Light.create(
                new Vector3f(20000, 20000, 20000),
                ColorEnum.WHITE.getValue()
        );

        final var terrainGrassTexture = this.resourceManager.getTexture(
            Path.of("src/main/resources/textures/grass.png")
        );

//        final var terrainGrassTexture = Texture.loadTexture("src/main/resources/textures/grass.png");
        final var terrain = Terrain.create(
                0,
                -1,
                128,
                terrainGrassTexture
        );

        final var terrain2 = Terrain.create(
                -1,
                -1,
                128,
                terrainGrassTexture
        );

        final var lightTerrain = Light.create(
                new Vector3f(-1, -1, 10f),
                ColorEnum.WHITE.getValue()
        );

//        model.increasePosition(0, 0, -30f);
//        model.increaseRotation(0, 30f, 0);
//        terrain.increasePosition(-20, -1, -25f);
//        terrain2.increasePosition(0, 0, -30f);

        this.terrains.add(terrain);
        this.terrains.add(terrain2);
        this.lights.add(light);
//        this.lights.add(lightTerrain);


//        this.models.add(Triangle.create(
//                Point.create(0.0f, 0.5f),
//                Point.create(-0.5f, -0.5f),
//                Point.create(0.5f, -0.5f)
//        )
//        .changeColor(
//                1,
//                255,
//                0,
//                100
//        )
//        .translate(-0.5f, 0.5f, 1f)
//        .changeScale(0.35f, 0.35f, 1f));

        this.isRunning = true;
    }

    private void addModelToRender(
            final Texture texture,
            final NewGraphicModel graphicModel
    ) {
        if(!this.models.containsKey(texture)) {
            this.models.put(texture, new ArrayList<>());
        }

        this.models.get(texture).add(graphicModel);
    }

    private EntityShader createEntityShader(
            final String vertexShader,
            final String fragmentShader
    ) {
        final var program = new EntityShader();
        this.attachShader(
                program,
                vertexShader,
                fragmentShader
        );
        return program;
    }

    private TerrainShader createTerrainShader(
            final String vertexShader,
            final String fragmentShader
    ) {
        final var program = new TerrainShader();
        this.attachShader(
                program,
                vertexShader,
                fragmentShader
        );
        return program;
    }

    private ProgramShader attachShader(
            final ProgramShader shader,
            final String vertexShader,
            final String fragmentShader
    ) {
        shader.attachShader(Shader.loadShader(
                Shader.Type.VERTEX,
                vertexShader
        ));

        shader.attachShader(Shader.loadShader(
                Shader.Type.FRAGMENT,
                fragmentShader
        ));
        return shader;
    }

    private void loop() {
        GL33.glClearColor(0f, 0f, 1f, 1f);
        while(this.isRunning) {

            if(this.window.shouldClose()) {
                this.isRunning = false;
            }

            GL33.glEnable(GL33.GL_DEPTH_TEST);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT|GL33.GL_DEPTH_BUFFER_BIT);

            this.renderer.prepareRenderEntity();
            this.renderer.renderEntity(
                this.models,
                this.lights
            );

            this.renderer.prepareRenderTerrain();
            this.renderer.renderTerrain(
                    this.terrains,
                    this.lights
            );

            this.window.update();
        }
    }
}
