package github.com_1009project.logicEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.*;

public class TutorialScene extends Scene {

    // Background animation
    private Animation<TextureRegion> bgAnimation;
    private float bgStateTime;

    // Rendering
    private ShapeRenderer shapeRenderer;
    private SpriteBatch fontBatch;
    private BitmapFont titleFont, keyFont, descFont;
    private GlyphLayout layout;
    private Skin skin;

    // Keybaord state
    private static final float KEY_SIZE = 55f, KEY_GAP = 8f, PADDING = 20f;
    private Rectangle[] keyRects = new Rectangle[6];
    private boolean[] keyPressed = new boolean[6];
    private final int W=0,A=1,S=2,D=3,E=4,F=5; // Indexes for keys in keyRects and keyPressed arrays

    // Colors
    private static final Color COL_PANEL       = new Color(0.35f, 0.20f, 0.10f, 0.95f);
    private static final Color COL_BORDER      = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_CREAM       = new Color(1.00f, 0.92f, 0.70f, 1.00f);
    private static final Color COL_KEY_NORMAL  = new Color(0.25f, 0.14f, 0.07f, 1.00f);
    private static final Color COL_KEY_PRESSED = new Color(0.90f, 0.60f, 0.15f, 1.00f);
    private static final Color COL_KEY_BORDER  = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_KEY_TEXT    = new Color(1.00f, 0.92f, 0.70f, 1.00f);

    public TutorialScene(int id, AssetManager assetManager, EntityManager entityManager, EventManager eventManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityManager, eventManager, batch, sceneManager);
        init();
    }

    @Override
    public void init() {
        // Initialize background layer with AssetManager
        BackgroundLayer bgLayer = new BackgroundLayer(batch, assetManager, "imgs/bg_frames", 52, 0.1f);
        layers.add(bgLayer);
        // Fonts and Renderer
        shapeRenderer = new ShapeRenderer();
        fontBatch     = new SpriteBatch();
        layout        = new GlyphLayout();

        titleFont = new BitmapFont(); titleFont.getData().setScale(2.4f); titleFont.setColor(COL_CREAM);
        keyFont   = new BitmapFont(); keyFont.getData().setScale(1.6f);
        descFont  = new BitmapFont(); descFont.getData().setScale(1.3f); descFont.setColor(COL_CREAM);

        // UI Layer and Skin
        UILayer uiLayer = new UILayer(batch); layers.add(uiLayer);
        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));

        float sw = Gdx.graphics.getWidth(), cx = sw/2f, btnY = Gdx.graphics.getHeight()*0.06f;

        // Buttons
        TextButton startBtn = new TextButton("Start Game", skin, "warm-resume");
        TextButton backBtn  = new TextButton("Back", skin, "warm-quit");
        startBtn.setSize(220f,55f); backBtn.setSize(120f,55f);

        float spacing = 20f, offsetX = 100f;
        startBtn.setPosition(cx - startBtn.getWidth()/2f + offsetX, btnY);
        backBtn.setPosition(cx - startBtn.getWidth()/2f - backBtn.getWidth() - spacing + offsetX, btnY);

        startBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e,float x,float y){ sceneManager.loadScene(1); }});
        backBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e,float x,float y){ sceneManager.loadScene(0); }});

        uiLayer.getStage().addActor(startBtn);
        uiLayer.getStage().addActor(backBtn);

        buildKeyRects();
    }

    private void buildKeyRects() {
        // Position keys in a cluster on the left side of the screen
        float sw = Gdx.graphics.getWidth(), sh = Gdx.graphics.getHeight(), cx = sw/2f;
        float clusterX = cx-260f, clusterY = sh/2f-20f;
        keyRects[W] = new Rectangle(clusterX+KEY_SIZE+KEY_GAP, clusterY+KEY_SIZE+KEY_GAP, KEY_SIZE, KEY_SIZE);
        keyRects[A] = new Rectangle(clusterX, clusterY, KEY_SIZE, KEY_SIZE);
        keyRects[S] = new Rectangle(clusterX+KEY_SIZE+KEY_GAP, clusterY, KEY_SIZE, KEY_SIZE);
        keyRects[D] = new Rectangle(clusterX+2*(KEY_SIZE+KEY_GAP), clusterY, KEY_SIZE, KEY_SIZE);
        float actionX = cx+60f;
        keyRects[E] = new Rectangle(actionX, clusterY+KEY_SIZE+KEY_GAP, KEY_SIZE, KEY_SIZE);
        keyRects[F] = new Rectangle(actionX, clusterY-KEY_SIZE-2f, KEY_SIZE, KEY_SIZE);
    }

    @Override
    public void update(float delta) {
        super.update(delta); // Update background animation time and key states
        int[] keys = {Input.Keys.W,Input.Keys.A,Input.Keys.S,Input.Keys.D,Input.Keys.E,Input.Keys.F};
        for(int i=0;i<keys.length;i++) 
            keyPressed[i] = Gdx.input.isKeyPressed(keys[i]);
    }

    @Override
    public void render() {
        float sw = Gdx.graphics.getWidth(); 
        float sh = Gdx.graphics.getHeight();
        float cx = sw/2f;

        // Render all layers first
        for (Layer layer : layers) {
            if (!(layer instanceof UILayer)) {   // Skip UI for now
                layer.update(Gdx.graphics.getDeltaTime());
                layer.render();
            }
        }

        // Dark overlay
        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,0.55f);
        shapeRenderer.rect(0,0,sw,sh);
        shapeRenderer.end();
        disableBlend();

        // Panel
        float panelW=700f,panelH=420f,panelX=cx-panelW/2f,panelY=sh/2f-panelH/2f+30f;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COL_PANEL);
        shapeRenderer.rect(panelX,panelY,panelW,panelH);
        shapeRenderer.end();

        // Draw keys
        for(int i=0;i<keyRects.length;i++)
            drawKey(keyRects[i],"WASDEF".charAt(i)+"", keyPressed[i]);

        // Draw text
        fontBatch.begin();
        layout.setText(titleFont,"HOW TO PLAY");
        titleFont.draw(fontBatch,layout,cx-layout.width/2f,panelY+panelH-20f);

        float wasdCenterX = keyRects[W].x + KEY_SIZE/2f + 5f;
        drawCentredDesc("Movement",wasdCenterX,keyRects[W].y+KEY_SIZE+40f);

        float descX = keyRects[E].x + KEY_SIZE + PADDING;
        layout.setText(descFont,"Pick up / Put down item");
        descFont.draw(fontBatch,"Pick up / Put down item",descX,keyRects[E].y+KEY_SIZE/2f+layout.height/3f);
        layout.setText(descFont,"Chop ingredient"); 
        descFont.draw(fontBatch,"Chop ingredient",descX,keyRects[F].y+KEY_SIZE/2f+layout.height/2f);

        fontBatch.end();

        // Render UI last
        for (Layer layer : layers) {
            if (layer instanceof UILayer) {
                layer.update(Gdx.graphics.getDeltaTime());
                layer.render(); // calls stage.act() + stage.draw()
            }
        }
    }
    // Helper methods for drawing keys and text
    private void drawKey(Rectangle r,String label,boolean pressed){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pressed?COL_KEY_PRESSED:COL_KEY_NORMAL);
        shapeRenderer.rect(r.x,r.y,r.width,r.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(COL_KEY_BORDER);
        shapeRenderer.rect(r.x,r.y,r.width,r.height);
        shapeRenderer.end();

        fontBatch.begin();
        layout.setText(keyFont,label);
        keyFont.setColor(pressed?Color.WHITE:COL_KEY_TEXT);
        keyFont.draw(fontBatch,layout,r.x+(r.width-layout.width)/2f,r.y+(r.height+layout.height)/2f);
        fontBatch.end();
    }
    // Draw centered description text
    private void drawCentredDesc(String text,float cx,float y){
        layout.setText(descFont,text);
        descFont.draw(fontBatch,layout,cx-layout.width/2f,y);
    }
    // Helper methods for blending
    private void enableBlend(){ Gdx.gl.glEnable(GL20.GL_BLEND); Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);}
    private void disableBlend(){ Gdx.gl.glDisable(GL20.GL_BLEND);}

    @Override
    public void dispose(){
        super.dispose();
        shapeRenderer.dispose();
        fontBatch.dispose();
        titleFont.dispose();
        keyFont.dispose();
        descFont.dispose();
        skin.dispose();
    }
}