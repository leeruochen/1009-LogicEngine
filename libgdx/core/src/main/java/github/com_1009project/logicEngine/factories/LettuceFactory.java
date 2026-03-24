package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Lettuce;

public class LettuceFactory implements Ifactory<Lettuce> {

    @Override
    public Lettuce createEntity(float x, float y, float width, float height) {
        // Create and return a new Lettuce entity with default properties
        return new Lettuce(x, y, width, height, Assets.getAssetManager().get("food/Lettuce.png", Texture.class)); // Texture can be set later
    }

}
