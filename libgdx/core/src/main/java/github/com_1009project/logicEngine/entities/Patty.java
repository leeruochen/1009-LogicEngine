package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Patty extends Ingredient {

    public Patty(float x, float y, float w, float h, Texture texture) {
        super("Patty", FoodState.Raw, x, y, w, h, texture);
    }

}
