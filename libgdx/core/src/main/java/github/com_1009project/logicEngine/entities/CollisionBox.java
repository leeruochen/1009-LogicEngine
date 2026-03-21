package github.com_1009project.logicEngine.entities;

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
}