# OverBaked

**OverBaked** is a fast-paced fast-food kitchen simulation game built on a custom 2D game framework powered by **Java** and **LibGDX**. 

This project was developed for the INF1009 Object-Oriented Programming module. The architecture is strictly decoupled into two distinct layers: the **Abstract Engine** (a robust, reusable backend framework) and the **Logic Engine** (the game-specific implementation).

## The Game: OverBaked (Logic Engine)
OverBaked simulates the dynamic and high-pressure environment of a fast-food restaurant kitchen. Players must manage time, prioritize tasks, and maintain efficiency under pressure to fulfill customer orders.

### Gameplay Features
- **Cooking Mechanics:** Prepare ingredients by moving them between stations:
  - **Chopping Station:** Turns raw ingredients (Lettuce, Tomato, Cheese) into chopped ingredients.
  - **Stove:** Cooks raw patties (features a cooking timer: `Raw` → `Cooked` → `Overcooked`).
  - **Counter & Plate:** Assemble ingredients and submit them to match active orders.
- **Dynamic Recipe System:** Order-independent ingredient validation for various recipes (Classic Burger, Double Patty, Green Double).
- **Player Interactions:** 80-unit interaction radius, single-item holding capacity, and animated states (Idle, Running, Chopping).
- **Time Management:** Fulfill randomized food orders within a specific timeframe to score points.

---

## The Framework (Abstract Engine)
The underlying engine was built with a strict adherence to **SOLID Principles** and **Gang of Four (GoF) Design Patterns** (Factory, Facade, Flyweight, Observer), completely eliminating "God Classes" and hidden dependencies.

### Engine Features
- **Entity Component System (ECS) & Interface Segregation:** - Entities are lightweight and composition-based, opting into behaviors via interfaces (`IUpdatable`, `IRenderable`, `ICollidable`, `Moveable`).
  - Strict separation between `DynamicEntity` (moving objects) and static entities to optimize memory.
- **Decoupled Entity Management:**
  - `EntityRegistry`: Strictly handles data storage, memory, and lifecycle/garbage collection.
  - `EntityRenderer`: Dedicated solely to y-sorted visual rendering.
  - `MapEntityLoader`: Parses Tiled Maps (TMX) to instantiate world objects dynamically.
- **Scene & State Management:** - Dynamic UI routing via a navigation stack (breadcrumb trail) allowing safe backward transitions.
  - Handled by a dedicated `SceneManager` decoupled from specific game logic.
- **Robust Managers:**
  - `EventManager` & `InputManager`: A decoupled publish/subscribe event bus.
  - `CollisionManager`: Spatial collision detection.
  - `MovementManager`: Velocity and physics handling.
  - `OutputManager`: Persistent settings management (e.g., real-time audio adjustments).
- **Dependency Injection:** A centralized `GameContext` safely passes global managers to scenes and factories, eradicating the Singleton anti-pattern.

---

## Project Structure

```text
1009-project/
├── README.md                     # This overview file
├── core/                         # Source code 
│   ├── src/main/java/github/com_1009project   
│       ├── abstractEngine        # Abstract Engine files (Core Framework)
│       ├── logicEngine           # Logic Engine files (OverBaked Game)
│       ├── GameMaster.java       # Main game lifecycle

```

to run

```bash

git clone https://github.com/leeruochen/1009-LogicEngine
cd 1009-LogicEngine
./gradlew lwjgl3:run

or however you run gradle builds

```