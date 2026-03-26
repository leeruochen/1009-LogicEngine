package github.com_1009project.logicEngine.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import github.com_1009project.abstractEngine.SceneManager;
import github.com_1009project.abstractEngine.UILayer;

public class ResultsOverlay {

    private static final int MAX_STARS = 3;

    private final UILayer      uiLayer;
    private final Skin         skin;
    private final SceneManager sceneManager;
    private boolean            shown = false;

    public ResultsOverlay(UILayer uiLayer, Skin skin, SceneManager sceneManager) {
        this.uiLayer      = uiLayer;
        this.skin         = skin;
        this.sceneManager = sceneManager;
    }

    /**
     * @param score number of dishes served (used for star rating)
     */
    public void show(int score) {
        if (shown) return;
        shown = true;

        // Star count
        int starCount;
        if      (score >= 10) starCount = 3;
        else if (score >= 5)  starCount = 2;
        else if (score >= 1)  starCount = 1;
        else                  starCount = 0;

        // Labels
        Label titleLabel = new Label("ROUND COMPLETE", skin, "default");
        titleLabel.setFontScale(2.5f);
        titleLabel.setColor(Color.GOLD);

        Label scoreLabel = new Label("Dishes Served: " + score, skin);
        scoreLabel.setFontScale(1.5f);

        Label ratingLabel = new Label("Rating: " + starCount + " / " + MAX_STARS + " stars", skin);
        ratingLabel.setFontScale(1.5f);
        ratingLabel.setColor(starCount == MAX_STARS ? Color.GOLD : Color.WHITE);

        // Buttons
        TextButton retryBtn = new TextButton("Retry", skin, "warm-resume");
        retryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.loadScene(1);
            }
        });

        TextButton settingsBtn = new TextButton("Settings", skin, "warm-resume");
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.loadScene(2);
            }
        });

        TextButton exitBtn = new TextButton("Main Menu", skin, "warm-quit");
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.unloadScene(1);
                sceneManager.loadScene(0);
            }
        });

        // Layout 
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel ).padBottom(30).row();
        table.add(scoreLabel ).padBottom(10).row();
        table.add(ratingLabel).padBottom(40).row();
        table.add(retryBtn   ).width(250).height(60).padBottom(10).row();
        table.add(settingsBtn).width(250).height(60).padBottom(10).row();
        table.add(exitBtn    ).width(250).height(60).row();

        uiLayer.getStage().addActor(table);

        // Claim input immediately so buttons are clickable right away
        restoreInputProcessor();
    }

    public void restoreInputProcessor() {
        Gdx.input.setInputProcessor(uiLayer.getStage());
    }

    public boolean isShown() { return shown; }
}
