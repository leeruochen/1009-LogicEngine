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
    private boolean spacePressed = false; //jump logic is in game logic

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
    
    protected void handlePlayerInput(Entity entity, Event event, boolean isUp) {
        if (entity == null) {
        	return;
        }
        // Update input state
        if (!isUp) { // Key Pressed
            switch (event) {
                case PlayerLeft: 
                	leftPressed = true; 
                	break;
                case PlayerRight: 
                	rightPressed = true; 
                	break;
                case PlayerUp: 
                	upPressed = true; 
                	break;
                case PlayerDown: 
                	downPressed = true; 
                	break;
                case PlayerJump: 
                	spacePressed = true;
                	break;
            }
        } else { // Key Released
            switch (event) {
                case PlayerLeft: 
                	leftPressed = false; 
                	break;
                case PlayerRight: 
                	rightPressed = false; 
                	break;
                case PlayerUp: 
                	upPressed = false; 
                	break;
                case PlayerDown: 
                	downPressed = false; 
                	break;
                case PlayerJump: 
                	spacePressed = false;
                	break;
            }
        }

        // Update velocity based on current input state
        updatePlayerVelocity(entity);
    }

    private void updatePlayerVelocity(Entity entity) {
    	//the input events is faster than testEntity to run its constructor finish, preventing crash
    	if (entity.getMovementComponent() == null) {
    		return;
    	}
        Vector2 vel = entity.getMovementComponent().getVelocity();
        
        // Horizontal movement
        if (leftPressed && !rightPressed) {
            vel.x = -playerSpeed;
        } else if (rightPressed && !leftPressed) {
            vel.x = playerSpeed;
        } else {
            vel.x = 0; // Both or neither pressed
        }
        
        // Vertical movement
        if (upPressed && !downPressed) {
            vel.y = playerSpeed;
        } else if (downPressed && !upPressed) {
            vel.y = -playerSpeed;
        } else {
            vel.y = 0; // Both or neither pressed
        }
        
    }
    
}
