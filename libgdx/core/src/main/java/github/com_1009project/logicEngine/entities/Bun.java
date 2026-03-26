package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Bun extends Ingredient {

    public Bun(float x, float y, float w, float h,Texture texture) {
        super("Bread", FoodState.Cooked, x, y, w, h, texture);
    }

}