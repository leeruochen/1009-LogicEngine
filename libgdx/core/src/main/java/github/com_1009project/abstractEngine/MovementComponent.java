package github.com_1009project.abstractEngine;

import com.badlogic.gdx.math.Vector2;

// Component that holds all movement-related attributes for each moveable entity
// in future will create another class for movement types - player, enemy...
public class MovementComponent {
	private final float playerSpeed = 300f;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float maxSpeed;
    private float friction;
    
    // Track which keys are currently pressed
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    //entity should not be moving when created, when input events are triggered then movementmanager handles it
    public MovementComponent(float maxSpeed, float friction) { 
    	this.velocity = new Vector2(0, 0); 
        this.acceleration = new Vector2(0, 0);
        this.maxSpeed = maxSpeed;
        this.friction = friction;
    }
    
    // Velocity
    public Vector2 getVelocity() {
        return velocity;
    }
    
    public void setVelocity(Vector2 vel) {
        velocity.set(vel);
    }
    
    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }
    
    // Acceleration
    public Vector2 getAcceleration() {
        return acceleration;
    }
    
    public void setAcceleration(Vector2 accel) {
        acceleration.set(accel);
    }
    
    public void setAcceleration(float x, float y) {
        acceleration.set(x, y);
    }
    
    // Max Speed
    public float getMaxSpeed() {
        return maxSpeed;
    }
    
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    // Friction
    public float getFriction() {
        return friction;
    }
    
    public void setFriction(float friction) {
        this.friction = friction;
    }
    
    public boolean isLeftPressed()      {
    	return leftPressed; 
    }
    
    public void setLeftPressed(boolean pressed) {
    	leftPressed = pressed; 
    }
    public boolean isRightPressed()     { 
    	return rightPressed; 
    }
    public void setRightPressed(boolean pressed) { 
    	rightPressed = pressed; 
    }
    public boolean isUpPressed()        { 
    	return upPressed; 
    }
    public void setUpPressed(boolean pressed)    { 
    	upPressed = pressed; 
    }
    public boolean isDownPressed()      { 
    	return downPressed; 
    }
    public void setDownPressed(boolean pressed)  { 
    	downPressed = pressed; 
    }
    
    public float getPlayerSpeed() {
    	return playerSpeed;
    }
}
