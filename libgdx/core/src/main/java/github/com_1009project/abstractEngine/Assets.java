package github.com_1009project.abstractEngine;

public class Assets {
    private static com.badlogic.gdx.assets.AssetManager assetManager;
    
    private Assets() {}
    
    public static void setAssetManager(com.badlogic.gdx.assets.AssetManager manager) {
        assetManager = manager;
    }
    
    public static com.badlogic.gdx.assets.AssetManager getAssetManager() {
        return assetManager;
    }
}
