package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// [waiting for EntityManager implementation cause will cause errors]
public class EntityLayer extends Layer {
    private EntityRegistry entityRegistry;
    private EntityRenderer entityRenderer;
    private EventManager eventManager;
    private SpriteBatch batch;

    public EntityLayer(SpriteBatch batch, EventManager eventManager, EntityRegistry entityRegistry, EntityRenderer entityRenderer) {
        this.batch = batch;
        this.eventManager = eventManager;
        this.entityRegistry = entityRegistry;
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void update(float deltaTime) {
        entityRegistry.update(deltaTime);
    }

    @Override
    public void render() {
        batch.begin();
        entityRenderer.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        entityRegistry.dispose();
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }
    
}
