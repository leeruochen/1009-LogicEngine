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

public class GameMaster extends ApplicationAdapter{
    private EntityRegistry entityRegistry;
    private EntityRenderer entityRenderer;
    private MapEntityLoader mapEntityLoader;
    private SceneManager sm;
    private EventManager eventManager;
    private MovementManager movementManager;
    private UIFactory uf;
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
        batch = new SpriteBatch();

        entityRegistry = new EntityRegistry();
        entityRenderer = new EntityRenderer(entityRegistry);
        mapEntityLoader = new MapEntityLoader(entityRegistry);

        entityRegistry.registerFactory(Bun.class, new BunFactory(assetManager));
        entityRegistry.registerFactory(Cheese.class, new CheeseFactory(assetManager));
        entityRegistry.registerFactory(ChoppingStation.class, new ChoppingStationFactory(assetManager));
        entityRegistry.registerFactory(CollisionBox.class, new CollisionBoxFactory());
        entityRegistry.registerFactory(Counter.class, new CounterFactory(assetManager));
        entityRegistry.registerFactory(CounterSubmission.class, new CounterSubmissionFactory(assetManager));
        entityRegistry.registerFactory(IngredientBox.class, new IngredientBoxFactory());
        entityRegistry.registerFactory(Lettuce.class, new LettuceFactory(assetManager));
        entityRegistry.registerFactory(Patty.class, new PattyFactory(assetManager));
        entityRegistry.registerFactory(PlateBox.class, new PlateBoxFactory(assetManager));
        entityRegistry.registerFactory(Player.class, new PlayerFactory(assetManager));
        entityRegistry.registerFactory(Plate.class, new PlateFactory(assetManager));
        entityRegistry.registerFactory(RubbishBin.class, new RubbishBinFactory(assetManager));
        entityRegistry.registerFactory(Stove.class, new StoveFactory(assetManager));
        entityRegistry.registerFactory(Tomato.class, new TomatoFactory(assetManager));

        // Register type aliases for TiledMap entity type names
        mapEntityLoader.registerTypeAlias("CollisionBox", new CollisionBoxFactory());
        mapEntityLoader.registerTypeAlias("Counter", new CounterFactory(assetManager));
        mapEntityLoader.registerTypeAlias("ChoppingStation", new ChoppingStationFactory(assetManager));
        mapEntityLoader.registerTypeAlias("Stove", new StoveFactory(assetManager));
        mapEntityLoader.registerTypeAlias("RubbishBin", new RubbishBinFactory(assetManager));
        mapEntityLoader.registerTypeAlias("CounterSubmission", new CounterSubmissionFactory(assetManager));
        mapEntityLoader.registerTypeAlias("Player", new PlayerFactory(assetManager));
        mapEntityLoader.registerTypeAlias("PlateBox", new PlateBoxFactory(assetManager));

        // Register food box type aliases — each creates an IngredientBox with the correct texture and ingredient name
        for (String ingredient : new String[]{"Bun", "Patty", "Lettuce", "Tomato", "Cheese", "Meat"}) {
            final String ingredientLower = ingredient.toLowerCase();
            final String texturePath = "foodstations/" + ingredientLower + "_box.png";
            mapEntityLoader.registerTypeAlias(ingredient + "Box", new Ifactory<IngredientBox>() {
                @Override
                public IngredientBox createEntity(float x, float y, float width, float height) {
                    Texture boxTexture = assetManager.get(texturePath, Texture.class);
                    return new IngredientBox(x, y, width, height, boxTexture, ingredientLower);
                }
            });
        }
        entityRegistry.registerFactory(RubbishBin.class, new RubbishBinFactory(assetManager));
        entityRegistry.registerFactory(Stove.class, new StoveFactory(assetManager));
        entityRegistry.registerFactory(Tomato.class, new TomatoFactory(assetManager));
        movementManager = new MovementManager(entityRegistry);

        sm = new SceneManager(assetManager, entityRegistry, eventManager, batch);
        sm.registerScene(0, () -> new MainMenuScene(0, assetManager, entityRegistry, eventManager, batch, sm));
        sm.registerScene(1, () -> new GameScene(1, assetManager, entityRegistry, entityRenderer, mapEntityLoader, eventManager, batch, sm, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        sm.registerScene(2, () -> new SettingsScene(2, assetManager, entityRegistry, eventManager, batch, sm));
        sm.registerScene(3, () -> new TutorialScene(3, assetManager, entityRegistry, eventManager, batch, sm));
        sm.registerScene(99, () -> new PauseScene(99, assetManager, entityRegistry, eventManager, batch, sm));

        outputManager = new OutputManager(assetManager);
        outputManager.registerMusic(Event.GameStart, "sounds/LevelMusic.mp3");
        outputManager.registerMusic(Event.MenuEnter, "sounds/MainMenuMusic.mp3");
        outputManager.registerSound(Event.Chopping, "sounds/Chop.mp3");
        outputManager.registerSound(Event.SubmissionCorrect, "sounds/SubmissionCorrect.mp3");
        outputManager.registerSound(Event.SubmissionWrong, "sounds/SubmissionWrong.mp3");
        outputManager.registerSound(Event.PlayerInteractSound, "sounds/Place.mp3");
        outputManager.registerSound(Event.IngredientTake, "sounds/IngredientTake.mp3");
        outputManager.registerSound(Event.Bin, "sounds/Bin.mp3");


        // set up camera with max world bounds
        camera = new CameraManager(width, height);
        camera.setBounds(4000, 4000);

        // load assets
        loadAssets();
        assetManager.update();
        assetManager.finishLoading();

        //eventmanager adds movementManager as an event observer
		eventManager.addObserver(movementManager);
        eventManager.addObserver(sm); // add SceneManager as an observer to handle pause events
        eventManager.addObserver(outputManager); // add OutputManager as an observer to handle audio events

		//key mappings for eventManager
		eventManager.mapKey(Input.Keys.W, Event.PlayerUp);
		eventManager.mapKey(Input.Keys.S, Event.PlayerDown);
		eventManager.mapKey(Input.Keys.A, Event.PlayerLeft);
		eventManager.mapKey(Input.Keys.D, Event.PlayerRight);
		eventManager.mapKey(Input.Keys.RIGHT, Event.PlayerRight);
		eventManager.mapKey(Input.Keys.LEFT, Event.PlayerLeft);
        eventManager.mapKey(Input.Keys.E, Event.PlayerInteract);
        eventManager.mapKey(Input.Keys.F, Event.PlayerChop);
        eventManager.mapKey(Input.Keys.ESCAPE, Event.GamePause);
		
		sm.loadScene(0);
    }

    // our main gameplay/simulation loop
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        float deltaTime = Gdx.graphics.getDeltaTime();

        sm.updateScene(deltaTime);
        sm.renderScene();
    }

    private void loadAssets() {
        // load textures
        for(int i=0; i<52; i++){
            assetManager.load(String.format("imgs/bg_frames/tile%03d.png", i), Texture.class);
        }
        assetManager.load("imgs/background.png", Texture.class);
        assetManager.load("foodstations/rubbishBin.png", Texture.class);
        assetManager.load("foodstations/counter.png", Texture.class);
        assetManager.load("foodstations/counter_submission.png", Texture.class);
        assetManager.load("foodstations/counter_choppingboard.png", Texture.class);
        assetManager.load("foodstations/stove_cooking.png", Texture.class);
        assetManager.load("foodstations/stove.png", Texture.class);
        assetManager.load("foodstations/plate_box.png", Texture.class);
        assetManager.load("foodstations/patty_box.png", Texture.class);
        assetManager.load("foodstations/bun_box.png", Texture.class);
        assetManager.load("foodstations/lettuce_box.png", Texture.class);
        assetManager.load("foodstations/cheese_box.png", Texture.class);
        assetManager.load("foodstations/tomato_box.png", Texture.class);

        assetManager.load("character/human_idle.png", Texture.class);
        assetManager.load("character/human_run.png", Texture.class);
        assetManager.load("character/human_death.png", Texture.class);
        assetManager.load("character/human_chop1.png", Texture.class);
        assetManager.load("character/human_chop2.png", Texture.class);

        // Raw ingredient textures (used when spawning from boxes)
        assetManager.load("food/bun.png", Texture.class);
        assetManager.load("food/lettuce.png", Texture.class);
        assetManager.load("food/tomato.png", Texture.class);
        assetManager.load("food/cheese.png", Texture.class);
        assetManager.load("food/patty.png", Texture.class);

        // Chopped / cooked ingredient textures
        assetManager.load("food/lettuce_chopped.png", Texture.class);
        assetManager.load("food/tomato_chopped.png", Texture.class);
        assetManager.load("food/cheese_chopped.png", Texture.class);
        assetManager.load("food/patty_cooked.png", Texture.class);
        assetManager.load("food/patty_burnt.png", Texture.class);

        // Plate / dish texture
        assetManager.load("food/dish.png", Texture.class);
        
        // Food textures + UI skin (for the order queue HUD)
        FoodQueueSystem.queueAssets(assetManager);

        // load music
        assetManager.load("sounds/MainMenuMusic.mp3", Music.class);
        assetManager.load("sounds/LevelMusic.mp3", Music.class);

        // load sounds
        assetManager.load("sounds/Chop.mp3", Sound.class);
        assetManager.load("sounds/SubmissionCorrect.mp3", Sound.class);
        assetManager.load("sounds/SubmissionWrong.mp3", Sound.class);
        assetManager.load("sounds/IngredientTake.mp3", Sound.class);
        assetManager.load("sounds/Place.mp3", Sound.class);
        assetManager.load("sounds/Bin.mp3", Sound.class);

        // load tmx maps
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.projectFilePath = "maps/test.tiled-project";
        assetManager.load("maps/kitchen.tmx", TiledMap.class, params);
        assetManager.finishLoading();
    }

    @Override
    public void resize(int width, int height) {
        if (sm.getCurrentScene() instanceof GameScene) {
            ((GameScene) sm.getCurrentScene()).resize(width, height);
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
        sm.dispose();
        eventManager.dispose();
    }
}
