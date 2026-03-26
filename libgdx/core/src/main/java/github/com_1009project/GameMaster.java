package github.com_1009project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
 
import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EntityRenderer;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.MovementManager;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.logicEngine.FoodQueueSystem;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.logicEngine.scenes.GameScene;
import github.com_1009project.logicEngine.scenes.MainMenuScene;
import github.com_1009project.logicEngine.scenes.PauseScene;
import github.com_1009project.logicEngine.scenes.TutorialScene;
import github.com_1009project.logicEngine.scenes.SettingsScene;
import github.com_1009project.abstractEngine.Ifactory;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.logicEngine.entities.*;
import github.com_1009project.logicEngine.factories.*;
import github.com_1009project.abstractEngine.OutputManager;
import github.com_1009project.logicEngine.helpers.GameAssets;
import github.com_1009project.logicEngine.helpers.GameRegistry;

public class GameMaster extends ApplicationAdapter{
    private EntityRegistry entityRegistry;
    private EntityRenderer entityRenderer;
    private MapEntityLoader mapEntityLoader;
    private SceneManager sceneManager;
    private EventManager eventManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private UIFactory uiFactory;
    private CollisionManager collisionManager;
    private AssetManager assetManager;
    private CameraManager camera;
    private MapManager mapManager;
    private SpriteBatch batch;
    private FoodQueueSystem foodQueueSystem;
    private OutputManager outputManager;

    // camera properties
    private int width, height;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // this is our set up, to initialize our managers and variables
    @Override
    public void create() {
        assetManager = new AssetManager();
        eventManager = new EventManager();
        inputManager = new InputManager(eventManager);
        batch = new SpriteBatch();

        entityRegistry = new EntityRegistry();
        entityRenderer = new EntityRenderer(entityRegistry);
        mapEntityLoader = new MapEntityLoader(entityRegistry);
        movementManager = new MovementManager(entityRegistry);

        sceneManager = new SceneManager(entityRegistry, eventManager, batch);
        sceneManager.registerScene(0, () -> new MainMenuScene(0, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager));
        sceneManager.registerScene(1, () -> new GameScene(1, assetManager, entityRegistry, entityRenderer, mapEntityLoader, eventManager, inputManager, batch, sceneManager, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        sceneManager.registerScene(2, () -> new SettingsScene(2, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager));
        sceneManager.registerScene(3, () -> new TutorialScene(3, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager));
        sceneManager.registerScene(99, () -> new PauseScene(99, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager));

        outputManager = new OutputManager(assetManager, "GameSettings", Event.SettingsChanged);

        // load assets
        GameAssets.loadAll(assetManager);
        assetManager.update();
        assetManager.finishLoading();

        // register entities and audio
        GameRegistry.registerEntities(entityRegistry, mapEntityLoader, assetManager);
        GameRegistry.registerAudio(outputManager);

        //eventmanager adds movementManager as an event observer
		eventManager.addObserver(movementManager);
        eventManager.addObserver(outputManager); // add OutputManager as an observer to handle audio events

		//key mappings for inputManager
		inputManager.mapKey(Input.Keys.W, Event.PlayerUp);
		inputManager.mapKey(Input.Keys.S, Event.PlayerDown);
		inputManager.mapKey(Input.Keys.A, Event.PlayerLeft);
		inputManager.mapKey(Input.Keys.D, Event.PlayerRight);
		inputManager.mapKey(Input.Keys.RIGHT, Event.PlayerRight);
		inputManager.mapKey(Input.Keys.LEFT, Event.PlayerLeft);
        inputManager.mapKey(Input.Keys.E, Event.PlayerInteract);
        inputManager.mapKey(Input.Keys.F, Event.PlayerChop);
        inputManager.mapKey(Input.Keys.ESCAPE, Event.GamePause);
		
		sceneManager.loadScene(0);
    }

    // our main gameplay/simulation loop
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        float deltaTime = Gdx.graphics.getDeltaTime();

        sceneManager.updateScene(deltaTime);
        sceneManager.renderScene();
    }

    @Override
    public void resize(int width, int height) {
        if (sceneManager.getCurrentScene() instanceof GameScene) {
            ((GameScene) sceneManager.getCurrentScene()).resize(width, height);
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
        sceneManager.dispose();
        eventManager.dispose();
        inputManager.dispose();
    }
}