package github.com_1009project.logicEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
 
public enum BurgerRecipe {
 
    BURGER_1(
        "Classic Burger", "food/burger.png",
        FoodItem.BUN, FoodItem.LETTUCE, FoodItem.TOMATO, FoodItem.CHEESE, FoodItem.PATTY
    ),
    BURGER_2(
        "Double Patty", "food/Burger_nolettuce.png",
        FoodItem.BUN, FoodItem.TOMATO, FoodItem.CHEESE, FoodItem.PATTY, FoodItem.PATTY
    ),
    BURGER_3(
        "Green Double", "food/Burger_notomato.png",
        FoodItem.BUN, FoodItem.LETTUCE, FoodItem.CHEESE, FoodItem.PATTY, FoodItem.PATTY
    );
 
    private final String                  displayName;
    private final String                  burgerImagePath;
    private final List<FoodItem>          ingredients;
    private final Map<FoodItem, Integer>  counts;
 
    BurgerRecipe(String displayName, String burgerImagePath, FoodItem... items) {
        this.displayName     = displayName;
        this.burgerImagePath = burgerImagePath;
        this.ingredients     = Collections.unmodifiableList(Arrays.asList(items));
 
        Map<FoodItem, Integer> map = new EnumMap<>(FoodItem.class);
        for (FoodItem item : items) {
            map.put(item, map.getOrDefault(item, 0) + 1);
        }
        this.counts = Collections.unmodifiableMap(map);
    }
 
    public String getDisplayName() { 
    	return displayName; 
    }
 
    /** Path to the finished burger image, e.g. {@code "food/burger.png"}. */
    public String getBurgerImagePath() { 
    	return burgerImagePath; 
    }
 
    /** Ordered ingredient list (order is illustrative only — matching ignores order). */
    public List<FoodItem> getIngredients() {
    	return ingredients; 
    }
 
    /** Ingredient frequency map used for order-independent matching. */
    public Map<FoodItem, Integer> getIngredientCounts() {
    	return counts; 
    }
 
    /** Human-readable ingredient summary, e.g. "Bun, Lettuce, Tomato, Cheese, Patty". */
    public String getIngredientSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            sb.append(ingredients.get(i).getDisplayName());
            if (i < ingredients.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
 
    /**
     * Resolves a player-assembled ingredient list to a recipe, regardless of
     * the order the ingredients were added.
     */
    public static BurgerRecipe fromIngredients(List<FoodItem> assembled) {
        // Build a frequency map of the submitted ingredients
        Map<FoodItem, Integer> submittedCounts = new EnumMap<>(FoodItem.class);
        for (FoodItem item : assembled) {
            submittedCounts.put(item, submittedCounts.getOrDefault(item, 0) + 1);
        }
 
        for (BurgerRecipe recipe : values()) {
            if (recipe.counts.equals(submittedCounts)) {
                return recipe;
            }
        }
        return null;   // no recipe matches
    }
}
