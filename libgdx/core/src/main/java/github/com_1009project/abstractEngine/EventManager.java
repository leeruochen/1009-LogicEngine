package github.com_1009project.abstractEngine;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

public class EventManager extends InputAdapter{
    private HashMap<Integer, Event> keyMappings;
    private HashMap<Integer, Event> mouseMappings;
    private Array<EventObserver> observers;
    private Set<Integer> pressedKeys;

    private void notifyObservers(Event event, Boolean up){
        for (int i = 0; i < observers.size; i++){
            observers.get(i).onNotify(event, up);
        }
    }
    private void notifyObservers(Event event, Boolean up, int screenX, int screenY){
        for (int i = 0; i < observers.size; i++){
            observers.get(i).onNotify(event, up, screenX, screenY);
        }
    }

    public EventManager(){
        observers = new Array<EventObserver>();
        keyMappings = new HashMap<Integer, Event>();
        pressedKeys = new HashSet<Integer>();
    }
    
    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(keycode);
        if (keyMappings.containsKey(keycode)){
            notifyObservers(keyMappings.get(keycode), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (pressedKeys.contains(keycode)){
            return false;
        }
        pressedKeys.add(keycode);

        if (keyMappings.containsKey(keycode)){
            notifyObservers(keyMappings.get(keycode), false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        if (keyMappings.containsKey(button)){
            notifyObservers(keyMappings.get(button), false, screenX, screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        if (keyMappings.containsKey(button)){
            notifyObservers(keyMappings.get(button), true, screenX, screenY);
            return true;
        }
        return false;
    }

    public void eventTrigger(Event event){
        notifyObservers(event, null);
    }

    public void mapKey(int keycode, Event event){
        keyMappings.put(keycode, event);
    }

    public void addObserver(EventObserver observer){
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer){
        observers.removeValue(observer, true);
    }

    public void dispose(){
        observers.clear();
        keyMappings.clear();
        pressedKeys.clear();
    }
}
