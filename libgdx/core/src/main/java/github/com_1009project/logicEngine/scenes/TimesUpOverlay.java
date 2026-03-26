package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import github.com_1009project.abstractEngine.UILayer;

public class TimesUpOverlay {

    private final Label label;

    public TimesUpOverlay(UILayer uiLayer, Skin skin) {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        label = new Label("TIME'S UP!", skin, "default");
        label.setFontScale(5.0f);
        label.setColor(Color.RED);
        label.layout();
        label.setPosition((sw - label.getPrefWidth()) / 2f, sh / 2f);
        label.setVisible(false);

        uiLayer.getStage().addActor(label);
    }

    public void show() { label.setVisible(true);  }
    public void hide() { label.setVisible(false); }
}