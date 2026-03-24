package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Assets;
import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.Cheese;

public class CheeseFactory implements Ifactory<Cheese> {

    @Override
    public Cheese createEntity(float x, float y, float width, float height) {
        // Create and return a new Cheese entity with default properties
        return new Cheese(x, y, width, height, Assets.getAssetManager().get("food/cheese.png", Texture.class)); // Texture can be set later
    }

}
