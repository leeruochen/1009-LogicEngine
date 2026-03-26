package github.com_1009project.logicEngine.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.logicEngine.BurgerRecipe;
import github.com_1009project.logicEngine.FoodItem;
import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;

/**
 * A Plate is a special Ingredient that holds assembled FoodItems for submission.
 * <p>
 * Finished (Cooked) ingredients can be added onto it.  After every addition the
 * plate checks whether the current ingredient set matches a known
 * {@link BurgerRecipe}.  When a match is found the plate:
 * <ul>
 *   <li>swaps its texture to the finished burger image,</li>
 *   <li>hides the individual stacked ingredient sprites, and</li>
 *   <li>locks itself so no further ingredients can be added.</li>
 * </ul>
 */
public class Plate extends Ingredient {

    private final List<FoodItem>   assembledItems      = new ArrayList<>();
    private final List<Ingredient> stackedIngredients   = new ArrayList<>();
    private final AssetManager     assetManager;

    /** Non-null once the assembled items match a recipe. */
    private BurgerRecipe matchedRecipe;

    /** True once a recipe has been matched — plate is locked. */
    private boolean completed;

    private static final float STACK_OFFSET_Y = 10f;

    public Plate(float x, float y, float w, float h, Texture texture, AssetManager assetManager) {
        super("Plate", FoodState.Cooked, x, y, w, h, texture);
        this.assetManager = assetManager;
        this.completed    = false;
        this.matchedRecipe = null;
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Adds a finished ingredient onto this plate.
     * Rejected if the plate is already completed (recipe matched).
     *
     * @return true if the ingredient was accepted.
     */
    public boolean addIngredient(Ingredient ingredient) {
        if (completed) return false;

        if (ingredient.getState() != FoodState.Cooked) return false;
        FoodItem foodItem = toFoodItem(ingredient);
        if (foodItem == null) return false;

        assembledItems.add(foodItem);
        stackedIngredients.add(ingredient);
        ingredient.setActive(true);

        // After every addition, check for a recipe match
        checkRecipeMatch();
        return true;
    }

    /** @return the assembled FoodItem list for recipe matching. */
    public List<FoodItem> getAssembledItems() { return assembledItems; }

    public boolean isEmpty()    { return assembledItems.isEmpty(); }
    public int     getItemCount() { return assembledItems.size(); }

    /** @return true once a valid burger recipe has been assembled. */
    public boolean isComplete() { return completed; }

    /** @return the matched recipe, or null if no match yet. */
    public BurgerRecipe getMatchedRecipe() { return matchedRecipe; }

    // ── Recipe matching ──────────────────────────────────────────────────────

    /**
     * Checks whether the current assembled items match any known
     * {@link BurgerRecipe}.  If so, swaps the plate texture to the finished
     * burger image and locks the plate.
     */
    private void checkRecipeMatch() {
        BurgerRecipe matched = BurgerRecipe.fromIngredients(assembledItems);
        if (matched == null) return;

        this.matchedRecipe = matched;
        this.completed     = true;

        // Swap plate texture to the finished burger sprite
        String imagePath = matched.getBurgerImagePath();
        if (assetManager.isLoaded(imagePath)) {
            this.setTexture(assetManager.get(imagePath, Texture.class));
        }

        // Hide individual ingredient sprites — the burger image replaces them
        for (Ingredient item : stackedIngredients) {
            item.setActive(false);
        }

        Gdx.app.log("Plate", "Recipe matched: " + matched.getDisplayName()
                + " — plate locked, texture updated to " + imagePath);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Maps an Ingredient entity to its FoodItem enum for recipe matching.
     */
    private FoodItem toFoodItem(Ingredient ingredient) {
        String name = ingredient.getName().toLowerCase();
        switch (name) {
            case "bread":   return FoodItem.BUN;
            case "bun":     return FoodItem.BUN;
            case "lettuce": return FoodItem.LETTUCE;
            case "tomato":  return FoodItem.TOMATO;
            case "cheese":  return FoodItem.CHEESE;
            case "patty":   return FoodItem.PATTY;
            default:        return null;
        }
    }

    // ── Rendering ────────────────────────────────────────────────────────────

    @Override
    public void render(SpriteBatch batch) {
        if (!this.isActive()) return;

        // Always render the plate/burger base texture
        super.render(batch);

        // If recipe is complete the burger image IS the base texture;
        // individual ingredients were deactivated, so nothing extra to draw.
        if (completed) return;

        // Otherwise render stacked ingredient sprites above the plate
        for (int i = 0; i < stackedIngredients.size(); i++) {
            Ingredient item = stackedIngredients.get(i);
            float cx = this.getPosition().x + (this.getSize().x - item.getSize().x) / 2f;
            float cy = this.getPosition().y + (i + 1) * STACK_OFFSET_Y;
            item.setPosition(cx, cy);
            item.render(batch);
        }
    }

    public List<Ingredient> getStackedIngredients() { return stackedIngredients; }

    //destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        stackedIngredients.clear();
        assembledItems.clear();
    }
}
