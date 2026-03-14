package github.com_1009project.abstractEngine;

public interface EventObserver {
    public void onNotify(Event event, Boolean up);
    public void onNotify(Event event, Boolean up, int screenX, int screenY);
}
