package github.com_1009project.logicEngine;

/**
 * Represents a single food order sitting in the queue.
 * Each order has a recipe and an individual countdown timer.
 */
public class FoodOrder {

    /** Default time (seconds) a customer will wait before the order expires. */
    public static final float DEFAULT_ORDER_TIME = 30f;

    private final BurgerRecipe recipe;
    private final float maxTime;
    private float timeRemaining;
    private boolean expired;

    // ── Unique ID so the UI can track/animate individual orders
    private static int idCounter = 0;
    private final int id;

    public FoodOrder(BurgerRecipe recipe) {
        this(recipe, DEFAULT_ORDER_TIME);
    }

    public FoodOrder(BurgerRecipe recipe, float maxTime) {
        this.recipe        = recipe;
        this.maxTime       = maxTime;
        this.timeRemaining = maxTime;
        this.expired       = false;
        this.id            = idCounter++;
    }

    /**
     * Tick the order timer down.
     */
    public boolean update(float deltaTime) {
        if (expired) return false;

        timeRemaining -= deltaTime;
        if (timeRemaining <= 0f) {
            timeRemaining = 0f;
            expired       = true;
            return true;   // expired THIS frame
        }
        return false;
    }

    public int getId()                 { return id; }
    public BurgerRecipe getRecipe()    { return recipe; }
    public float getTimeRemaining()    { return timeRemaining; }
    public float getMaxTime()          { return maxTime; }
    public boolean isExpired()         { return expired; }

    /**
     * 0.0 (empty) → 1.0 (full).
     * Drives ProgressBar value directly.
     */
    public float getTimerProgress() {
        return maxTime > 0f ? timeRemaining / maxTime : 0f;
    }

    /**
     * Returns a 0-1 urgency level (inverted progress).
     * 0 = plenty of time, 1 = about to expire.
     */
    public float getUrgency() {
        return 1f - getTimerProgress();
    }

    @Override
    public String toString() {
        return "FoodOrder{id=" + id + ", recipe=" + recipe.name()
                + ", time=" + String.format("%.1f", timeRemaining) + "s}";
    }
}
