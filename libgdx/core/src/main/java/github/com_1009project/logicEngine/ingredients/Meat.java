package github.com_1009project.logicEngine.ingredients;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Meat extends Ingredient {

    public Meat(Texture texture) {
        super("Meat", FoodState.Raw, texture);
    }

}
