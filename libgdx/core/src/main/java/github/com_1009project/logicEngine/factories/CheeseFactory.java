package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import github.com_1009project.abstractEngine.Ifactory;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Cheese;

public class CheeseFactory implements Ifactory<Cheese> {

    private final AssetManager assetManager;

    public CheeseFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Cheese createEntity(float x, float y, float width, float height) {
        // Create and return a new Cheese entity with default properties
        return new Cheese(x, y, width, height, assetManager.get("food/cheese.png", Texture.class)); // Texture can be set later
    }

}