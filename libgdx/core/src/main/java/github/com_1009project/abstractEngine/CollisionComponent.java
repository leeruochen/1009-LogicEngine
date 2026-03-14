package github.com_1009project.abstractEngine;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionComponent {
    // bounding rectangle for collision detection
    private Rectangle bounds;
    private boolean active = true;

    // offset values to adjust the position of the collision bounds relative to the entity's position
    private float offsetX;
    private float offsetY;

    // creates a collision bounds that will follow the entity
    public CollisionComponent(float x, float y, float width, float height) {
        this(x, y, width, height, 0, 0);
    }

    // creates a collision bounds with specified offsets for more precise or smaller collision detection
    public CollisionComponent(float x, float y, float width, float height, float offsetX, float offsetY) {
        this.bounds = new Rectangle(x + offsetX, y + offsetY, width, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void updateBounds(Vector2 position) { 
        // update the bounding rectangle based on the entity's position and size
        this.bounds.setPosition(position.x + this.offsetX, position.y + this.offsetY);
    }

    public Rectangle getBounds() { 
        // getter for the bounding rectangle
        return new Rectangle(this.bounds);
    }

    public void setActive(boolean active) { 
        // choose to activate or deactivate collision detection for this entity
        this.active = active;
    }

    public boolean isActive() { 
        // getter for the active state
        return active;
    }
}
