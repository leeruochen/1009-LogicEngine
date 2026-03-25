package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import github.com_1009project.abstractEngine.Ifactory;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Patty;

public class PattyFactory implements Ifactory<Patty> {

    private final AssetManager assetManager;

    public PattyFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Patty createEntity(float x, float y, float width, float height) {
        // Create and return a new patty entity with default properties
        return new Patty(x, y, width, height, assetManager.get("food/patty.png", Texture.class)); // Texture can be set later
    }

}
