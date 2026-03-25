package github.com_1009project.logicEngine.factories;

import com.badlogic.gdx.assets.AssetManager;
import github.com_1009project.abstractEngine.Ifactory;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Tomato;

public class TomatoFactory implements Ifactory<Tomato> {

    private final AssetManager assetManager;

    public TomatoFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Tomato createEntity(float x, float y, float width, float height) {
        // Create and return a new Tomato entity with default properties
        return new Tomato(x, y, width, height, assetManager.get("food/tomato.png", Texture.class)); // Texture can be set later
    }

}
