package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.abstractEngine.UILayer;

public class RoundTimer {

    private final float duration;
    private float timeRemaining;
    private boolean expired = false;

    private final Label      timerLabel;
    private final ProgressBar timerBar;

    public RoundTimer(float duration, UILayer uiLayer, Skin skin, EventManager eventManager) {
        this.duration      = duration;
        this.timeRemaining = duration;

        float sw = Gdx.graphics.getWidth();
        UIFactory factory = new UIFactory(uiLayer, skin, eventManager);
        timerLabel = factory.createTimerLabel(duration, sw / 2f - 100f, 50f);
        timerBar   = factory.createTimerBar(duration);
    }

    public void update(float delta) {
        if (expired) return;

        timeRemaining -= delta;
        if (timeRemaining <= 0f) {
            timeRemaining = 0f;
            expired = true;
        }

        timerLabel.setText(formatTime(timeRemaining));
        timerLabel.setColor(timeRemaining <= 30f ? Color.RED : Color.WHITE);
        timerBar.setValue(timeRemaining);
    }

    public boolean isExpired()        { return expired; }
    public float   getTimeRemaining() { return timeRemaining; }
    public float   getDuration()      { return duration; }

    // Private helpers
    private String formatTime(float seconds) {
        int m = (int) (seconds / 60);
        int s = (int) (seconds % 60);
        return String.format("%02d:%02d", m, s);
    }
}