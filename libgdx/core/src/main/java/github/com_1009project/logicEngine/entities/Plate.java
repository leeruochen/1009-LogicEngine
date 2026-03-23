package github.com_1009project.logicEngine.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.logicEngine.FoodItem;
import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

/**
 * A Plate is a special Ingredient that holds assembled FoodItems for submission.
 * Finished (Cooked) ingredients can be added onto it. The plate tracks
 * the list of FoodItems for recipe matching via FoodQueue.submitOrder().
 */
public class Plate extends Ingredient {

    private final List<FoodItem> assembledItems = new ArrayList<>();
    private final List<Ingredient> stackedIngredients = new ArrayList<>();
    private static final float STACK_OFFSET_Y = 10f;

    public Plate(float x, float y, float w, float h, Texture texture) {
        super("Plate", FoodState.Raw, x, y, w, h, texture);
    }

    /**
     * Adds a finished ingredient onto this plate.
     * @return true if the ingredient was accepted.
     */
    public boolean addIngredient(Ingredient ingredient) {
        FoodItem foodItem = toFoodItem(ingredient);
        if (foodItem == null) return false;

        assembledItems.add(foodItem);
        stackedIngredients.add(ingredient);
        ingredient.setActive(true);
        return true;
    }

    /** @return the assembled FoodItem list for recipe matching. */
    public List<FoodItem> getAssembledItems() {
        return assembledItems;
    }

    public boolean isEmpty() {
        return assembledItems.isEmpty();
    }

    public int getItemCount() {
        return assembledItems.size();
    }

    /**
     * Maps an Ingredient entity to its FoodItem enum for recipe matching.
     */
    private FoodItem toFoodItem(Ingredient ingredient) {
        String name = ingredient.getName().toLowerCase();
        switch (name) {
            case "bread":  return FoodItem.BUN;
            case "bun":    return FoodItem.BUN;
            case "lettuce": return FoodItem.LETTUCE;
            case "tomato":  return FoodItem.TOMATO;
            case "cheese":  return FoodItem.CHEESE;
            case "patty":   return FoodItem.PATTY;
            default: return null;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!this.isActive()) return;
        // Render plate base
        super.render(batch);
        // Render stacked ingredients above the plate
        for (int i = 0; i < stackedIngredients.size(); i++) {
            Ingredient item = stackedIngredients.get(i);
            float cx = this.getPosition().x + (this.getSize().x - item.getSize().x) / 2f;
            float cy = this.getPosition().y + (i + 1) * STACK_OFFSET_Y;
            item.setPosition(cx, cy);
            item.render(batch);
        }
    }
}
