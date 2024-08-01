package com.devnavigator.computergraphics.engine;

import com.devnavigator.computergraphics.components.Light;
import com.devnavigator.computergraphics.components.Player;
import com.devnavigator.computergraphics.components.Terrain;
import com.devnavigator.computergraphics.components.base.GraphicModel;
import com.devnavigator.computergraphics.components.base.ResourceManager;
import com.devnavigator.computergraphics.components.base.TexturedModel;
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

    private Map<Texture, List<GraphicModel>> models;

    private List<Light> lights;

    private List<Terrain> terrains;

    private boolean isRunning;

    private Player playerTest;

    public Engine() {
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

        this.renderer.getTerrainShader().use();

        final var playerTextureModel = ResourceManager.getOrCreate(
                Path.of("src/main/resources/models/stanford-bunny.obj"),
                Path.of("src/main/resources/textures/white-texture.png"),
                ProgramShader.Types.ENTITY
        );

        final Player player = new Player(
            playerTextureModel,
            new Vector3f(0, 0, -20),
            new Vector3f(0, 0, 0),
                0.1f,
            this.keyboardListener
        );

        this.playerTest = player;


        this.addModelToRender(
                playerTextureModel.getTexture(),
                player
        );


        final var treeModel = ResourceManager.getOrCreate(
                Path.of("src/main/resources/models/tree.obj"),
                Path.of("src/main/resources/textures/tree.png"),
                ProgramShader.Types.ENTITY
        );

        final var tallGrassRawModel = ResourceManager.getRawModel(
                Path.of("src/main/resources/models/tall-grass.obj"),
                ProgramShader.Types.ENTITY
        );

        final var tallGrassTexture = ResourceManager.getTexture(
                Path.of("src/main/resources/textures/tall-grass.png")
        );
        tallGrassTexture.enableTransparency();
        tallGrassTexture.enableFakeLight();

        final var tallGrassModel = TexturedModel.create(
                tallGrassRawModel,
                tallGrassTexture
        );

        final var random = new Random();

        for (var i = 0; i < 100; i++) {
            final var treeEntity = GraphicModel.create(treeModel)
                .setPosition(new Vector3f(random.nextFloat()*128 - 64, 0, random.nextFloat() * -100))
                .build();

            this.addModelToRender(
                    treeEntity.getModel().getTexture(),
                    treeEntity
            );

            final var tallGrassEntity = GraphicModel.create(tallGrassModel)
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

        final var blendMap = ResourceManager.getTexture(
                Path.of("src/main/resources/textures/blend-map.png")
        );

        final var texturePack = Terrain.TexturesPack.create(
                ResourceManager.getTexture(
                        Path.of("src/main/resources/textures/grass.png")
                ),
                ResourceManager.getTexture(
                        Path.of("src/main/resources/textures/mud.png")
                ),
                ResourceManager.getTexture(
                        Path.of("src/main/resources/textures/grass-flowers.png")
                ),
                ResourceManager.getTexture(
                        Path.of("src/main/resources/textures/path-rocks.png")
                )
        );

        final var terrain = Terrain.create(
                0,
                -1,
                128,
                blendMap,
                texturePack
        );

        final var terrain2 = Terrain.create(
                -1,
                -1,
                128,
                blendMap,
                texturePack
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
            final GraphicModel graphicModel
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

            Clock.update();

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

            this.playerTest.move();

            this.window.update();
        }
    }
}
