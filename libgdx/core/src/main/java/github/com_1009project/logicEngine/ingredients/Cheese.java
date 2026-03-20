package github.com_1009project.logicEngine.ingredients;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Cheese extends Ingredient {

    public Cheese(Texture texture) {
        super("Cheese", FoodState.Raw, texture);
    }

}
