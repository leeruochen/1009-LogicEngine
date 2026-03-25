package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import github.com_1009project.abstractEngine.Ifactory;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Stove;

public class StoveFactory implements Ifactory<Stove> {

    private final AssetManager assetManager;

    public StoveFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Stove createEntity(float x, float y, float width, float height) {
        // Create and return a new Stove entity with default properties
        return new Stove(x, y, width, height, assetManager.get("foodstations/stove.png", Texture.class)); // Texture can be set later
    }

}
