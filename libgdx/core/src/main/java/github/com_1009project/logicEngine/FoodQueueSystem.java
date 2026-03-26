package github.com_1009project.logicEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.abstractEngine.UILayer;

public class FoodQueueSystem {

    private final SpriteBatch  batch;
    private final AssetManager assetManager;
    private final EventManager eventManager;

    private UILayer   uiLayer;
    private FoodQueue foodQueue;
    private FoodQueueUI foodQueueUI;

    // Textures generated for the skin — kept so dispose() can free them
    private Texture skinBgTex;
    private Texture skinFillTex;
    private Texture skinKnobTex;

    public FoodQueueSystem(SpriteBatch batch, AssetManager assetManager,
                           EventManager eventManager) {
        this.batch        = batch;
        this.assetManager = assetManager;
        this.eventManager = eventManager;
    }

    /** Call inside GameMaster.loadAssets(), before finishLoading(). */
    public static void queueAssets(AssetManager assetManager) {
        FoodAssetLoader.queueAssets(assetManager);
    }

    /** Builds all owned objects. Must be called after assetManager.finishLoading(). */
    public void create() {
        Skin skin = buildSkin();

        uiLayer = new UILayer(batch);

        UIFactory       uiFactory   = new UIFactory(uiLayer, skin, eventManager);
        FoodAssetLoader assetLoader = new FoodAssetLoader(assetManager);

        foodQueue = new FoodQueue();
        foodQueue.setListener(new FoodQueueListener() {
            @Override
            public void onOrderCleared(FoodOrder order) {
                Gdx.app.log("FoodQueue", "Cleared: " + order.getRecipe().getDisplayName()
                        + "  Score: " + foodQueue.getClearedCount());
            }
            @Override
            public void onOrderExpired(FoodOrder order) {
                Gdx.app.log("FoodQueue", "Expired: " + order.getRecipe().getDisplayName());
            }
            @Override
            public void onOrderWrong() {
                Gdx.app.log("FoodQueue", "Wrong ingredients!");
            }
        });

        foodQueueUI = new FoodQueueUI(uiFactory, uiLayer, foodQueue, assetLoader);
    }

    /** Tick the queue model. Call every frame before render(). */
    public void update(float deltaTime) {
        foodQueue.update(deltaTime);
    }

    /**
     * Sync card widgets and draw the HUD Stage.
     * Call every frame after all world rendering is done.
     */
    public void render(float deltaTime) {
        foodQueueUI.update();
        uiLayer.update(deltaTime);
        uiLayer.render();
        // Reset batch color so the tint from ProgressBar.setColor() does not
        // bleed into the world rendering on the next frame
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** UILayer manages its own viewport — no resize needed. */
    public void resize(int width, int height) { }

    /** Frees all owned resources. */
    public void dispose() {
        foodQueueUI.dispose();
        uiLayer.dispose();
        if (skinBgTex   != null) skinBgTex.dispose();
        if (skinFillTex != null) skinFillTex.dispose();
        if (skinKnobTex != null) skinKnobTex.dispose();
    }

    /** Exposes the queue so CounterSubmission can call submitOrder(). */
    public FoodQueue getFoodQueue() {
        return foodQueue;
    }
    
    /**
     * Builds a minimal Skin containing only the ProgressBar style needed by
     * UIFactory.createResourceBar(). No atlas or JSON file required.
     */
    private Skin buildSkin() {
        skinBgTex   = solidTexture(200, 12, new Color(0.25f, 0.25f, 0.25f, 1f));
        skinFillTex = solidTexture(200, 12, new Color(0.2f,  0.8f,  0.2f,  1f));
        skinKnobTex = solidTexture(1,   12, new Color(0f,    0f,    0f,    0f));

        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = new NinePatchDrawable(new NinePatch(skinBgTex,   1, 1, 1, 1));
        style.knobBefore = new NinePatchDrawable(new NinePatch(skinFillTex, 1, 1, 1, 1));
        style.knob       = new NinePatchDrawable(new NinePatch(skinKnobTex, 0, 0, 0, 0));
        style.knob.setMinWidth(0);

        Skin skin = new Skin();
        skin.add("default-horizontal", style);
        return skin;
    }

    private Texture solidTexture(int w, int h, Color color) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }
}