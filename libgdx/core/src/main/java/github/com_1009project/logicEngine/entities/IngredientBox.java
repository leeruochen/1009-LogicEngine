package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.Station;

public class IngredientBox extends Station {
    private String ingredientType; 

    public IngredientBox(float x, float y, float w, float h, Texture texture, String ingredientType) {
        super(x, y, w, h, texture);
        this.ingredientType = ingredientType;
    }
}