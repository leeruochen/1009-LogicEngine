package github.com_1009project.logicEngine.ingredients;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Tomato extends Ingredient {

    public Tomato(Texture texture) {
        super("Tomato", FoodState.Raw, texture);
    }

}
