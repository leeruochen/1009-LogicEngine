package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public class EntityManager implements MovementEntity, MapEntity{

    private final List<Entity> entities = new ArrayList<>(); // master list

    private final List<IUpdatable> updatableEntities = new ArrayList<>(); // entities that need to be updated every frame
    private final List<IRenderable> renderableEntities = new ArrayList<>(); // entities that need to be rendered every frame
    private final List<Entity> collidableEntities = new ArrayList<>(); // entities that have collision components and need to be checked for collisions every frame

    private final List<Entity> toRemove = new ArrayList<>();
    private final Map<String, Entity> persistentEntities = new HashMap<>();
    private final Map<Class<? extends Entity>, Ifactory<? extends Entity>> factories = new HashMap<>();
    private final Map<String, Ifactory<? extends Entity>> typeAliases = new HashMap<>();
    private final AssetManager assetManager;
    // private final EntityFactory factory;

    public EntityManager(AssetManager assetManager) {
        // this.factory = new EntityFactory(assetManager);
        this.assetManager = assetManager;
    }

    public <T extends Entity> void registerFactory(Class<T> type, Ifactory<T> factory) {
        factories.put(type, factory);
    }

    /**
     * Registers a type-name alias that maps a TiledMap type string to a factory.
     * This allows the engine to create entities from map data without knowing
     * concrete entity class names.
     */
    public void registerTypeAlias(String typeName, Ifactory<? extends Entity> factory) {
        typeAliases.put(typeName, factory);
    }

    // Creates a new entity of the specified type, adds it to the manager, and returns it
    public <T extends Entity> T createEntity(Class<T> type, float x, float y, float width, float height) {
        
        Ifactory<?> factory = factories.get(type);
        Entity entity = factory.createEntity(x,y,width,height);

        categorizeEntity(entity);
        return (T) type.cast(entity);
    }

    // Creates an entity from a TiledMap MapObject, adds it to the manager, and returns it
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
                categorizeEntity(newEntity);

        @SuppressWarnings("unchecked")
        T result = (T) newEntity;
        return result;
    }

    private void categorizeEntity(Entity entity) {
        if (entity == null) {return;}

        entities.add(entity);

        if (entity instanceof IUpdatable) {
            updatableEntities.add((IUpdatable) entity);
        }
        if (entity instanceof IRenderable) {
            renderableEntities.add((IRenderable) entity);
        }
        if (entity.isCollidable()) {
            collidableEntities.add(entity);
        }
    }

    // Marks an entity for removal at the end of the current update cycle
    public void markForRemoval(Entity entity) {
        if (!toRemove.contains(entity)) {
            toRemove.add(entity);
        }
    }

    // Immediately removes an entity from the manager and calls its onDestroy method
    public void processRemovals() {
        for (Entity entity : toRemove) {
            entity.onDestroy(); // Call onDestroy for cleanup
            System.out.println("Removing entity: " + entity);
            entities.remove(entity);
            // Remove from categorized lists
            if (entity instanceof IUpdatable) {
                updatableEntities.remove(entity);
            }
            if (entity instanceof IRenderable) {
                renderableEntities.remove(entity);
            }
            if (entity.isCollidable()) {
                collidableEntities.remove(entity);
            }
        }
        toRemove.clear();
    }

    // Updates all active entities and processes removals at the end of the update cycle
    public void update(float deltaTime){

        // Update all active entities
        for (IUpdatable entity : updatableEntities) {
            entity.update(deltaTime);
        }
        processRemovals(); // Process removals after updates
    }

    // Renders all active entities using the provided SpriteBatch
    public void render(SpriteBatch batch) {
        renderableEntities.sort((r1, r2) -> Float.compare(r2.getY(), r1.getY())); // sort all renderable entities by their Y position for correct rendering order

		for (IRenderable entity : renderableEntities) {
			if (((Entity) entity).isActive()) {
				entity.render(batch);
			}
		}
	}

    // getter for entities
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public List<Entity> getCollidableEntities() {
        return Collections.unmodifiableList(collidableEntities);
    }

    // getter for persistent entities map
    public Map<String, Entity> getPersistentEntities() {
        return Collections.unmodifiableMap(persistentEntities);
    }

    // Disposes of all non-persistent entities and clears the manager
    public void dispose() {
        for (Entity entity : entities) {
            if (!entity.getPersistent()) {
                markForRemoval(entity);
            }
        }
        processRemovals();
    }
    // use to dispose and clear all entities, including persistent ones, for example when exiting to the main menu
    public void disposeAll() {
        entities.clear();
        updatableEntities.clear();
        renderableEntities.clear();
        collidableEntities.clear();
        persistentEntities.clear();
        toRemove.clear();
    }
}