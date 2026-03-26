package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.Ifactory;
import github.com_1009project.logicEngine.entities.Lettuce;

public class LettuceFactory implements Ifactory<Lettuce> {

    private final AssetManager assetManager;

    public LettuceFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Lettuce createEntity(float x, float y, float width, float height) {
        // Create and return a new Lettuce entity with default properties
        return new Lettuce(x, y, width, height, assetManager.get("food/lettuce.png", Texture.class)); // Texture can be set later
    }

}
