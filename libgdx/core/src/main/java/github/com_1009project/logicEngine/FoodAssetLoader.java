package github.com_1009project.logicEngine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Single Responsibility: loads all food-related textures into the AssetManager.
 *
 * Keeping asset loading out of FoodQueueUI lets the UI class focus purely
 * on rendering, and lets other systems (e.g. a loading screen) own the
 * loading lifecycle.
 */
public class FoodAssetLoader {

    private final AssetManager assetManager;

    public FoodAssetLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /** Enqueues all ingredient and burger textures for loading. */
    public void queueAssets() {
        queueAssets(assetManager);
    }

    /**
     * Static overload — use this when you don't have an instance yet,
     * e.g. inside GameMaster.loadAssets() before the queue is initialised.
     */
    public static void queueAssets(AssetManager assetManager) {
        for (FoodItem item : FoodItem.values()) {
            assetManager.load(item.getAssetPath(), Texture.class);
        }
        for (BurgerRecipe recipe : BurgerRecipe.values()) {
            assetManager.load(recipe.getBurgerImagePath(), Texture.class);
        }
    }

    /** Returns a loaded texture by path. Assets must be fully loaded first. */
    public Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }
}