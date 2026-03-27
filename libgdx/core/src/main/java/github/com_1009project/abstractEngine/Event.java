package github.com_1009project.abstractEngine;

// The Event enum defines a set of events that can occur in the game. These events can be used for communication between different systems and components in the game engine.

public enum Event {
    PlayerRight,
    PlayerLeft,
    PlayerUp,
    PlayerDown,
    PlayerInteract,
    PlayerChop,
    GamePause,
    GameStart,
    MenuEnter,
    SubmissionCorrect,
    SubmissionWrong,
    IngredientTake,
    Chopping,
    PlayerInteractSound,
    Bin,
    SettingsChanged,
}
