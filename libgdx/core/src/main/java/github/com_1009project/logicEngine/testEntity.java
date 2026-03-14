package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import github.com_1009project.abstractEngine.Entity;

public class testEntity extends Entity {
    public boolean hasCollided = false; // Flag for camera shake
    private Texture texture;
    public String mapToLoad = null;
    public Door nearbyDoor = null;

    public testEntity(float x, float y, float w, float h, Texture texture) {
        super();
        this.setPosition(x, y);
        this.setSize(w, h);
        this.createCollisionComponent(44, 10, 10, 0);
        this.texture = texture;
        this.setOnGround(true);
        this.setCanMove(true);
        this.setInputEnabled(true);
        this.setMovementComponent(1000f, 0.85f);
        this.setPersistent(true);
    }

    @Override
    public void updateMovement(float deltaTime) {
        nearbyDoor = null; // reset each frame; onCollision will set it if overlapping
        Vector2 vel = this.getMovementComponent().getVelocity();
        float playerMaxSpd = this.getMovementComponent().getMaxSpeed();
        
        //diagonal movement
        if (vel.len() > playerMaxSpd) {
            vel.setLength(playerMaxSpd);
        }
        
        // Update position
        this.getPosition().add(vel.x * deltaTime, vel.y * deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }

    @Override
    public void onCollision(Entity other) {
        Rectangle playerRect = this.getCollisionComponent().getBounds();
        Rectangle otherRect = other.getCollisionComponent().getBounds();

        if (playerRect.overlaps(otherRect)) {
            if (other instanceof Door) {
                nearbyDoor = (Door) other;
            }
            else if (other instanceof CollisionBox) {
                this.setPosition(this.getPreviousPosition().x, this.getPreviousPosition().y);
                this.hasCollided = true;
            }
        }
        this.getCollisionComponent().updateBounds(getPosition());
    }
}
