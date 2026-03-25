package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Stove;

public class StoveFactory implements Ifactory<Stove> {

    @Override
    public Stove createEntity(float x, float y, float width, float height) {
        // Create and return a new Stove entity with default properties
        return new Stove(x, y, width, height, Assets.getAssetManager().get("foodstations/stove.png", Texture.class)); // Texture can be set later
    }

}
