package github.com_1009project.abstractEngine;

import java.util.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class CollisionManager implements Disposable {
    // spatial hash grid for efficient collision detection
    private SpatialHashGrid grid;

    public CollisionManager(int cellSize) {
        // we can adjust cell size base on our game's needs
        this.grid = new SpatialHashGrid(cellSize);
    }

    // this checks every collidable entity against nearby entities for collisions
    // gamemaster calls this every frame with the list of all entities
    public void updateCollision(List<Entity> entities) {
        // clear and re-insert all collidable entities into the spatial hash grid
        grid.clear();
        for (Entity e : entities) {
            if (e.isCollidable()) { 
                grid.insert(e);
            }
        }

        // for each entity, get its potential colliders from the grid
        for (Entity e1 : entities) {
            if (!e1.isCollidable()) {continue;}

            // get nearby entities that could collide with e1
            Array<Entity> neighbors = grid.getPotentialColliders(e1);

            // check for actual collisions with these neighbors with Rectangle's overlap
            for (Entity e2 : neighbors) {
                if (e1 == e2) {continue;}

                if (e1.getCollisionComponent().getBounds().overlaps(e2.getCollisionComponent().getBounds())) {
                    // if they overlap, trigger their onCollision methods
                    e1.onCollision(e2);
                }
            }
        }
    }

    // dispose data structure
    public void dispose() {
        grid.dispose();
    }
}
