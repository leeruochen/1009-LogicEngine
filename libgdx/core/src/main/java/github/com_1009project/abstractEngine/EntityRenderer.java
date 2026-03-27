package github.com_1009project.abstractEngine;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// The EntityRenderer class is responsible for rendering all active renderable entities in the game. 
// It retrieves the list of renderable entities from the EntityRegistry, sorts them by their Y position to ensure correct rendering order (entities with higher Y values are rendered behind those with lower Y values), and calls their render method using the provided SpriteBatch.

public class EntityRenderer {

    private final EntityRegistry registry;

    public EntityRenderer(EntityRegistry registry) {
        this.registry = registry;
    }

    // Renders all active renderable entities using the provided SpriteBatch, sorted by Y position
    public void render(SpriteBatch batch) {
        List<IRenderable> renderableEntities = registry.getRenderableEntities();

        renderableEntities.sort((r1, r2) -> Float.compare(r2.getY(), r1.getY()));

        for (IRenderable entity : renderableEntities) {
            if (((Entity) entity).isActive()) {
                entity.render(batch);
            }
        }
    }
}
