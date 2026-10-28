# Connect Four

CSCI 4448/5448 project – Pablo Roig

Connect Four in Java. Human vs human or human vs computer (easy, medium, hard), with undo and move history. Right now it runs in the console; the Swing GUI and saving games are next.

## Run

```
./gradlew run
./gradlew test
```

On Windows use `gradlew.bat`. In the game type a column (1-7), `u` to undo, `h` for history or `q` to quit.

## Design patterns

- **Strategy** – `MoveStrategy` with `RandomStrategy` (easy), `GreedyStrategy` (medium) and `MinimaxStrategy` (hard). `ComputerPlayer` just calls the strategy it was given.
- **Command** – each move is a `DropDiscCommand` with `execute()`/`undo()`, stored in `CommandHistory`. Used for undo and the move history.
- **Observer** – `Game` notifies `GameListener`s when a move is played or undone and when the game ends. `ConsoleView` is a listener, so the model doesn't depend on the UI (the GUI will be another listener).
- **Factory Method** – each `Difficulty` creates its own strategy with `createStrategy()`, so there's no switch on the difficulty.

## OO principles

- Classes depend on interfaces: `Player`, `MoveStrategy`, `WinDetector`, `HumanInput`, `GameListener`.
- Polymorphism: the game loop calls `takeTurn()` on the current player without checking if it's a human or the computer.
- Dependency injection through constructors (`Game`, `ComputerPlayer`, `HumanPlayer`, strategies). Objects are wired in `Main`.

## Tests

JUnit 5 tests in `src/test`: board rules, horizontal/vertical/diagonal wins, draw, turns, undo, listeners and the AI (takes a win, blocks a win).
