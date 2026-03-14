package github.com_1009project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.CameraManager;
import github.com_1009project.abstractEngine.CollisionManager;
import github.com_1009project.abstractEngine.MapManager;
import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.MovementManager;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.logicEngine.PauseScene;
import github.com_1009project.logicEngine.testEntity;
import github.com_1009project.abstractEngine.Event;
import com.badlogic.gdx.Input;

public class GameMaster extends ApplicationAdapter{
    private EntityManager entityManager;
    private SceneManager sm;
    private EventManager eventManager;
    private MovementManager movementManager;
    private UIFactory uf;
    private CollisionManager collisionManager;
    private AssetManager assetManager;
    private CameraManager camera;
    private MapManager mapManager;
    private SpriteBatch batch;
    private testEntity player;

    // camera properties
    private int width, height;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // this is our set up, to initialize our managers and variables
    @Override
    public void create() {
        collisionManager = new CollisionManager(128);
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        entityManager = new EntityManager(assetManager);
        eventManager = new EventManager();
        movementManager = new MovementManager(entityManager);
        sm = new SceneManager(assetManager, entityManager, eventManager, batch);

        // set up camera with max world bounds
        camera = new CameraManager(width, height);
        camera.setBounds(4000, 4000);

        // load assets
        // update the asset manager to actually load the assets
        // finishLoading() makes sure all assets are loaded before proceeding
        loadAssets();
        assetManager.update();
        assetManager.finishLoading();

        // set up the map
        // scale the map if needed, if textures look small
        // load the map from file
        // parse collision layer and add collision boxes to entities list, "Collision" can be changed to how the developer wants to name it in Tiled
        mapManager = new MapManager(entityManager);
        mapManager.setScale(4.0f); 
        loadMap("maps/test.tmx");

        sm.loadScene(1);

        //eventmanager adds movementManager as an event observer
		eventManager.addObserver(movementManager);
        eventManager.addObserver(sm); // add SceneManager as an observer to handle pause events

		//key mappings for eventManager
		eventManager.mapKey(Input.Keys.W, Event.PlayerUp);
		eventManager.mapKey(Input.Keys.S, Event.PlayerDown);
		eventManager.mapKey(Input.Keys.A, Event.PlayerLeft);
		eventManager.mapKey(Input.Keys.D, Event.PlayerRight);
		eventManager.mapKey(Input.Keys.RIGHT, Event.PlayerRight);
		eventManager.mapKey(Input.Keys.LEFT, Event.PlayerLeft);
		eventManager.mapKey(Input.Keys.SPACE, Event.PlayerJump);
        eventManager.mapKey(Input.Keys.E, Event.PlayerInteract);
        eventManager.mapKey(Input.Keys.ESCAPE, Event.GamePause);

		// Register input processor
		Gdx.input.setInputProcessor(eventManager);
    }

    // our main gameplay/simulation loop
    // the loop should be process input -> update -> collision -> render
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        // time between each frame, this ensures same speed on different devices
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Check if paused
        if (sm.getCurrentScene() instanceof PauseScene) {
            // Only update and render the Pause UI
            sm.updateScene(deltaTime);
            sm.renderScene();
            return; // Skip the rest of the game logic below
        }

        if (player != null && player.mapToLoad != null) {
            String newMap = player.mapToLoad;
            player.mapToLoad = null; // reset the mapToLoad variable
            loadMap(newMap);
            return;
        }

        // update all entities
        entityManager.update(deltaTime);

        // update collisions
        collisionManager.updateCollision(entityManager.getEntities());

        // update camera position
        camera.cameraUpdate(deltaTime);

        // render entities and map based on camera position
        mapManager.render(camera.getCamera());
        
        if (player.hasCollided) {
            camera.shake(2f, 0.2f);
            player.hasCollided = false;
        }

        // batch will render entities according to cameraPosition
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        entityManager.render(batch);
        batch.end();
    }

    // this method is used to load a new map, it clears the current entities and loads the new map's entities
    // used when transitioning between maps, the player entity will have a variable that specifies which map to load, and when the variable is not null, this method will be called with the new map name
    private void loadMap(String mapName) {
        entityManager.dispose();

        mapManager.setMap(assetManager.get(mapName, TiledMap.class));
        System.out.println("Loaded map: " + mapName);
        mapManager.loadEntities();

        // find player entity and set camera target to player.
        for (Entity entity : entityManager.getEntities()) {
            if (entity instanceof testEntity) {
                player = (testEntity) entity;
                break;
            }
        }
        camera.setTarget(player);
    }

    // an example of how to use the asset manager to load assets, this can be expanded to load more assets as needed
    // can also use a json file or other data file to specify assets to load, and parse that file in this method to load assets in bulk
    private void loadAssets() {
        // load textures
        assetManager.load("imgs/boy_down_1.png", Texture.class);

        // load tmx maps, params required to prevent errors
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.projectFilePath = "maps/test.tiled-project";
        assetManager.load("imgs/background.png", Texture.class);
        assetManager.load("maps/test.tmx", TiledMap.class, params);
        assetManager.load("maps/tests.tmx", TiledMap.class, params);
    }

    // resize is called whenever ApplicationAdapter detects a change in screen size, this can be used to adjust the camera viewport and other properties as needed
    @Override
    public void resize(int width, int height) {
        camera.resize(width, height);
        mapManager.resize(width, height);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
        mapManager.dispose();
        collisionManager.dispose();
        sm.dispose();
    }
}
