package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;

import github.com_1009project.logicEngine.Station;

/**
 * An infinite supply box for a specific ingredient type.
 * When the player interacts with it while empty-handed, a new ingredient is spawned.
 */
public class IngredientBox extends Station {
    private final String ingredientType;

    public IngredientBox(float x, float y, float w, float h, Texture texture, String ingredientType) {
        super(x, y, w, h, texture);
        this.ingredientType = ingredientType;
    }

    /** @return the ingredient type name, e.g. "bun", "patty", "lettuce", "tomato", "cheese" */
    public String getIngredientType() {
        return ingredientType;
    }
}
