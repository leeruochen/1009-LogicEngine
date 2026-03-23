package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Plate;

public class PlateFactory implements Ifactory<Plate> {

    private final AssetManager assetManager;

    public PlateFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Plate createEntity(float x, float y, float width, float height) {
        return new Plate(x, y, width, height, assetManager.get("food/dish.png", Texture.class));
    }
}
