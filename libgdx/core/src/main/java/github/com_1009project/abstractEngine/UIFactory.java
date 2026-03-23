package github.com_1009project.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIFactory {

    private final Skin         skin;
    private final UILayer      uiLayer;
    private final EventManager eventManager;

    public UIFactory(UILayer uiLayer, Skin skin, EventManager eventManager) {
        this.skin         = skin;
        this.uiLayer      = uiLayer;
        this.eventManager = eventManager;
    }

    public TextButton createButton(String text, Event event, float x, float y) {
        TextButton btn = new TextButton(text, skin);
        btn.setPosition(x, y);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                eventManager.eventTrigger(event);
            }
        });
        uiLayer.addActor(btn);
        return btn;
    }
    public Label createLabel(String text, float x, float y) {
        return createLabel(text, "default", x, y, 1.0f);
    }

    public Label createLabel(String text, String style, float x, float y, float fontScale) {
        Label label = new Label(text, skin, style);
        label.setFontScale(fontScale);
        label.setPosition(x, y);
        uiLayer.addActor(label);
        return label;
    }

    public ProgressBar createResourceBar(float x, float y, boolean vertical) {
        ProgressBar bar = new ProgressBar(0, 100, 1, vertical, skin);
        bar.setPosition(x, y);
        uiLayer.addActor(bar);
        return bar;
    }
    
    public Label createTimerLabel(float totalTime, float x, float y) {
        int m = (int) (totalTime / 60);
        int s = (int) (totalTime % 60);
        Label label = createLabel(String.format("%02d:%02d", m, s), "warm", x, y, 3.0f);
        label.setWidth(200f);
        label.setAlignment(com.badlogic.gdx.utils.Align.center);
        return label;
    }

    public ProgressBar createTimerBar(float totalTime) {
        float sw = Gdx.graphics.getWidth();
        ProgressBar bar = new ProgressBar(0f, totalTime, 0.1f, false, skin, "timer-horizontal");
        bar.setValue(totalTime);
        bar.setSize(sw, 30f); // taller
        bar.setPosition(0, 5f); // slightly above bottom edge
        uiLayer.addActor(bar);
        return bar;
    }

    public Table createCardTable(float x, float y, float w, float h, Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
 
        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(tex)));
        table.setSize(w, h);
        table.setPosition(x, y);
        table.top().pad(6f);
        uiLayer.addActor(table);
        return table;
    }

    public Image wrapTexture(Texture texture) {
        return new Image(new TextureRegionDrawable(new TextureRegion(texture)));
    }
}