package github.com_1009project.logicEngine;

public enum FoodItem {
    BUN    ("Bun",     "food/bun.png"),
    LETTUCE("Lettuce", "food/lettuce_chopped.png"),
    TOMATO ("Tomato",  "food/tomato_chopped.png"),
    CHEESE ("Cheese",  "food/cheese.png"),
    PATTY  ("Patty",   "food/patty_cooked.png");
 
    private final String displayName;
    private final String assetPath;
 
    FoodItem(String displayName, String assetPath) {
        this.displayName = displayName;
        this.assetPath   = assetPath;
    }
 
    public String getDisplayName() { return displayName; }
 
    public String getAssetPath()   { return assetPath; }
}