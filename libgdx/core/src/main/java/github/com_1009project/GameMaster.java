package github.com_1009project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EntityRenderer;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.abstractEngine.MovementManager;
import github.com_1009project.abstractEngine.OutputManager;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.logicEngine.helpers.GameAssets;
import github.com_1009project.logicEngine.helpers.GameContext;
import github.com_1009project.logicEngine.helpers.GameRegistry;
import github.com_1009project.logicEngine.scenes.GameScene;

public class GameMaster extends ApplicationAdapter{
    private SceneManager sceneManager;
    private EventManager eventManager;
    private InputManager inputManager;
    private AssetManager assetManager;
    private SpriteBatch batch;

    // camera properties
    private int width, height;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // this is our set up, to initialize our managers and variables
    @Override
    public void create() {
        // GameMaster systems
        assetManager = new AssetManager();
        eventManager = new EventManager();
        inputManager = new InputManager(eventManager);
        batch = new SpriteBatch();

        // setup systems
        EntityRegistry entityRegistry = new EntityRegistry();
        EntityRenderer entityRenderer = new EntityRenderer(entityRegistry);
        MapEntityLoader mapEntityLoader = new MapEntityLoader(entityRegistry);
        MovementManager movementManager = new MovementManager(entityRegistry);
        OutputManager outputManager = new OutputManager(assetManager, "GameSettings", Event.SettingsChanged);

        // scene manager (also needs entity registry and event manager)
        sceneManager = new SceneManager(entityRegistry, eventManager, batch);

        // create game context
        GameContext gameContext = new GameContext(assetManager, entityRegistry, entityRenderer, mapEntityLoader, eventManager, inputManager, batch, sceneManager, outputManager, movementManager);

        // load assets
        GameAssets.loadAll(assetManager);

        // register entity factories, map loaders, input mappings, audio, and event observers
        GameRegistry.registerAll(gameContext, width, height);

        // load the first scene (main menu)
		sceneManager.loadScene(0);
    }

    // render loop, updates and renders the current scene
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float deltaTime = Gdx.graphics.getDeltaTime();
        sceneManager.updateScene(deltaTime);
        sceneManager.renderScene();
    }

    // resize method, called when the window is resized, resizes the current scene
    @Override
    public void resize(int width, int height) {
        if (sceneManager.getCurrentScene() != null) {
            sceneManager.getCurrentScene().resize(width, height);
        }
    }

    // dispose method, called when the application is closed, disposes of all resources
    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
        sceneManager.dispose();
        eventManager.dispose();
        inputManager.dispose();
    }
}