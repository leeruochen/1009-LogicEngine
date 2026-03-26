package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import github.com_1009project.abstractEngine.EntityRegistry;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.InputManager;
import github.com_1009project.abstractEngine.Scene;
import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;

public class SettingsScene extends Scene {

    private Skin skin;
    private Preferences prefs;

    public SettingsScene(int id, AssetManager assetManager, EntityRegistry entityRegistry, EventManager eventManager, InputManager inputManager, SpriteBatch batch, SceneManager sceneManager) {
        super(id, assetManager, entityRegistry, eventManager, inputManager, batch, sceneManager);
        init();
    }

    @Override
    public void init() {
        UILayer uiLayer = new UILayer(batch);
        layers.add(uiLayer);
        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        prefs = Gdx.app.getPreferences("GameSettings");

        Table table = new Table();
        table.setFillParent(true);
        uiLayer.getStage().addActor(table);

        table.add(new Label("SETTINGS", skin)).colspan(2).padBottom(50).row();
        table.add(new Label("Music Volume:", skin)).padRight(20);
        
        Slider musicSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicSlider.setValue(prefs.getFloat("musicVolume", 1.0f));
        
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putFloat("musicVolume", musicSlider.getValue());
                prefs.flush(); 
                eventManager.eventTrigger(Event.SettingsChanged);
            }
        });
        table.add(musicSlider).width(300).row();
        table.add(new Label("SFX Volume:", skin)).padRight(20).padTop(30);
        
        Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);
        sfxSlider.setValue(prefs.getFloat("soundVolume", 1.0f));
        
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putFloat("soundVolume", sfxSlider.getValue());
                prefs.flush();
                eventManager.eventTrigger(Event.SettingsChanged);
            }
        });
        table.add(sfxSlider).width(300).padTop(30).row();

        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneManager.loadPreviousScene();
            }
        });
        table.add(backBtn).colspan(2).padTop(60).width(200).height(50);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
    }
}