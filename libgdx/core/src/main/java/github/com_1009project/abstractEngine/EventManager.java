package github.com_1009project.abstractEngine;

// The EventManager class is responsible for managing events in the game. It allows different systems and components to communicate with each other by sending and receiving events.
// The EventManager maintains a list of observers that are interested in receiving events. When an event is triggered, the EventManager notifies all registered observers by calling their onNotify method with the relevant event information.

import com.badlogic.gdx.utils.Array;

public class EventManager {
    private Array<EventObserver> observers;

    public EventManager() {
        observers = new Array<EventObserver>();
    }

    private void notifyObservers(Event event, Boolean up) {
        for (int i = 0; i < observers.size; i++) {
            observers.get(i).onNotify(event, up);
        }
    }

    private void notifyObservers(Event event, Boolean up, int screenX, int screenY) {
        for (int i = 0; i < observers.size; i++) {
            observers.get(i).onNotify(event, up, screenX, screenY);
        }
    }

    public void eventTrigger(Event event) {
        notifyObservers(event, null);
    }

    public void eventTrigger(Event event, boolean up) {
        notifyObservers(event, up);
    }

    public void eventTrigger(Event event, boolean up, int screenX, int screenY) {
        notifyObservers(event, up, screenX, screenY);
    }

    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer) {
        observers.removeValue(observer, true);
    }

    public void dispose() {
        observers.clear();
    }
}