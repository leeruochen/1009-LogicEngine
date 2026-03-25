package github.com_1009project.logicEngine.factories;

import github.com_1009project.abstractEngine.Ifactory;

import github.com_1009project.logicEngine.entities.CollisionBox;

public class CollisionBoxFactory implements Ifactory<CollisionBox> {

    @Override
    public CollisionBox createEntity(float x, float y, float width, float height) {
        // Create and return a new CollisionBox entity with default properties
        return new CollisionBox(x, y, width, height);
    }

}