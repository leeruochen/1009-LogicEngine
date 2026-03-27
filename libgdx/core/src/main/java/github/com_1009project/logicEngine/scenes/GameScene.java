package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EntityRenderer;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;
import github.com_1009project.logicEngine.FoodQueueSystem;
import github.com_1009project.logicEngine.InteractionManager;
import github.com_1009project.logicEngine.entities.Player;
import github.com_1009project.logicEngine.helpers.GameContext;

// This class is the main game scene, where the player plays the game. 
// It contains the main game loop, which updates and renders all game systems and entities. 
// It also handles the round timer, times-up overlay, and results overlay. 
// It is loaded when the player starts the game from the main menu.

public class GameScene extends Scene {
    // Engine systems
    private final CameraManager      camera;
    private final MapManager         mapManager;
    private final CollisionManager   collisionManager;
    private final InteractionManager interactionManager;
    private final FoodQueueSystem    foodQueueSystem;
    private final SceneController    sceneController;
    private final ShapeRenderer      shapeRenderer;
    private final EntityRenderer     entityRenderer;
    private final AssetManager       assetManager;
    private final EventManager       eventManager;
    private final SceneManager       sceneManager;
    private final SpriteBatch        batch;
    private final EntityRegistry     entityRegistry;
    private final MapEntityLoader    mapEntityLoader;
    private final InputManager       inputManager;

    private Player player;

    // Post-round helpers
    private RoundTimer     roundTimer;
    private TimesUpOverlay timesUpOverlay;
    private ResultsOverlay resultsOverlay;

    // Scene state
    private static final float ROUND_DURATION = 180f; // 3 minutes
    private static final float TIMES_UP_DELAY = 3.0f; // seconds before results appear

    private enum GameState { PLAYING, TIMES_UP, RESULTS }
    private GameState currentState;
    private float     postGameTimer;

    // Shared UI resources
    private final UILayer uiLayer;
    private final Skin    skin;

    // Constructor
    public GameScene(int id, int width, int height, GameContext gameContext) {
        super(id);

        this.entityRenderer = gameContext.getEntityRenderer();
        this.assetManager    = gameContext.getAssetManager();
        this.eventManager    = gameContext.getEventManager();
        this.sceneManager    = gameContext.getSceneManager();
        this.entityRegistry  = gameContext.getEntityRegistry();
        this.batch           = gameContext.getSpriteBatch();
        this.inputManager    = gameContext.getInputManager();
        this.mapEntityLoader = gameContext.getMapEntityLoader();
        this.shapeRenderer  = new ShapeRenderer();

        this.camera = new CameraManager(width, height);
        this.camera.setBounds(2112, 2176);

        this.collisionManager = new CollisionManager(64);

        this.mapManager = new MapManager(mapEntityLoader);
        this.mapManager.setScale(4.0f);

        this.foodQueueSystem = new FoodQueueSystem(batch, assetManager, eventManager);
        this.foodQueueSystem.create();

        this.interactionManager = new InteractionManager(
                entityRegistry, assetManager,
                foodQueueSystem.getFoodQueue(), eventManager);
        eventManager.addObserver(interactionManager);

        this.sceneController = new SceneController(sceneManager, eventManager);

        // Shared UI resources
        this.uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        this.skin = gameContext.getAssetManager().isLoaded("menu/uiskin.json")
                ? gameContext.getAssetManager().get("menu/uiskin.json", Skin.class)
                : new Skin(Gdx.files.internal("menu/uiskin.json"));

        init();
    }

    // Lifecycle
    @Override
    public void init() {
        currentState  = GameState.PLAYING;
        postGameTimer = 0f;

        // Each helper creates its own Stage actors into the shared uiLayer
        roundTimer     = new RoundTimer(ROUND_DURATION, uiLayer, skin, eventManager);
        timesUpOverlay = new TimesUpOverlay(uiLayer, skin);
        resultsOverlay = new ResultsOverlay(uiLayer, skin, sceneManager);

        loadMap("maps/kitchen.tmx");
    }

    @Override
    public void onEnter() {
        super.onEnter();

        if (currentState == GameState.PLAYING) {
            // Normal first entry — start the game music
            eventManager.eventTrigger(Event.GameStart);
        } else {
            // Returning from a sub-scene (e.g. Settings) while results are showing.
            // Settings replaced the input processor; restore it to the results stage.
            resultsOverlay.restoreInputProcessor();
        }
    }

    @Override
    public void update(float delta) {
        switch (currentState) {

            case PLAYING:
                entityRegistry.update(delta);
                collisionManager.updateCollision(entityRegistry.getCollidableEntities());
                camera.cameraUpdate(delta);
                foodQueueSystem.update(delta);
                interactionManager.update(delta);
                roundTimer.update(delta);

                if (roundTimer.isExpired()) {
                    currentState = GameState.TIMES_UP;
                    timesUpOverlay.show();
                    sceneController.setEnabled(false);
                }
                break;

            case TIMES_UP:
                postGameTimer += delta;
                if (postGameTimer >= TIMES_UP_DELAY) {
                    currentState = GameState.RESULTS;
                    timesUpOverlay.hide();
                    resultsOverlay.show(foodQueueSystem.getFoodQueue().getClearedCount());
                }
                break;

            case RESULTS:
                // Scene2D handles button input automatically — nothing to update here
                break;
        }
    }

    @Override
    public void render() {
        // World geometry
        mapManager.render(camera.getCamera());
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        entityRenderer.render(batch);
        batch.end();

        // Food-queue HUD 
        foodQueueSystem.render(Gdx.graphics.getDeltaTime());

        // Dim overlay 
        if (currentState != GameState.PLAYING) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.65f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // UI stage 
        super.render();
    }

    // Resize
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.resize(width, height);
        mapManager.resize(width, height);
    }

    // Disposal
    @Override
    public void dispose() {
        super.dispose();
        mapManager.dispose();
        foodQueueSystem.dispose();
        eventManager.removeObserver(interactionManager);
        sceneController.dispose(eventManager);
        shapeRenderer.dispose();
    }

    // Private helpers
    private void loadMap(String mapName) {
        entityRegistry.disposeAll();
        mapManager.setMap(assetManager.get(mapName, TiledMap.class));
        System.out.println("Loaded map: " + mapName);
        mapManager.loadEntities();

        for (Entity entity : entityRegistry.getEntities()) {
            if (entity instanceof Player) {
                player = (Player) entity;
                break;
            }
        }
        camera.setTarget(player);
    }

    @Override
    public InputManager getSceneInputProcessor() {
        return inputManager;
    }
}