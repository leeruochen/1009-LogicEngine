package github.com_1009project.abstractEngine;

import com.badlogic.gdx.math.Vector2;

public abstract class DynamicEntity extends Entity implements IUpdatable, Moveable {

    private Vector2 previousPosition;
    private boolean canMove;
    private boolean onGround;
    private MovementComponent movementComponent;

    public DynamicEntity() {
        super();
        this.previousPosition = new Vector2();
        this.canMove = false;
        this.movementComponent = null;
    }

    @Override
    public void update(float deltaTime){
        if (!this.isActive()) {
            return; // Skip update if entity is not active
        }
        
        this.previousPosition.set(this.getPosition()); // Store previous position
        updateMovement(deltaTime); // Update the entity's movement
        
        // if entity is collidable, update its bounds
        if (this.getCollisionComponent() != null) {
            this.getCollisionComponent().updateBounds(this.getPosition());
        }
    }

    @Override
    public abstract void updateMovement(float deltaTime);

    // Getters and Setters for movement-related properties
    public Vector2 getPreviousPosition() { return previousPosition; }
    public boolean canMove() { return canMove; }
    public void setCanMove(boolean canMove) { this.canMove = canMove; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public MovementComponent getMovementComponent() { return movementComponent; }
    public void setMovementComponent(float maxSpeed, float friction) {
        if (this.canMove) {
            this.movementComponent = new MovementComponent(maxSpeed, friction);
        } else {
            this.movementComponent = null; // If entity can't move, ensure movement component is null
        }
    }
}
