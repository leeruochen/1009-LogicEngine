package github.com_1009project.abstractEngine;

public class MovementManager implements EventObserver {
    private MovementEntity entityManager;
    
    public MovementManager(MovementEntity entityManager) {
    	this.entityManager = entityManager;
    }
    
	@Override
	public void onNotify(Event event, Boolean up) {
		// Only loop through entities that have explicitly flagged they want input
		for (Entity entity : entityManager.getEntities()) {
			if (entity instanceof DynamicEntity) {
				DynamicEntity dynamicEntity = (DynamicEntity) entity;
				if (dynamicEntity.isInputEnabled() == true && dynamicEntity.canMove() == true) {
					dynamicEntity.getMovementComponent().handlePlayerInput(dynamicEntity, event, up);
				}
			}
		}
	}

	@Override // this is for handling mouse events
	public void onNotify(Event event, Boolean up, int screenX, int screenY) {

	}
}
