package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    // properties fields to be used by subclasses
    private static int idCounter = 0;
    private int id; 
    private boolean active;
    private Vector2 position;
    private Vector2 size;
    private float rotation; 
    private CollisionComponent collisionComponent;
    private AnimationComponent animationComponent;
    private boolean isPersistent;
    private boolean inputEnabled;


    public Entity() { // constructor to initialize the entity with defaults
        this.id = idCounter++; // id's are sequentially assigned
        this.position = new Vector2(0, 0);
        this.size = new Vector2(1, 1); 
        this.rotation = 0;
        this.active = true; 
        this.inputEnabled = false;
        this.collisionComponent = null;
        this.animationComponent = null;
        this.isPersistent = false;
    }

    // collision component creation
    // entities can call this in their constructor to make it collidable
    protected void createCollisionComponent(float width, float height) { this.collisionComponent = new CollisionComponent(position.x, position.y, width, height);}
    // overloaded method to create collision component with offsets suitable for "Players"
    protected void createCollisionComponent(float width, float height, float offsetX, float offsetY) {this.collisionComponent = new CollisionComponent(position.x, position.y, width, height, offsetX, offsetY);}

    public void setCollisionActive(boolean active) {
        // Enable or disable collision detection for this entity
        if (this.collisionComponent != null) {
            this.collisionComponent.setActive(active);
        }
    }

    public void createAnimationComponent(String initialState) { this.animationComponent = new AnimationComponent(initialState);}
    public boolean hasAnimation() { return this.animationComponent != null;}

    // check if entity is collidable and active
    public boolean isCollidable() { return this.collisionComponent != null && this.collisionComponent.isActive();}

    // Getters and Setters
    public CollisionComponent getCollisionComponent() {return this.collisionComponent;}
    public AnimationComponent getAnimationComponent() {return this.animationComponent;}
    public void setPosition(float x, float y) {this.position.set(x, y);}
    public Vector2 getPosition() { return position;}
    public void setSize(float width, float height) {this.size.set(width, height);}
    public Vector2 getSize() { return size;}
    public void setRotation(float rotation) {this.rotation = rotation;}
    public float getRotation() {return rotation;}
    public int getId() {return id;}
    public boolean isActive() { return active;}
    public void setActive(boolean active) { this.active = active;}
    public boolean getPersistent() {return isPersistent;}
    public void setPersistent(boolean persistent) {this.isPersistent = persistent;}
    public boolean isInputEnabled() {return inputEnabled;}
    public void setInputEnabled(boolean inputEnabled) {this.inputEnabled = inputEnabled;}

    public void onDestroy(){} // method to be called when the entity is destroyed, can be overridden by subclasses for cleanup

    public float getY() { return position.y;}

    public Entity copy(){return this;}; // method for creating a copy of the entity
}
