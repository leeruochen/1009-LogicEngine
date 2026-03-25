package github.com_1009project.abstractEngine;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
