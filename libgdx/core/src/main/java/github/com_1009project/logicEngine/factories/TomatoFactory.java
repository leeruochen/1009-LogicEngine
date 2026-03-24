package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Tomato;

public class TomatoFactory implements Ifactory<Tomato> {

    @Override
    public Tomato createEntity(float x, float y, float width, float height) {
        // Create and return a new Tomato entity with default properties
        return new Tomato(x, y, width, height, Assets.getAssetManager().get("food/Tomato.png", Texture.class)); // Texture can be set later
    }

}
