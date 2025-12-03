# Gravitrips
A JavaFX Connect Four–style game built with Gradle and Java 21, featuring pluggable AI strategies and a simple MVC architecture.

## Features
- 7x6 Connect Four board with configurable dimensions and win length.
- Human vs Human and Human vs Computer modes with background AI turns.
- Plugin-ready AI system; built-in random strategy plus support for external strategies in `ai_players/`.
- JavaFX UI with column click-to-drop, menus for New Game/Exit, and end-of-game dialogs.
- JUnit 5 test coverage for board win/draw detection and AI move validity.

## How to Run
1. Clone or download:
   ```sh
   git clone <repo-url> && cd gravitrips2
   ```
2. Use the Gradle wrapper (preferred):
   ```sh
   ./gradlew run
   ```
   This downloads dependencies and launches the JavaFX app.
3. Run tests:
   ```sh
   ./gradlew test
   ```
4. Install locally (optional): use `./gradlew installDist` to create a distributable under `build/install/gravitrips` with launch scripts.
5. Java requirement: JDK 21 on your PATH. If Gradle cannot find Java, set `JAVA_HOME` to your JDK 21 path.

## About
Connect Four (aka Gravitrips in some regions) is a two-player connection game where players drop colored discs into a vertical grid, aiming to align four in a row horizontally, vertically, or diagonally. Commercial versions include Hasbro’s classic Connect Four; variants span different board sizes, pop-out mechanics, and digital adaptations with AI opponents.

## Creating Your Own AI Player
1. Implement `ca.bgiroux.gravitrips.model.MoveStrategy` with a public no-arg constructor and a unique `getName()`:
   ```java
   public class CenterFirstStrategy implements MoveStrategy {
       @Override public String getName() { return "Center First"; }
       @Override public int chooseMove(Board board, int playerId) { return board.getColumns() / 2; }
   }
   ```
2. Compile your class or package it into a `.jar`.
3. Place the compiled `.class` or `.jar` in the `ai_players/` folder beside the app.
4. Restart the app. New strategies appear in the New Game dialog’s strategy dropdown.

## Future Improvements
- Richer AI strategies (minimax/Monte Carlo) and difficulty settings.
- Better UI polish: animations, responsive layout, accessibility tweaks.
- Persisted settings and score tracking.
- Additional game modes (e.g., different board sizes or connect lengths selectable in the UI).
