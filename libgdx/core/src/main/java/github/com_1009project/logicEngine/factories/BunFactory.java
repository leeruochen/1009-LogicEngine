package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Ifactory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Bun;

public class BunFactory implements Ifactory<Bun> {

    private final AssetManager assetManager;

    public BunFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Bun createEntity(float x, float y, float width, float height) {
        // Create and return a new Bun entity with default properties
        return new Bun(x, y, width, height, assetManager.get("food/bun.png", Texture.class)); // Texture can be set later
    }

}
