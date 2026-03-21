package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Tomato extends Ingredient {

    public Tomato(float x, float y, float w, float h, Texture texture) {
        super("Tomato", FoodState.Raw, x, y, w, h, texture);
    }

}
