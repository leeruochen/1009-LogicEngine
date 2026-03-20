package github.com_1009project.logicEngine;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.abstractEngine.Entity;

public class Counter extends Station implements ICollidable {
    public Counter(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }

    @Override
    public void onCollision(Entity other) {
        // able to place foods on counter
    }
}
