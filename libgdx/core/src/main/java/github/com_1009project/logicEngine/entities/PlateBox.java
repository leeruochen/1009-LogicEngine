package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.logicEngine.Station;
import github.com_1009project.abstractEngine.Entity;

public class PlateBox extends Station implements ICollidable {
    public PlateBox(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }

    @Override
    public void onCollision(Entity other) {
    }
}