package github.com_1009project.abstractEngine;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputProcessor;

public class UILayer extends Layer {
    private Stage stage;
    private SpriteBatch batch;

    public UILayer(SpriteBatch batch) {
        this.batch = batch;
        this.stage = new Stage(new ExtendViewport(1280, 720), batch);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    @Override
    public void render() {
       stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void addActor(Actor actor){
        stage.addActor(actor);
    }
    // optional can remove if not needed
    public Stage getStage() {
        return stage;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }
}
