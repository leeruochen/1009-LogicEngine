package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;
import github.com_1009project.logicEngine.Station;

/**
 * A station where Lettuce, Tomato, and Cheese can be chopped.
 * The player places a raw choppable ingredient, presses the chop button
 * multiple times to chop it, then picks up the chopped ingredient.
 */
public class ChoppingStation extends Station {

    /** Number of chop actions required to finish chopping. */
    private static final float CHOP_TIME_REQUIRED = 1.5f;

    /** The ingredient currently on this chopping board. */
    private Ingredient placedIngredient;

    /** How many chop actions have been applied so far. */
    private float chopProgress;

    /** Texture to apply to the ingredient once chopping is complete. */
    private Texture choppedTexture;

    public ChoppingStation(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
        this.placedIngredient = null;
        this.chopProgress = 0.0f;
        this.choppedTexture = null;
    }

    // ── Station item API ─────────────────────────────────────────────────────

    public boolean hasIngredient() { return placedIngredient != null; }
    public Ingredient getPlacedIngredient() { return placedIngredient; }

    /**
     * Checks whether this ingredient type can be chopped (Lettuce, Tomato, Cheese).
     */
    public boolean canAccept(Ingredient ingredient) {
        if (placedIngredient != null) return false;
        if (ingredient.getState() != FoodState.Raw) return false;
        String name = ingredient.getName().toLowerCase();
        return name.equals("lettuce") || name.equals("tomato") || name.equals("cheese");
    }

    /**
     * Place a raw ingredient on the board.
     */
    public void placeIngredient(Ingredient ingredient, Texture choppedTex) {
        this.placedIngredient = ingredient;
        this.choppedTexture = choppedTex;
        this.chopProgress = 0.0f;
        // Position ingredient visually on the station
        float cx = this.getPosition().x + (this.getSize().x - ingredient.getSize().x) / 2f;
        float cy = this.getPosition().y + this.getSize().y * 0.6f;
        ingredient.setPosition(cx, cy);
        ingredient.setActive(true);
    }

    /**
     * Applies one chop action.
     * @return true if this chop completed the ingredient (state changed to Cooked).
     */
    public boolean chop(float deltaTime) {
        if (placedIngredient == null) return false;
        if (placedIngredient.getState() != FoodState.Raw) return false;

        chopProgress += deltaTime;
        if (chopProgress >= CHOP_TIME_REQUIRED) {
            placedIngredient.setState(FoodState.Cooked);
            if (choppedTexture != null) {
                placedIngredient.setTexture(choppedTexture);
            }
            return true;
        }
        return false;
    }

    /** @return progress 0.0 – 1.0 */
    public float getChopProgress() {
        if (placedIngredient == null) return 0f;
        return Math.min(1f, chopProgress / CHOP_TIME_REQUIRED);
    }

    /** @return true if the placed ingredient is fully chopped and ready for pickup. */
    public boolean isChopComplete() {
        return placedIngredient != null && placedIngredient.getState() == FoodState.Cooked;
    }

    /**
     * Removes and returns the ingredient from the board.
     */
    public Ingredient removeIngredient() {
        Ingredient item = this.placedIngredient;
        this.placedIngredient = null;
        this.chopProgress = 0.0f;
        this.choppedTexture = null;
        return item;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        // Render the placed ingredient on top of the station
        if (placedIngredient != null && placedIngredient.isActive()) {
            placedIngredient.render(batch);
        }
    }
}
