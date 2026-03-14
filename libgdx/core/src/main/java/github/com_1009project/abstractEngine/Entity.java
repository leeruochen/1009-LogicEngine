package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity implements Moveable {
    // properties fields to be used by subclasses
    private static int idCounter = 0;
    private int id; 
    private boolean active;
    private Vector2 position;
    private Vector2 size;
    private float rotation; 
    private Vector2 previousPosition;
    private CollisionComponent collisionComponent;
    private boolean isPersistent;
    private boolean inputEnabled;
    private boolean canMove;
    private boolean onGround;
    private MovementComponent movementComponent;

    public Entity() { // constructor to initialize the entity with defaults
        this.id = idCounter++; // id's are sequentially assigned
        this.position = new Vector2(0, 0);
        this.size = new Vector2(1, 1); 
        this.rotation = 0;
        this.previousPosition = new Vector2(0, 0);
        this.active = true; 
        this.inputEnabled = false;
        this.onGround = false;
        this.canMove = false;
        this.collisionComponent = null;
        this.movementComponent = null;
    }

    // to render the entity
    public abstract void render(SpriteBatch batch);
    // to update the entity's movement
    public void updateMovement(float delta) { //from moveable interface
    	//let entities override this method, they should define their own unique movement
    }

    // this will be called every frame to update the entity
    public void update(float deltaTime){
        this.previousPosition.set(this.position); // Store previous position
        updateMovement(deltaTime); // Update the entity's movement
        
        // if entity is collidable, update its bounds
        if (this.collisionComponent != null) {
            this.collisionComponent.updateBounds(this.position);
        }
    }

    protected void createCollisionComponent(float width, float height) { 
        // collision component creation
        // entities can call this in their constructor to make it collidable
        // protected so that only subclasses can create collision components
        this.collisionComponent = new CollisionComponent(position.x, position.y, width, height);
    }

    protected void createCollisionComponent(float width, float height, float offsetX, float offsetY) {
        // overloaded method to create collision component with offsets suitable for "Players"
        this.collisionComponent = new CollisionComponent(position.x, position.y, width, height, offsetX, offsetY);
    }

    public void setCollisionActive(boolean active) {
        // Enable or disable collision detection for this entity
        if (this.collisionComponent != null) {
            this.collisionComponent.setActive(active);
        }
    }

    public boolean isCollidable() { 
        // check if entity is collidable and active
        return this.collisionComponent != null && this.collisionComponent.isActive();
    }

    public void onCollision(Entity collidedEntity){} // classes to override this if they want to handle collision

    // Getters and Setters
    public CollisionComponent getCollisionComponent() {return this.collisionComponent;}
    public void setPosition(float x, float y) {this.position.set(x, y);}
    public Vector2 getPosition() { return position;}
    public void setSize(float width, float height) {this.size.set(width, height);}
    public Vector2 getSize() { return size;}
    public void setRotation(float rotation) {this.rotation = rotation;}
    public float getRotation() {return rotation;}
    public int getId() {return id;}
    public Vector2 getPreviousPosition() {return previousPosition;}
    public boolean isActive() { return active;}
    public void setActive(boolean active) { this.active = active;}
    public boolean getPersistent() {return isPersistent;}
    public void setPersistent(boolean persistent) {this.isPersistent = persistent;}
    public boolean isInputEnabled() {return inputEnabled;}
    public void setInputEnabled(boolean inputEnabled) {this.inputEnabled = inputEnabled;}
    public boolean isOnGround() {return onGround;}
    public void setOnGround(boolean onGround) {this.onGround = onGround;}
    public boolean isCanMove() {return canMove;}
    public void setCanMove(boolean canMove) {this.canMove = canMove;}
    public MovementComponent getMovementComponent() {return this.movementComponent;}
    public void setMovementComponent(float maxSpeed, float friction) {
    	if (this.canMove == true) { 
    		this.movementComponent = new MovementComponent(maxSpeed, friction);
    	}
    }

    public void onDestroy(){} // method to be called when the entity is destroyed, can be overridden by subclasses for cleanup
    


    public Entity copy(){return this;}; // method for creating a copy of the entity
}
