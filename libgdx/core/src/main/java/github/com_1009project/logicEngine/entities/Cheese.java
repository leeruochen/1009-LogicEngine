package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

public class Cheese extends Ingredient {

    public Cheese(float x, float y, float w, float h,Texture texture) {
        super("Cheese", FoodState.Raw, x, y, w, h, texture);
    }

}
