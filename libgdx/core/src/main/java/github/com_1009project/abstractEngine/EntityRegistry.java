package github.com_1009project.abstractEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRegistry implements MovementEntity {

    private final List<Entity> entities = new ArrayList<>(); // master list

    private final List<IUpdatable> updatableEntities = new ArrayList<>(); // entities that need to be updated every frame
    private final List<IRenderable> renderableEntities = new ArrayList<>(); // entities that need to be rendered every frame
    private final List<Entity> collidableEntities = new ArrayList<>(); // entities that have collision components

    private final List<Entity> toRemove = new ArrayList<>();
    private final Map<String, Entity> persistentEntities = new HashMap<>();
    private final Map<Class<? extends Entity>, Ifactory<? extends Entity>> factories = new HashMap<>();

    public EntityRegistry() {
    }

    // ── Factory registry ────────────────────────────────────────────────────

    public <T extends Entity> void registerFactory(Class<T> type, Ifactory<T> factory) {
        factories.put(type, factory);
    }

    public Ifactory<? extends Entity> getFactory(Class<? extends Entity> type) {
        return factories.get(type);
    }

    // Creates a new entity of the specified type, adds it to the registry, and returns it
    @SuppressWarnings("unchecked")
    public <T extends Entity> T createEntity(Class<T> type, float x, float y, float width, float height) {
        Ifactory<?> factory = factories.get(type);
        Entity entity = factory.createEntity(x, y, width, height);
        add(entity);
        return (T) type.cast(entity);
    }

    // ── Entity lifecycle ────────────────────────────────────────────────────

    public void add(Entity entity) {
        if (entity == null) { return; }

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

    // Removes all marked entities and calls their onDestroy methods
    public void processRemovals() {
        for (int i = 0; i < toRemove.size(); i++) {
            Entity entity = toRemove.get(i);
            entity.onDestroy(); // Call onDestroy for cleanup
            System.out.println("Removing entity: " + entity);
            entities.remove(entity);
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
    public void update(float deltaTime) {
        for (IUpdatable entity : updatableEntities) {
            entity.update(deltaTime);
        }
        processRemovals();
    }

    // ── Read-only accessors ─────────────────────────────────────────────────

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public List<IRenderable> getRenderableEntities() {
        return renderableEntities;          // mutable access for EntityRenderer's sort
    }

    public List<Entity> getCollidableEntities() {
        return Collections.unmodifiableList(collidableEntities);
    }

    public Map<String, Entity> getPersistentEntities() {
        return Collections.unmodifiableMap(persistentEntities);
    }

    // ── Disposal ────────────────────────────────────────────────────────────

    // Disposes of all non-persistent entities and clears the manager
    public void dispose() {
        for (Entity entity : entities) {
            if (!entity.getPersistent()) {
                markForRemoval(entity);
            }
        }
        processRemovals();
    }

    // Dispose and clear all entities, including persistent ones
    public void disposeAll() {
        entities.clear();
        updatableEntities.clear();
        renderableEntities.clear();
        collidableEntities.clear();
        persistentEntities.clear();
        toRemove.clear();
    }
}
