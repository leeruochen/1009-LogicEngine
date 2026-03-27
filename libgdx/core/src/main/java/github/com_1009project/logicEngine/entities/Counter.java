package github.com_1009project.logicEngine.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;
import github.com_1009project.logicEngine.Station;

/**
 * A counter surface where finished (Cooked) ingredients can be placed and stacked.
 * Ingredients stack visually on top of each other.
 */
public class Counter extends Station implements ICollidable {

    private static final float STACK_OFFSET_Y = 12f;
    private static final int MAX_STACK_SIZE = 6;

    private final List<Ingredient> ingredientStack = new ArrayList<>();

    public Counter(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
    }

    public boolean hasIngredients() { return !ingredientStack.isEmpty(); }
    public int getStackSize() { return ingredientStack.size(); }
    public List<Ingredient> getIngredientStack() { return ingredientStack; }

    /**
     * @return true if this counter can accept another ingredient.
     */
    public boolean canAccept(Ingredient ingredient) {
        return ingredientStack.size() < MAX_STACK_SIZE;
    }

    public boolean checkCooked() {
        for (Ingredient ingredient : ingredientStack) {
            if (ingredient.getState() != FoodState.Cooked) {
                return false;
            }
        }
        return true;
    }

    /**
     * Place an ingredient on top of the stack.
     */
    public boolean placeIngredient(Ingredient ingredient) {
        if (checkCooked()){
            ingredientStack.add(ingredient);
            repositionStack();
            ingredient.setActive(true);
            return true;
        } 
        return false;

    }

    /**
     * Remove and return the top ingredient from the stack.
     */
    public Ingredient removeTopIngredient() {
        if (ingredientStack.isEmpty()) return null;
        Ingredient top = ingredientStack.remove(ingredientStack.size() - 1);
        repositionStack();
        return top;
    }

    /**
     * Remove and return ALL ingredients (used when picking up a full plate/stack).
     */
    public List<Ingredient> removeAllIngredients() {
        List<Ingredient> all = new ArrayList<>(ingredientStack);
        ingredientStack.clear();
        return all;
    }

    private void repositionStack() {
        for (int i = 0; i < ingredientStack.size(); i++) {
            Ingredient item = ingredientStack.get(i);
            float cx = this.getPosition().x + (this.getSize().x - item.getSize().x) / 2f;
            float cy = this.getPosition().y + this.getSize().y * 0.5f + i * STACK_OFFSET_Y;
            item.setPosition(cx, cy);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        for (Ingredient item : ingredientStack) {
            if (item.isActive()) {
                item.render(batch);
            }
        }
    }

    @Override
    public void onCollision(Entity other) {
        // Counter blocks movement via collision - no special logic needed
    }
}
