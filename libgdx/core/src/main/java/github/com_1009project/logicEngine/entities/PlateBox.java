package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.logicEngine.Station;

/**
 * An infinite supply box for plates.
 * When the player interacts while empty-handed, they get a new empty Plate.
 */
public class PlateBox extends Station implements ICollidable {
    public PlateBox(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }

    @Override
    public void onCollision(Entity other) {
        // collision handled by engine
    }
}
