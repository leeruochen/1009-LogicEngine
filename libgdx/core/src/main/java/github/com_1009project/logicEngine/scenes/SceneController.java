package github.com_1009project.logicEngine.scenes;

import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventManager;
import github.com_1009project.abstractEngine.EventObserver;
import github.com_1009project.abstractEngine.SceneManager;

public class SceneController implements EventObserver {
    private SceneManager sceneManager;

    public SceneController(SceneManager sceneManager, EventManager eventManager) {
        this.sceneManager = sceneManager;
        eventManager.addObserver(this);
    }

    @Override
    public void onNotify(Event event, Boolean up) {
        if (up == null) return;

        if (event == Event.GamePause && !up) {
            sceneManager.loadScene(99);
        }
    }

    @Override
    public void onNotify(Event event, Boolean up, int x, int y) {}

    public void dispose(EventManager eventManager) {
        eventManager.removeObserver(this);
    }
}