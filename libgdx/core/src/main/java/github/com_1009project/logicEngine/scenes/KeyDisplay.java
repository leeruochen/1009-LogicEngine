package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class KeyDisplay implements Disposable {

    // Layout
    public static final float KEY_SIZE = 55f;
    public static final float KEY_GAP  = 8f;

    // Colours
    private static final Color COL_KEY_NORMAL  = new Color(0.25f, 0.14f, 0.07f, 1.00f);
    private static final Color COL_KEY_PRESSED = new Color(0.90f, 0.60f, 0.15f, 1.00f);
    private static final Color COL_KEY_BORDER  = new Color(0.75f, 0.50f, 0.20f, 1.00f);
    private static final Color COL_KEY_TEXT    = new Color(1.00f, 0.92f, 0.70f, 1.00f);

    // State
    private final String    label;       
    private final int       keycode;     
    private final Rectangle bounds;
    private final String    description; 
    private boolean         pressed;

    // Rendering tools 
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch   fontBatch;
    private final BitmapFont    keyFont;
    private final GlyphLayout   layout;

    public KeyDisplay(String label, int keycode, float x, float y,
                      String description,
                      ShapeRenderer shapeRenderer, SpriteBatch fontBatch, BitmapFont keyFont) {
        this.label        = label;
        this.keycode      = keycode;
        this.bounds       = new Rectangle(x, y, KEY_SIZE, KEY_SIZE);
        this.description  = description;
        this.shapeRenderer = shapeRenderer;
        this.fontBatch    = fontBatch;
        this.keyFont      = keyFont;
        this.layout       = new GlyphLayout();
    }

    // Update
    public void update() {
        pressed = Gdx.input.isKeyPressed(keycode);
    }

    // Render
    public void render() {
        // fill
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pressed ? COL_KEY_PRESSED : COL_KEY_NORMAL);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(COL_KEY_BORDER);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // label text
        fontBatch.begin();
        layout.setText(keyFont, label);
        keyFont.setColor(pressed ? Color.WHITE : COL_KEY_TEXT);
        keyFont.draw(fontBatch, layout,
                bounds.x + (bounds.width  - layout.width)  / 2f,
                bounds.y + (bounds.height + layout.height) / 2f);
        fontBatch.end();
    }

    // Getters
    public Rectangle getBounds()    { return bounds; }
    public String    getDescription() { return description; }
    public boolean   isPressed()    { return pressed; }

    @Override
    public void dispose() {
        
    }
}