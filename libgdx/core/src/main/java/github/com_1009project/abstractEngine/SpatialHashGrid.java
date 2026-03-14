package github.com_1009project.abstractEngine;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import java.util.HashSet;
import java.util.Set;

public class SpatialHashGrid {
    // size of each cell in the grid
    private int cellSize;

    // each cell maps to a list of entities in that cell
    private LongMap<Array<Entity>> buckets;
    
    // array to hold entities that are potential colliders
    private Array<Entity> potentialColliders;

    public SpatialHashGrid(int cellSize) {
        this.cellSize = cellSize;
        this.buckets = new LongMap<>();
        this.potentialColliders = new Array<>();
    }

    // clear all entities from the grid
    public void clear() {
        for (Array<Entity> bucket : buckets.values()) {
            bucket.clear();
        }
    }

    // this inserts a collidable entity into their respective cells/keys
    public void insert(Entity entity) {
        CollisionComponent comp = entity.getCollisionComponent();
        if (comp == null || !entity.isActive()) {return;}

        Rectangle bounds = comp.getBounds();
        
        // calculate which cells this entity touches based on its bounds
        // divide the grids by cell size to make cell coordinates small but accurate
        // if an entity is big, it may touch multiple cells
        // e.g. if cell size is 128px, an entity at (300, 300) with width 100 and height 100
        // would touch cells (2,2) and (3,2) horizontally and (2,2) and (2,3) vertically
        int leftX = (int) (bounds.x / cellSize);
        int leftY = (int) (bounds.y / cellSize);
        int rightX = (int) ((bounds.x + bounds.width) / cellSize);
        int rightY = (int) ((bounds.y + bounds.height) / cellSize);

        // for every cell it touches, add it to that cell's list
        for (int x = leftX; x <= rightX; x++) {
            for (int y = leftY; y <= rightY; y++) {
                // get the unique key for this cell
                long key = getKey(x, y);
                
                // if no list exists for this cell/key, create it
                if (!buckets.containsKey(key)) { 
                    buckets.put(key, new Array<>());
                }
                buckets.get(key).add(entity);
            }
        }
    }

    // this retrieves a list of potential colliders for a given entity
    public Array<Entity> getPotentialColliders(Entity entity) {
        potentialColliders.clear();
        // a set is used to avoid duplicates when an entity spans multiple cells
        Set<Entity> uniqueNeighbors = new HashSet<>();

        Rectangle bounds = entity.getCollisionComponent().getBounds();

        // same logic as insert(): figure out which cells this entity touches
        int leftX = (int) (bounds.x / cellSize);
        int leftY = (int) (bounds.y / cellSize);
        int rightX = (int) ((bounds.x + bounds.width) / cellSize);
        int rightY = (int) ((bounds.y + bounds.height) / cellSize);
        for (int x = leftX; x <= rightX; x++) {
            for (int y = leftY; y <= rightY; y++) {
                // get the unique key for this cell
                long key = getKey(x, y);
                
                // if buckets contains this key, that means there are collidable entities in this cell
                if (buckets.containsKey(key)) {
                    Array<Entity> bucket = buckets.get(key); // get the list of entities in this cell
                    for (Entity neighbor : bucket) { // iterate through each entity in the cell
                        // dont add itself and duplicate entities
                        if (neighbor != entity && !uniqueNeighbors.contains(neighbor)) {
                            uniqueNeighbors.add(neighbor);
                            potentialColliders.add(neighbor);
                        }
                    }
                }
            }
        }
        return new Array<>(potentialColliders);
    }

    private long getKey(int x, int y) {
        // shifts x to the high 32 bits and y to the low 32 bits to create a unique long key
        // mask 0xffffffffL gives us 32 0's followed by 32 1's to ensure y occupies only the lower 32 bits
        // then combine them with or
        return (((long)x) << 32) | (y & 0xffffffffL);
    }

    public void dispose() {
        // clear all buckets to free memory
        for (Array<Entity> bucket : buckets.values()) {
            bucket.clear();
        }
        buckets.clear();
        potentialColliders.clear();
    }
}