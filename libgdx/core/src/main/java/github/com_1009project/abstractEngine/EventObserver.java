package github.com_1009project.abstractEngine;

// The EventObserver interface defines the contract for any class that wants to receive event notifications from the EventManager.

public interface EventObserver {
    public void onNotify(Event event, Boolean up);
    public void onNotify(Event event, Boolean up, int screenX, int screenY);
}
