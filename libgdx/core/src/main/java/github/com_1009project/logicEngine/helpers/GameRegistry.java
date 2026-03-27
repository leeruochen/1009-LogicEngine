package github.com_1009project.logicEngine.helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.Ifactory;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.MapEntityLoader;
import github.com_1009project.abstractEngine.OutputManager;
import github.com_1009project.logicEngine.entities.Bun;
import github.com_1009project.logicEngine.entities.Cheese;
import github.com_1009project.logicEngine.entities.ChoppingStation;
import github.com_1009project.logicEngine.entities.CollisionBox;
import github.com_1009project.logicEngine.entities.Counter;
import github.com_1009project.logicEngine.entities.CounterSubmission;
import github.com_1009project.logicEngine.entities.IngredientBox;
import github.com_1009project.logicEngine.entities.Lettuce;
import github.com_1009project.logicEngine.entities.Patty;
import github.com_1009project.logicEngine.entities.Plate;
import github.com_1009project.logicEngine.entities.PlateBox;
import github.com_1009project.logicEngine.entities.Player;
import github.com_1009project.logicEngine.entities.RubbishBin;
import github.com_1009project.logicEngine.entities.Stove;
import github.com_1009project.logicEngine.entities.Tomato;
import github.com_1009project.logicEngine.factories.BunFactory;
import github.com_1009project.logicEngine.factories.CheeseFactory;
import github.com_1009project.logicEngine.factories.ChoppingStationFactory;
import github.com_1009project.logicEngine.factories.CollisionBoxFactory;
import github.com_1009project.logicEngine.factories.CounterFactory;
import github.com_1009project.logicEngine.factories.CounterSubmissionFactory;
import github.com_1009project.logicEngine.factories.IngredientBoxFactory;
import github.com_1009project.logicEngine.factories.LettuceFactory;
import github.com_1009project.logicEngine.factories.PattyFactory;
import github.com_1009project.logicEngine.factories.PlateBoxFactory;
import github.com_1009project.logicEngine.factories.PlateFactory;
import github.com_1009project.logicEngine.factories.PlayerFactory;
import github.com_1009project.logicEngine.factories.RubbishBinFactory;
import github.com_1009project.logicEngine.factories.StoveFactory;
import github.com_1009project.logicEngine.factories.TomatoFactory;
import github.com_1009project.logicEngine.scenes.GameScene;
import github.com_1009project.logicEngine.scenes.MainMenuScene;
import github.com_1009project.logicEngine.scenes.PauseScene;
import github.com_1009project.logicEngine.scenes.SettingsScene;
import github.com_1009project.logicEngine.scenes.TutorialScene;

// This class is a helper class to register all entities, factories, scenes, input mappings, and audio for the game. 
// It is called in the GameMaster create method to register everything at the start of the game. 
// It is also called whenever we need to re-register everything, such as when we change settings that affect entities or output.

public final class GameRegistry {

    // register all entities and factories, as well as map entity type aliases for map loading
    public static void registerEntities(GameContext gameContext) {
        EntityRegistry entityRegistry = gameContext.getEntityRegistry();
        MapEntityLoader mapEntityLoader = gameContext.getMapEntityLoader();
        AssetManager assetManager = gameContext.getAssetManager();

        // register factories for entities
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

        // register map loader
        mapEntityLoader.registerTypeAlias("CollisionBox", new CollisionBoxFactory());
        mapEntityLoader.registerTypeAlias("Counter", new CounterFactory(assetManager));
        mapEntityLoader.registerTypeAlias("ChoppingStation", new ChoppingStationFactory(assetManager));
        mapEntityLoader.registerTypeAlias("Stove", new StoveFactory(assetManager));
        mapEntityLoader.registerTypeAlias("RubbishBin", new RubbishBinFactory(assetManager));
        mapEntityLoader.registerTypeAlias("CounterSubmission", new CounterSubmissionFactory(assetManager));
        mapEntityLoader.registerTypeAlias("Player", new PlayerFactory(assetManager));
        mapEntityLoader.registerTypeAlias("PlateBox", new PlateBoxFactory(assetManager));
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
    }

    // register all audio for the game, mapping them to events that trigger them
    public static void registerAudio(OutputManager outputManager) {
        outputManager.registerMusic(Event.GameStart, "sounds/LevelMusic.mp3");
        outputManager.registerMusic(Event.MenuEnter, "sounds/MainMenuMusic.mp3");
        outputManager.registerSound(Event.Chopping, "sounds/Chop.mp3");
        outputManager.registerSound(Event.SubmissionCorrect, "sounds/SubmissionCorrect.mp3");
        outputManager.registerSound(Event.SubmissionWrong, "sounds/SubmissionWrong.mp3");
        outputManager.registerSound(Event.PlayerInteractSound, "sounds/Place.mp3");
        outputManager.registerSound(Event.IngredientTake, "sounds/IngredientTake.mp3");
        outputManager.registerSound(Event.Bin, "sounds/Bin.mp3");
    }

    // register all scenes for the game, mapping them to scene IDs that can be used to load them
    public static void registerScenes(GameContext gameContext, int width, int height) {
        gameContext.getSceneManager().registerScene(0, () -> new MainMenuScene(0, gameContext));
        gameContext.getSceneManager().registerScene(1, () -> new GameScene(1, width, height, gameContext));
        gameContext.getSceneManager().registerScene(2, () -> new SettingsScene(2, gameContext));
        gameContext.getSceneManager().registerScene(3, () -> new TutorialScene(3, gameContext));
        gameContext.getSceneManager().registerScene(99, () -> new PauseScene(99, gameContext));
    }

    // register all input mappings for the game, mapping keys to events that trigger them
    public static void registerInput(InputManager inputManager) {
        inputManager.mapKey(Input.Keys.W, Event.PlayerUp);
		inputManager.mapKey(Input.Keys.S, Event.PlayerDown);
		inputManager.mapKey(Input.Keys.A, Event.PlayerLeft);
		inputManager.mapKey(Input.Keys.D, Event.PlayerRight);
		inputManager.mapKey(Input.Keys.RIGHT, Event.PlayerRight);
		inputManager.mapKey(Input.Keys.LEFT, Event.PlayerLeft);
        inputManager.mapKey(Input.Keys.E, Event.PlayerInteract);
        inputManager.mapKey(Input.Keys.F, Event.PlayerChop);
        inputManager.mapKey(Input.Keys.ESCAPE, Event.GamePause);
    }

    // register any event observers for the game, such as systems that need to listen to events
    public static void registerEventObservers(GameContext gameContext) {
        gameContext.getEventManager().addObserver(gameContext.getMovementManager());
        gameContext.getEventManager().addObserver(gameContext.getOutputManager());
    }

    // register all entities, factories, scenes, input mappings, and audio for the game
    public static void registerAll(GameContext gameContext, int width, int height) {
        registerScenes(gameContext, width, height);
        registerInput(gameContext.getInputManager());
        registerAudio(gameContext.getOutputManager());
        registerEntities(gameContext);
        registerEventObservers(gameContext);
    }
}
