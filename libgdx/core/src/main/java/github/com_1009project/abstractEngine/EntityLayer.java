package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// [waiting for EntityManager implementation cause will cause errors]
public class EntityLayer extends Layer {
    private EntityManager entityManager;
    private EventManager eventManager;
    private SpriteBatch batch;

    public EntityLayer(SpriteBatch batch, EventManager eventManager, EntityManager entityManager) {
        this.batch = batch;
        this.eventManager = eventManager;
        this.entityManager = entityManager;
        // entityManager.setEventManager(eventManager);
    }

    @Override
    public void update(float deltaTime) {
        entityManager.update(deltaTime);
    }

    @Override
    public void render() {
        batch.begin();
        entityManager.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        entityManager.dispose();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
}
