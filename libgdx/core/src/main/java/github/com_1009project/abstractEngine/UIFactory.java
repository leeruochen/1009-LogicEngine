package github.com_1009project.abstractEngine;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UIFactory {
    private EventManager eventManager;
    private UILayer uiLayer;
    private Skin skin;

    public UIFactory(UILayer uiLayer, Skin skin, EventManager eventManager) {
        this.skin = skin;
        this.uiLayer = uiLayer;
        this.eventManager = eventManager;
    }

    public TextButton createButton(String text, Event event, float x, float y) {
        // Create button using the "default" style in the Skin
        TextButton btn = new TextButton(text, skin);
        btn.setPosition(x, y);
        // Add interaction logic
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                eventManager.eventTrigger(event);
            }
        });
        uiLayer.addActor(btn);
        
        return btn;
    }

    public ProgressBar createResourceBar(float x, float y, boolean vertical){
        ProgressBar resourceBar = new ProgressBar(0, 100, 1, vertical, skin);
        resourceBar.setPosition(x, y);
        uiLayer.addActor(resourceBar);
        
        return resourceBar;
    }
}
