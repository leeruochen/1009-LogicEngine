package github.com_1009project.logicEngine.factories;


import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.entities.IngredientBox;

public class IngredientBoxFactory implements Ifactory<IngredientBox> {

    @Override
    public IngredientBox createEntity(float x, float y, float width, float height) {
        return null;
    }

    @Override 
    public IngredientBox createEntity(float x, float y, float width, float height, Texture texture, String ingredientType) {
        return new IngredientBox(x, y, width, height, texture, ingredientType);
    }

}
