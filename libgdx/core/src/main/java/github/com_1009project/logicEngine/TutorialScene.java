package github.com_1009project.logicEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.*;

public class TutorialScene extends Scene {

    private Texture background;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch fontBatch;
    private BitmapFont titleFont, keyFont, descFont;
    private GlyphLayout layout;
    private Skin skin;

    private static final float KEY_SIZE = 55f;
    private static final float KEY_GAP  = 8f;
    private static final float PADDING  = 20f;

    private static final Color COL_PANEL       = new Color(0.35f, 0.20f, 0.10f, 0.95f);
    private static final Color COL_BORDER      = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_HEADER      = new Color(0.22f, 0.12f, 0.05f, 1.00f);
    private static final Color COL_CREAM       = new Color(1.00f, 0.92f, 0.70f, 1.00f);
    private static final Color COL_KEY_NORMAL  = new Color(0.25f, 0.14f, 0.07f, 1.00f);
    private static final Color COL_KEY_PRESSED = new Color(0.90f, 0.60f, 0.15f, 1.00f);
    private static final Color COL_KEY_BORDER  = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_KEY_TEXT    = new Color(1.00f, 0.92f, 0.70f, 1.00f);

    private boolean wPressed, aPressed, sPressed, dPressed, ePressed, fPressed;

    private Rectangle wRect, aRect, sRect, dRect, eRect, fRect;

    public TutorialScene(int id, AssetManager assetManager, EntityManager entityManager,
                         EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityManager, eventManager, batch, sceneManager);
        init();
    }

    @Override
    public void init() {
        background    = assetManager.get("imgs/background.png", Texture.class);
        shapeRenderer = new ShapeRenderer();
        fontBatch     = new SpriteBatch();
        layout        = new GlyphLayout();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.4f);
        titleFont.setColor(COL_CREAM);

        keyFont = new BitmapFont();
        keyFont.getData().setScale(1.6f);

        descFont = new BitmapFont();
        descFont.getData().setScale(1.3f);
        descFont.setColor(COL_CREAM);

        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));

        float sw = Gdx.graphics.getWidth();
        float cx = sw / 2f;
        float btnY = Gdx.graphics.getHeight() * 0.06f;

        TextButton startBtn = new TextButton("Start Game", skin, "warm-resume");
        TextButton backBtn  = new TextButton("Back", skin, "warm-quit");

        startBtn.setSize(220f, 55f);
        backBtn.setSize(120f, 55f);

        float spacing = 20f;
        float offsetX = 100f;

        startBtn.setPosition(cx - startBtn.getWidth()/2f + offsetX, btnY);
        backBtn.setPosition(cx - startBtn.getWidth()/2f - backBtn.getWidth() - spacing + offsetX, btnY);

        startBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                sceneManager.loadScene(1);
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                sceneManager.loadScene(0);
            }
        });

        uiLayer.getStage().addActor(startBtn);
        uiLayer.getStage().addActor(backBtn);

        buildKeyRects();
    }

    private void buildKeyRects() {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cx = sw / 2f;

        float clusterX = cx - 260f;
        float clusterY = sh / 2f - 20f;

        wRect = new Rectangle(clusterX + KEY_SIZE + KEY_GAP, clusterY + KEY_SIZE + KEY_GAP, KEY_SIZE, KEY_SIZE);
        aRect = new Rectangle(clusterX, clusterY, KEY_SIZE, KEY_SIZE);
        sRect = new Rectangle(clusterX + KEY_SIZE + KEY_GAP, clusterY, KEY_SIZE, KEY_SIZE);
        dRect = new Rectangle(clusterX + 2*(KEY_SIZE+KEY_GAP), clusterY, KEY_SIZE, KEY_SIZE);

        float actionX = cx + 60f;

        eRect = new Rectangle(actionX, clusterY + KEY_SIZE + KEY_GAP, KEY_SIZE, KEY_SIZE);
        fRect = new Rectangle(actionX, clusterY - KEY_SIZE - 2f, KEY_SIZE, KEY_SIZE);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        wPressed = Gdx.input.isKeyPressed(Input.Keys.W);
        aPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        sPressed = Gdx.input.isKeyPressed(Input.Keys.S);
        dPressed = Gdx.input.isKeyPressed(Input.Keys.D);
        ePressed = Gdx.input.isKeyPressed(Input.Keys.E);
        fPressed = Gdx.input.isKeyPressed(Input.Keys.F);
    }

    @Override
    public void render() {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cx = sw / 2f;

        batch.begin();
        batch.draw(background, 0, 0, sw, sh);
        batch.end();

        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,0.55f);
        shapeRenderer.rect(0,0,sw,sh);
        shapeRenderer.end();
        disableBlend();

        float panelW = 700f;
        float panelH = 420f;
        float panelX = cx - panelW / 2f;
        float panelY = sh / 2f - panelH / 2f + 30f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COL_PANEL);
        shapeRenderer.rect(panelX, panelY, panelW, panelH);
        shapeRenderer.end();

        drawKey(wRect,"W",wPressed);
        drawKey(aRect,"A",aPressed);
        drawKey(sRect,"S",sPressed);
        drawKey(dRect,"D",dPressed);
        drawKey(eRect,"E",ePressed);
        drawKey(fRect,"F",fPressed);

        fontBatch.begin();

        layout.setText(titleFont,"HOW TO PLAY");
        titleFont.draw(fontBatch,layout,cx-layout.width/2f,panelY+panelH-20f);

        float wasdCenterX = wRect.x + KEY_SIZE / 2f + 5f;
        drawCentredDesc("Movement", wasdCenterX, wRect.y + KEY_SIZE + 40f);

        float descX = eRect.x + KEY_SIZE + PADDING;

        layout.setText(descFont,"Pick up / Put down item");
        descFont.draw(fontBatch,"Pick up / Put down item",
                descX,
                eRect.y + KEY_SIZE/2f + layout.height/3f);

        layout.setText(descFont,"Chop ingredient");
        descFont.draw(fontBatch,"Chop ingredient",
                descX,
                fRect.y + KEY_SIZE/2f + layout.height/2f);

        fontBatch.end();

        super.render();
    }

    private void drawKey(Rectangle r, String label, boolean pressed) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pressed ? COL_KEY_PRESSED : COL_KEY_NORMAL);
        shapeRenderer.rect(r.x, r.y, r.width, r.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(COL_KEY_BORDER);
        shapeRenderer.rect(r.x, r.y, r.width, r.height);
        shapeRenderer.end();

        fontBatch.begin();
        layout.setText(keyFont,label);
        keyFont.setColor(pressed ? Color.WHITE : COL_KEY_TEXT);
        keyFont.draw(fontBatch,layout,
                r.x + (r.width-layout.width)/2f,
                r.y + (r.height+layout.height)/2f);
        fontBatch.end();
    }

    private void drawCentredDesc(String text, float cx, float y) {
        layout.setText(descFont,text);
        descFont.draw(fontBatch,layout,cx-layout.width/2f,y);
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