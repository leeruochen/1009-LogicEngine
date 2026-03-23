package github.com_1009project.abstractEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    public ProgressBar createResourceBar(float x, float y, boolean vertical) {
        ProgressBar bar = new ProgressBar(0, 100, 1, vertical, skin);
        bar.setPosition(x, y);
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