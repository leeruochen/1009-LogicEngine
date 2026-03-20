package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

import github.com_1009project.abstractEngine.DynamicEntity;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.abstractEngine.IRenderable;
import github.com_1009project.abstractEngine.AnimationComponent;

public class Player extends DynamicEntity implements ICollidable, IRenderable {
    public boolean hasCollided = false;
    private AnimationComponent animationComponent;

    public Player(float x, float y, float w, float h, Texture idleSheet, Texture runSheet) {
        super();
        this.setPosition(x, y);
        this.setSize(w, h);
        this.createCollisionComponent(60, 10, 20, 10);
        this.setOnGround(true);
        this.setCanMove(true);
        this.setInputEnabled(true);
        this.setMovementComponent(1000f, 0.85f);
        this.setPersistent(true);
        this.animationComponent = new AnimationComponent();
        this.animationComponent.addAnimation("IDLE", idleSheet, 4, 1, 0.3f);
        this.animationComponent.addAnimation("RUN", runSheet, 8, 1, 0.05f);
    	this.animationComponent.setState("IDLE");
    }

    @Override
    public void updateMovement(float deltaTime) {
        Vector2 vel = this.getMovementComponent().getVelocity();
        float playerMaxSpd = this.getMovementComponent().getMaxSpeed();
        
        //diagonal movement
        if (vel.len() > playerMaxSpd) {
            vel.setLength(playerMaxSpd);
        }
        
        // Update position
        this.getPosition().add(vel.x * deltaTime, vel.y * deltaTime);

        if (vel.x != 0 || vel.y != 0) {
            this.animationComponent.setState("RUN");
        } else {
            this.animationComponent.setState("IDLE");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = this.animationComponent.getCurrentFrame(Gdx.graphics.getDeltaTime(), true);
        batch.draw(currentFrame, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }

    @Override
    public void onCollision(Entity other) {
        Rectangle playerRect = this.getCollisionComponent().getBounds();
        Rectangle otherRect = other.getCollisionComponent().getBounds();

        if (playerRect.overlaps(otherRect)) {
            // Simple collision response: move player back to previous position
            this.getPosition().set(this.getPreviousPosition());
            this.hasCollided = true;
        } else {
            this.hasCollided = false;
        }
    }

    public void pickup(int itemID){}

    public void drop(int itemID){}
}
