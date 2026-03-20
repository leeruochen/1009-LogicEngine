package github.com_1009project.logicEngine.ingredients;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Lettuce extends Ingredient {

    public Lettuce(Texture texture) {
        super("Lettuce", FoodState.Raw, texture);
    }

}
