package github.com_1009project.logicEngine.helpers;

import com.badlogic.gdx.Input;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import github.com_1009project.abstractEngine.*;
import github.com_1009project.logicEngine.entities.*;
import github.com_1009project.logicEngine.factories.*;
import github.com_1009project.logicEngine.scenes.*;

public final class GameRegistry {
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

    public static void registerScenes(GameContext gameContext, int width, int height) {
        gameContext.getSceneManager().registerScene(0, () -> new MainMenuScene(0, gameContext));
        gameContext.getSceneManager().registerScene(1, () -> new GameScene(1, width, height, gameContext));
        gameContext.getSceneManager().registerScene(2, () -> new SettingsScene(2, gameContext));
        gameContext.getSceneManager().registerScene(3, () -> new TutorialScene(3, gameContext));
        gameContext.getSceneManager().registerScene(99, () -> new PauseScene(99, gameContext));
    }

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

    public static void registerEventObservers(GameContext gameContext) {
        gameContext.getEventManager().addObserver(gameContext.getMovementManager());
        gameContext.getEventManager().addObserver(gameContext.getOutputManager());
    }

    public static void registerAll(GameContext gameContext, int width, int height) {
        registerScenes(gameContext, width, height);
        registerInput(gameContext.getInputManager());
        registerAudio(gameContext.getOutputManager());
        registerEntities(gameContext);
        registerEventObservers(gameContext);
    }
}
