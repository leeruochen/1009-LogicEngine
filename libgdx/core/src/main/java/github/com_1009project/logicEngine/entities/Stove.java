package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import github.com_1009project.abstractEngine.IUpdatable;
import github.com_1009project.logicEngine.FoodState;
import github.com_1009project.logicEngine.Ingredient;
import github.com_1009project.logicEngine.Station;

/**
 * A station that cooks Patties over time.
 * After COOK_TIME seconds the patty becomes Cooked.
 * If left for longer than BURN_TIME seconds total, it becomes Overcooked (burnt).
 */
public class Stove extends Station implements IUpdatable {

    private static final float COOK_TIME = 5f;   // seconds to cook
    private static final float BURN_TIME = 10f;   // seconds total before burning

    private Ingredient placedIngredient;
    private float cookTimer;
    private Texture cookedTexture;
    private Texture stoveCookingTexture;
    private Texture stoveDefaultTexture;
    private Texture burntTexture;

    public Stove(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
        this.stoveDefaultTexture = texture;
        this.placedIngredient = null;
        this.cookTimer = 0f;
    }

    /** Set the texture shown for a cooking patty and stove-in-use. */
    public void setCookedTexture(Texture cookedTex) {
        this.cookedTexture = cookedTex;
    }

    public void setStoveCookingTexture(Texture tex) {
        this.stoveCookingTexture = tex;
    }

    public void setBurntTexture(Texture burntTex) {
        this.burntTexture = burntTex;
    }

    // ── Station item API ─────────────────────────────────────────────────────

    public boolean hasIngredient() { return placedIngredient != null; }
    public Ingredient getPlacedIngredient() { return placedIngredient; }

    public boolean canAccept(Ingredient ingredient) {
        if (placedIngredient != null) return false;
        if (ingredient.getState() != FoodState.Raw) return false;
        return ingredient.getName().toLowerCase().equals("patty");
    }

    public void placeIngredient(Ingredient ingredient) {
        this.placedIngredient = ingredient;
        this.cookTimer = 0f;
        float cx = this.getPosition().x + (this.getSize().x - ingredient.getSize().x) / 2f;
        float cy = this.getPosition().y + this.getSize().y * 0.6f;
        ingredient.setPosition(cx, cy);
        ingredient.setActive(true);
    }

    public Ingredient removeIngredient() {
        Ingredient item = this.placedIngredient;
        this.placedIngredient = null;
        this.cookTimer = 0f;
        return item;
    }

    /** @return cooking progress 0.0 – 1.0 (capped at 1.0 when cooked). */
    public float getCookProgress() {
        if (placedIngredient == null) return 0f;
        return Math.min(1f, cookTimer / COOK_TIME);
    }

    public boolean isBurnt() {
        return placedIngredient != null && placedIngredient.getState() == FoodState.Overcooked;
    }

    // ── IUpdatable ───────────────────────────────────────────────────────────

    @Override
    public void update(float deltaTime) {
        if (placedIngredient == null) return;
        if (placedIngredient.getState() == FoodState.Overcooked) return; // already burnt

        cookTimer += deltaTime;

        if (cookTimer >= BURN_TIME) {
            placedIngredient.setState(FoodState.Overcooked);
            // Burnt patty keeps its texture but could be tinted; for now just mark state
            if (burntTexture != null) {
                placedIngredient.setTexture(burntTexture);
            }
        } else if (cookTimer >= COOK_TIME && placedIngredient.getState() == FoodState.Raw) {
            placedIngredient.setState(FoodState.Cooked);
            if (cookedTexture != null) {
                placedIngredient.setTexture(cookedTexture);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!this.isActive()) return;
        // Show cooking texture if stove is active
        if (placedIngredient != null && stoveCookingTexture != null) {
            batch.draw(stoveCookingTexture, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
        } else {
            super.render(batch);
        }
        // Render ingredient on top
        if (placedIngredient != null && placedIngredient.isActive()) {
            placedIngredient.render(batch);
        }
    }
}
