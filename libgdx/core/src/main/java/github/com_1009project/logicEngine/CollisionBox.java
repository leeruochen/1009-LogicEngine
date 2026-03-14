package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.Entity;

// simple collision box entity
public class CollisionBox extends Entity {
    public CollisionBox(float x, float y, float w, float h) {
        super();
        this.setPosition(x, y);
        this.setSize(w, h);
        this.createCollisionComponent(w, h);
    }

    // collision boxes are invisible and have no behavior, they just exist to be collided with
    @Override
    public void updateMovement(float deltaTime) {}

    // no rendering for collision boxes, they are invisible
    @Override
    public void render(SpriteBatch batch) {}
    
    // no onCollision behavior, classes will implement their own collision logic if touching this box
    @Override
    public void onCollision(Entity other) {}
}