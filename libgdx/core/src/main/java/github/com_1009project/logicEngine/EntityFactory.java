package github.com_1009project.logicEngine;

import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import github.com_1009project.abstractEngine.Entity;

public class EntityFactory {

    private final AssetManager assetManager;

    public EntityFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    // Creates a new entity of the specified type
    public Entity createEntity(EntityType type) {
        switch (type) {
            // Add cases for other entity types as needed
            case Player:
                Texture idleSheet = assetManager.get("character/human_idle.png", Texture.class);
                Texture runSheet = assetManager.get("character/human_run.png", Texture.class);
                return new Player(200, 200, 50, 50, idleSheet, runSheet);
            default:
                return null;
        }
    }

    // Create an entity using TiledMap MapObject properties, with support for persistent entities
    public Entity createEntity(MapObject object, float mapScale, Map<String, Entity> persistentEntities) {
        String type = object.getProperties().get("type", String.class);
        Entity entity = null;

        // Create collision box for the map object
        RectangleMapObject rectObj = (RectangleMapObject) object;
        Rectangle rect = rectObj.getRectangle();

        float x = rect.x * mapScale;
        float y = rect.y * mapScale;
        float width = rect.width * mapScale;
        float height = rect.height * mapScale;

        // Demo code as most of the map objects do not have classes
        if (type == null){
            type = "CollisionBox"; // default to CollisionBox if no type is specified
        }

        // Check if a persistent entity of this type already exists and reuse it
        if (persistentEntities.containsKey(type)) {
            entity = persistentEntities.get(type);
            entity.setPosition(x, y);
            entity.setSize(width, height);
            entity.setPersistent(true);
            return entity;
        }

        // Create new entity based on type
        switch (type) {
            case "Player":
                Texture idleSheet = assetManager.get("character/human_idle.png", Texture.class);
                Texture runSheet = assetManager.get("character/human_run.png", Texture.class);
                entity = new Player(x, y, width, height, idleSheet, runSheet);
                break;

            case "CollisionBox":
                entity = new CollisionBox(x, y, width, height);
                break;
            // Add cases for other entity types as needed
            case "Counter":
                Texture counterTexture = assetManager.get("foodstations/counter.png", Texture.class);
                entity = new Counter(x, y, width, height, counterTexture);
                break;

            case "ChoppingStation":
                Texture choppingTexture = assetManager.get("foodstations/counter_choppingboard.png", Texture.class);
                entity = new ChoppingStation(x, y, width, height, choppingTexture);
                break;

            case "Stove":
                Texture stoveTexture = assetManager.get("foodstations/stove.png", Texture.class);
                entity = new Stove(x, y, width, height, stoveTexture);
                break;
            
            case "RubbishBin":
                Texture rubbishTexture = assetManager.get("foodstations/rubbishBin.png", Texture.class);
                entity = new RubbishBin(x, y, width, height, rubbishTexture);
                break;

            case "BunBox":
            case "PattyBox":
            case "CheeseBox":
            case "LettuceBox":
            case "TomatoBox":
            case "PlateBox":
                // 1. Extract the ingredient name from the TiledMap type.
                // This turns "BunBox" into "bun", "CheeseBox" into "cheese", etc.
                String ingredientName = type.replace("Box", "").toLowerCase(); 

                // 2. Dynamically build the file path based on the name!
                // Make sure your files are named: bun_box.png, cheese_box.png, etc.
                String texturePath = "foodstations/" + ingredientName + "_box.png";
                
                // 3. Grab the specific texture from your AssetManager
                Texture boxTexture = assetManager.get(texturePath, Texture.class);
                
                // 4. Create the single IngredientBox class, passing in the unique texture and name
                entity = new IngredientBox(x, y, width, height, boxTexture, ingredientName);
                break;

            case "CounterSubmission":
                Texture submissionTexture = assetManager.get("foodstations/counter_submission.png", Texture.class);
                entity = new CounterSubmission(x, y, width, height, submissionTexture);
                break;
            
            default:
                System.out.println("Unknown entity type: " + type);
                break;
        }

        // Mark entity as persistent or non-persistent based on PersistentEntityType configuration
        if (entity != null) {
            entity.setPersistent(isPersistentType(type));
        }

        System.out.println("Created entity of type: " + type);
        return entity;
    }


    // Checks if an entity type is configured as persistent
    private boolean isPersistentType(String type) {
        try {
            PersistentEntityType.valueOf(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}