package github.com_1009project.logicEngine;

import github.com_1009project.abstractEngine.Entity;

public class Door extends CollisionBox {
    private String destination;

    public Door(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.destination = "";
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public void onCollision(Entity other) {
    }
}
