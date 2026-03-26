package github.com_1009project.logicEngine.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import github.com_1009project.abstractEngine.*;
import github.com_1009project.logicEngine.entities.*;
import github.com_1009project.logicEngine.factories.*;

public final class GameRegistry {
    public static void registerEntities(EntityRegistry entityRegistry, MapEntityLoader mapEntityLoader, AssetManager assetManager) {
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
}
