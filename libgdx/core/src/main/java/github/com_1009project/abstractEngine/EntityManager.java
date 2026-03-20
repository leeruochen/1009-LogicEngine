package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

import github.com_1009project.logicEngine.EntityFactory;
import github.com_1009project.logicEngine.EntityType;

public class EntityManager implements MovementEntity, MapEntity{

    private final List<Entity> entities = new ArrayList<>(); // master list

    private final List<IUpdatable> updatableEntities = new ArrayList<>(); // entities that need to be updated every frame
    private final List<IRenderable> renderableEntities = new ArrayList<>(); // entities that need to be rendered every frame
    private final List<Entity> collidableEntities = new ArrayList<>(); // entities that have collision components and need to be checked for collisions every frame

    private final List<Entity> toRemove = new ArrayList<>();
    private final Map<String, Entity> persistentEntities = new HashMap<>();
    private final EntityFactory factory;

    public EntityManager(AssetManager assetManager) {
        this.factory = new EntityFactory(assetManager);
    }

    // Creates a new entity of the specified type, adds it to the manager, and returns it
    public Entity createEntity(EntityType type) {
        Entity entity = factory.createEntity(type);

        categorizeEntity(entity);
        return entity;
    }

    // Creates an entity from a TiledMap MapObject, adds it to the manager, and returns it
    public Entity createEntity(MapObject object, float map_scale) {
        Entity entity = factory.createEntity(object, map_scale, persistentEntities);

        if (entity != null && !entities.contains(entity)) {
            categorizeEntity(entity);
            // Track persistent entities by their type for reuse on map load
            if (entity.getPersistent()) {
                String type = object.getProperties().get("type", String.class);
                if (type != null) {
                    persistentEntities.put(type, entity);
                }
            }
        }
        return entity;
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
}