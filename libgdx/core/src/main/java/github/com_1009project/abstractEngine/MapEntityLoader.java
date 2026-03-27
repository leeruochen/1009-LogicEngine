package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

// The MapEntityLoader class is responsible for creating entities from map objects defined in a TiledMap.

public class MapEntityLoader implements MapEntity {

    private final EntityRegistry registry;
    private final Map<String, Ifactory<? extends Entity>> typeAliases = new HashMap<>();

    public MapEntityLoader(EntityRegistry registry) {
        this.registry = registry;
    }

    /**
     * Registers a type-name alias that maps a TiledMap type string to a factory.
     * This allows the engine to create entities from map data without knowing
     * concrete entity class names.
     */
    public void registerTypeAlias(String typeName, Ifactory<? extends Entity> factory) {
        typeAliases.put(typeName, factory);
    }

    // Creates an entity from a TiledMap MapObject, adds it to the registry, and returns it
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T createEntity(MapObject object, float map_scale) {

        String typeName = object.getProperties().get("type", String.class);

        Ifactory<?> factory = typeAliases.get(typeName);
        if (factory == null) {
            System.err.println("Error: No factory registered for type alias: " + typeName);
            return null;
        }

        RectangleMapObject rectObj = (RectangleMapObject) object;
        Rectangle rect = rectObj.getRectangle();

        float x = rect.x * map_scale;
        float y = rect.y * map_scale;
        float width = rect.width * map_scale;
        float height = rect.height * map_scale;

        Entity newEntity = factory.createEntity(x, y, width, height);
        registry.add(newEntity);

        T result = (T) newEntity;
        return result;
    }
}
