package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import github.com_1009project.logicEngine.EntityFactory;
import github.com_1009project.logicEngine.EntityType;
import github.com_1009project.logicEngine.entities.IngredientBox;
import github.com_1009project.logicEngine.factories.Ifactory;

public class EntityManager implements MovementEntity, MapEntity{

    private final List<Entity> entities = new ArrayList<>(); // master list

    private final List<IUpdatable> updatableEntities = new ArrayList<>(); // entities that need to be updated every frame
    private final List<IRenderable> renderableEntities = new ArrayList<>(); // entities that need to be rendered every frame
    private final List<Entity> collidableEntities = new ArrayList<>(); // entities that have collision components and need to be checked for collisions every frame

    private final List<Entity> toRemove = new ArrayList<>();
    private final Map<String, Entity> persistentEntities = new HashMap<>();
    private final Map<Class<? extends Entity>, Ifactory<? extends Entity>> factories = new HashMap<>();
    private final AssetManager assetManager;
    // private final EntityFactory factory;

    public EntityManager(AssetManager assetManager) {
        // this.factory = new EntityFactory(assetManager);
        this.assetManager = assetManager;
    }

    public <T extends Entity> void registerFactory(Class<T> type, Ifactory<T> factory) {
        factories.put(type, factory);
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

        try {

            // Checks if the type is one of the food boxes
            List<String> foodBox = Arrays.asList("BunBox", "PattyBox", "LettuceBox", "TomatoBox", "CheeseBox", "MeatBox");

            Class<?> rawClass;

            if (foodBox.contains(typeName)) {


                rawClass = Class.forName("github.com_1009project.logicEngine.entities.IngredientBox");
            } else {
                rawClass = Class.forName("github.com_1009project.logicEngine.entities." + typeName);
            }

            if (!Entity.class.isAssignableFrom(rawClass)) {
                throw new ClassCastException(typeName + " does not extend Entity");
            }

            // Safely cast to Class<? extends Entity> for the map lookup
            Class<? extends Entity> entityClass = rawClass.asSubclass(Entity.class);

            RectangleMapObject rectObj = (RectangleMapObject) object;
            Rectangle rect = rectObj.getRectangle();

            float x = rect.x * map_scale;
            float y = rect.y * map_scale;
            float width = rect.width * map_scale;
            float height = rect.height * map_scale;

            Ifactory<?> factory = factories.get(entityClass);
            if (factory != null) {
                Entity newEntity;
                // If the type is a food box, use alternative creation logic to assign the correct texture and name
                if (foodBox.contains(typeName)){

                    String ingredientName = typeName.replace("Box", "").toLowerCase(); 
                    String texturePath = "foodstations/" + ingredientName + "_box.png";
                    Texture boxTexture = assetManager.get(texturePath, Texture.class);
                    newEntity = factory.createEntity(x, y, width, height, boxTexture, ingredientName);
                } else {
                    newEntity = factory.createEntity(x, y, width, height);
                }
                // 6. Categorize the entity before returning
                categorizeEntity(newEntity);

                // 7. Cast the entity to the specific type T (the return type)
                // We use (T) because the method signature uses <T extends Entity>
                return (T) entityClass.cast(newEntity);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Could not find class for type: " + typeName);
        } catch (ClassCastException e) {
            System.err.println("Error: Type " + typeName + " is not a valid Entity.");
        }
        return null;


        // Entity entity = factory.createEntity(object, map_scale, persistentEntities);

        // if (entity != null && !entities.contains(entity)) {
        //     categorizeEntity(entity);
        //     // Track persistent entities by their type for reuse on map load
        //     if (entity.getPersistent()) {
        //         if (type != null) {
        //             persistentEntities.put(type, entity);
        //         }
        //     }
        // }
        // return entity;
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