package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
    private HashMap<Integer, Event> keyMappings;
    private Set<Integer> pressedKeys;
    private EventManager eventManager;

    public InputManager(EventManager eventManager) {
        this.eventManager = eventManager;
        keyMappings = new HashMap<Integer, Event>();
        pressedKeys = new HashSet<Integer>();
    }

    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(keycode);
        if (keyMappings.containsKey(keycode)) {
            eventManager.eventTrigger(keyMappings.get(keycode), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (pressedKeys.contains(keycode)) {
            return false;
        }
        pressedKeys.add(keycode);

        if (keyMappings.containsKey(keycode)) {
            eventManager.eventTrigger(keyMappings.get(keycode), false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (keyMappings.containsKey(button)) {
            eventManager.eventTrigger(keyMappings.get(button), false, screenX, screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (keyMappings.containsKey(button)) {
            eventManager.eventTrigger(keyMappings.get(button), true, screenX, screenY);
            return true;
        }
        return false;
    }

    public void mapKey(int keycode, Event event) {
        keyMappings.put(keycode, event);
    }

    public void dispose() {
        keyMappings.clear();
        pressedKeys.clear();
    }
}