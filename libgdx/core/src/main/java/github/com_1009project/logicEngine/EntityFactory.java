package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import github.com_1009project.abstractEngine.Entity;

import com.badlogic.gdx.assets.AssetManager;
import java.util.Map;

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
                return new testEntity(200, 200, 50, 50, assetManager.get("imgs/boy_down_1.png", Texture.class));
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
                entity = new testEntity(x, y, width, height, assetManager.get("imgs/boy_down_1.png", Texture.class));
                break;

            case "Door":
                entity = new Door(x, y, width, height);
                if (object.getProperties().containsKey("targetMap")) {
                    ((Door) entity).setDestination(object.getProperties().get("targetMap", String.class));
                }
                break;

            case "CollisionBox":
                entity = new CollisionBox(x, y, width, height);
                break;
            // Add cases for other entity types as needed
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