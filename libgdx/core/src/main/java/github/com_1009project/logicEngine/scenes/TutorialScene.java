package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.BackgroundLayer;
import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.Layer;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;
import github.com_1009project.logicEngine.scenes.KeyDisplay;

public class TutorialScene extends Scene {

    // Rendering tools
    private ShapeRenderer shapeRenderer;
    private SpriteBatch   fontBatch;
    private BitmapFont    titleFont;
    private BitmapFont    keyFont;
    private BitmapFont    descFont;
    private GlyphLayout   layout;
    private Skin          skin;

    // Key displays
    private KeyDisplay[] keys;

    // Colours
    private static final Color COL_PANEL  = new Color(0.35f, 0.20f, 0.10f, 0.95f);
    private static final Color COL_BORDER = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_HEADER = new Color(0.22f, 0.12f, 0.05f, 1.00f);
    private static final Color COL_CREAM  = new Color(1.00f, 0.92f, 0.70f, 1.00f);

    public TutorialScene(int id, AssetManager assetManager, EntityRegistry entityRegistry,
                         EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityRegistry, eventManager, batch, sceneManager);
        init();
    }

    // Scene lifecycle methods
    @Override
    public void init() {
        // background
        layers.add(new BackgroundLayer(batch, assetManager, "imgs/bg_frames", 52, 0.1f));

        // shared rendering tools
        shapeRenderer = new ShapeRenderer();
        fontBatch     = new SpriteBatch();
        layout        = new GlyphLayout();

        titleFont = new BitmapFont(); titleFont.getData().setScale(2.4f); titleFont.setColor(COL_CREAM);
        keyFont   = new BitmapFont(); keyFont.getData().setScale(1.6f);
        descFont  = new BitmapFont(); descFont.getData().setScale(1.3f); descFont.setColor(COL_CREAM);

        buildKeys();
        buildButtons();
    }
    // Set up the KeyDisplay objects, which know how to draw themselves and track their own pressed state.
    private void buildKeys() {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float ks = KeyDisplay.KEY_SIZE;
        float kg = KeyDisplay.KEY_GAP;

        float cx = sw / 2f - 260f;
        float cy = sh / 2f - 20f;
        float ax = sw / 2f + 60f;

        keys = new KeyDisplay[] {
            new KeyDisplay("W", Input.Keys.W, cx + ks + kg,         cy + ks + kg, "Move up",                  shapeRenderer, fontBatch, keyFont),
            new KeyDisplay("A", Input.Keys.A, cx,                   cy,           "Move left",                shapeRenderer, fontBatch, keyFont),
            new KeyDisplay("S", Input.Keys.S, cx + ks + kg,         cy,           "Move down",                shapeRenderer, fontBatch, keyFont),
            new KeyDisplay("D", Input.Keys.D, cx + 2 * (ks + kg),   cy,           "Move right",               shapeRenderer, fontBatch, keyFont),
            new KeyDisplay("E", Input.Keys.E, ax,                   cy + ks + kg, "Pick up / Put down item",  shapeRenderer, fontBatch, keyFont),
            new KeyDisplay("F", Input.Keys.F, ax,                   cy - ks - 2f, "Chop ingredient",          shapeRenderer, fontBatch, keyFont),
        };
    }
    // Set up the buttons at the bottom of the screen. They use Scene2D UI, which is separate from our usual rendering tools.
    private void buildButtons() {
        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));

        float sw  = Gdx.graphics.getWidth();
        float cx  = sw / 2f;
        float btnY = Gdx.graphics.getHeight() * 0.06f;

        TextButton startBtn = new TextButton("Start Game", skin, "warm-resume");
        startBtn.setSize(220f, 55f);
        startBtn.setPosition(cx - 110f + 80f, btnY);
        startBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                sceneManager.loadScene(1);
            }
        });

        TextButton backBtn = new TextButton("Back", skin, "warm-quit");
        backBtn.setSize(120f, 55f);
        backBtn.setPosition(cx - 110f + 80f - 140f, btnY);
        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                sceneManager.loadScene(0);
            }
        });

        uiLayer.getStage().addActor(startBtn);
        uiLayer.getStage().addActor(backBtn);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (KeyDisplay key : keys) {
            key.update(); // each key polls its own keycode — SRP
        }
    }

    @Override
    public void render() {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cx = sw / 2f;

        // background layer
        for (Layer layer : layers) {
            if (!(layer instanceof UILayer)) layer.render();
        }

        // dark tint
        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.55f);
        shapeRenderer.rect(0, 0, sw, sh);
        shapeRenderer.end();
        disableBlend();

        drawPanel(cx, sh);
        drawKeys();
        drawText(cx, sh);

        // UI layer (buttons) last
        for (Layer layer : layers) {
            if (layer instanceof UILayer) layer.render();
        }
    }

    // Drawing helpers
    private void drawPanel(float cx, float sh) {
        float panelW = 700f, panelH = 420f;
        float panelX = cx - panelW / 2f;
        float panelY = sh / 2f - panelH / 2f + 30f;
        float b = 3f, headerH = 62f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.40f);
        shapeRenderer.rect(panelX + 6, panelY - 6, panelW, panelH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COL_PANEL);
        shapeRenderer.rect(panelX, panelY, panelW, panelH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COL_BORDER);
        shapeRenderer.rect(panelX,              panelY,              panelW, b);
        shapeRenderer.rect(panelX,              panelY + panelH - b, panelW, b);
        shapeRenderer.rect(panelX,              panelY,              b,      panelH);
        shapeRenderer.rect(panelX + panelW - b, panelY,              b,      panelH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COL_HEADER);
        shapeRenderer.rect(panelX + b, panelY + panelH - headerH, panelW - 2 * b, headerH - b);
        shapeRenderer.end();
    }
    // Each KeyDisplay knows how to draw itself, so we just loop through and call render on each one.
    private void drawKeys() {
        for (KeyDisplay key : keys) {
            key.render();
        }
    }
    // Draw the static text elements: title, labels, descriptions.
    private void drawText(float cx, float sh) {
        float panelH  = 420f;
        float panelY  = sh / 2f - panelH / 2f + 30f;
        float headerH = 62f;

        fontBatch.begin();

        // title
        layout.setText(titleFont, "HOW TO PLAY");
        titleFont.draw(fontBatch, layout,
                cx - layout.width / 2f,
                panelY + panelH - (headerH - layout.height) / 2f);

        // "Movement" label above WASD cluster
        KeyDisplay wKey = keys[0];
        layout.setText(descFont, "Movement");
        descFont.draw(fontBatch, layout,
                wKey.getBounds().x - layout.width / 4f,
                wKey.getBounds().y + KeyDisplay.KEY_SIZE + 36f);

        // descriptions next to action keys (E and F — indices 4 and 5)
        float padding = 20f;
        for (int i = 4; i < keys.length; i++) {
            KeyDisplay key = keys[i];
            layout.setText(descFont, key.getDescription());
            descFont.draw(fontBatch, layout,
                    key.getBounds().x + KeyDisplay.KEY_SIZE + padding,
                    key.getBounds().y + (KeyDisplay.KEY_SIZE + layout.height) / 2f);
        }

        // ESC hint at bottom
        descFont.setColor(new Color(1f, 1f, 1f, 0.55f));
        layout.setText(descFont, "Press ESC to pause during the game");
        descFont.draw(fontBatch, layout, cx - layout.width / 2f, panelY + 20f);
        descFont.setColor(COL_CREAM);

        fontBatch.end();
    }

    private void enableBlend() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void disableBlend() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        fontBatch.dispose();
        titleFont.dispose();
        keyFont.dispose();
        descFont.dispose();
        skin.dispose();
    }
}
