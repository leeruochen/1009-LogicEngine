package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.PlateBox;

public class PlateBoxFactory implements Ifactory<PlateBox> {

    private final AssetManager assetManager;

    public PlateBoxFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public PlateBox createEntity(float x, float y, float width, float height) {
        // Create and return a new PlateBox entity with default properties
        return new PlateBox(x, y, width, height, assetManager.get("foodstations/plate_box.png", Texture.class)); // Texture can be set later
    }

}
