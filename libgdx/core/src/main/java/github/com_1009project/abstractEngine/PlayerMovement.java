package github.com_1009project.abstractEngine;

import com.badlogic.gdx.math.Vector2;

public class PlayerMovement {
	
    protected void handlePlayerInput(DynamicEntity entity, Event event, boolean isUp) {
        MovementComponent movementComponent = entity.getMovementComponent();
        if (movementComponent == null) {
        	return;
        }
        // Update input state
        if (!isUp) { // Key Pressed
            switch (event) {
                case PlayerLeft: 
                	movementComponent.setLeftPressed(true);
                	break;
                case PlayerRight: 
                	movementComponent.setRightPressed(true);
                	break;
                case PlayerUp: 
                	movementComponent.setUpPressed(true);
                	break;
                case PlayerDown: 
                	movementComponent.setDownPressed(true);
                	break;
            }
        } else { // Key Released
            switch (event) {
            	case PlayerLeft: 
            		movementComponent.setLeftPressed(false);
            		break;
            	case PlayerRight: 
            		movementComponent.setRightPressed(false);
            		break;
            	case PlayerUp: 
            		movementComponent.setUpPressed(false);
            		break;
            	case PlayerDown: 
            		movementComponent.setDownPressed(false);
            		break;
            }
        }

        // Update velocity based on current input state
        updatePlayerVelocity(entity, movementComponent);
    }

    private void updatePlayerVelocity(DynamicEntity entity, MovementComponent movementComponent) {
    	//the input events is faster than testEntity to run its constructor finish, preventing crash
    	if (entity.getMovementComponent() == null) {
    		return;
    	}
        Vector2 vel = entity.getMovementComponent().getVelocity();
        
        // Horizontal movement
        if (movementComponent.isLeftPressed() && !movementComponent.isRightPressed()) {
            vel.x = -movementComponent.getPlayerSpeed();
        } else if (movementComponent.isRightPressed() && !movementComponent.isLeftPressed()) {
            vel.x = movementComponent.getPlayerSpeed();
        } else {
            vel.x = 0; // Both or neither pressed
        }
        
        // Vertical movement
        if (movementComponent.isUpPressed() && !movementComponent.isDownPressed()) {
            vel.y = movementComponent.getPlayerSpeed();
        } else if (movementComponent.isDownPressed() && !movementComponent.isUpPressed()) {
            vel.y = -movementComponent.getPlayerSpeed();
        } else {
            vel.y = 0; // Both or neither pressed
        }
        
    }
}
