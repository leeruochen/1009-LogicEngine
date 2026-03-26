package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.abstractEngine.Ifactory;
import github.com_1009project.logicEngine.entities.RubbishBin;

public class RubbishBinFactory implements Ifactory<RubbishBin> {

    private final AssetManager assetManager;

    public RubbishBinFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public RubbishBin createEntity(float x, float y, float width, float height) {
        // Create and return a new RubbishBin entity with default properties
        return new RubbishBin(x, y, width, height, assetManager.get("foodstations/rubbishBin.png", Texture.class)); // Texture can be set later
    }

}
