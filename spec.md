# Gravitrips Specification

## Overview

Gravitrips is a JavaFX desktop implementation of Connect Four
(7x6 by default) with MVC separation and pluggable AI strategies.
Built with Gradle (Kotlin DSL) on Java 21 (classpath only, no
module-info). Distribution scripts embed JavaFX module path flags
for offline/local installs.

## Purpose & Goals

- Provide a playable Connect Four game with Human vs Human and
  Human vs Computer modes.
- Keep game rules/UI decoupled (MVC).
- Support AI plugins loaded from an ai_players/ folder at
  runtime.
- Deliver test coverage for board logic and AI validity.

## Requirements

- Java 21 toolchain (classpath; no modules).
- Gradle 8.x with wrapper; JavaFX runtime bundled via platform-
  specific artifacts.
- JavaFX modules: controls, fxml (graphics/base transitively via
  platform jars).
- JUnit 5 for tests.
- No global mutable singletons; avoid magic numbers (use
  GameConfig).

## Architecture

- Model (pure Java): Game rules, board state, AI interfaces. No
  JavaFX imports.
- View (JavaFX): Renders board, emits column click events, shows
  dialogs.
- Controller: Mediates UI events, applies moves, runs AI on
  background thread, updates view, handles game end dialogs.
- AI Plugin Loader: Scans ai_players/ for .class or .jar
  implementing MoveStrategy with a public no-arg constructor;
  loads via URLClassLoader.
- Logging: java.util.logging for plugin loading and game
  outcomes.

## Features

- Configurable board (default 7x6, connect 4).
- Turn-based piece drops with gravity; invalid move handling for
  full columns.
- Win detection: horizontal, vertical, diagonal.
- Draw detection on full board.
- Game modes: Human vs Human, Human vs Computer.
- Built-in AI: RandomMoveStrategy (chooses a random available
  column).
- New Game dialog with mode and AI selection.
- End-of-game dialogs (win/draw).
- JavaFX UI: colored discs (Red/Yellow), clickable columns, menu
  (New Game, Exit).

## Constraints & Design Patterns

- MVC separation enforced: model contains logic; view contains
  only rendering/event emission; controller coordinates and runs
  AI.
- Model immutable configs, minimal shared mutable state.
- AI runs off the JavaFX Application Thread (single-threaded
  executor, results posted via Platform.runLater).
- No Java modules; all classpath-based.

## Directory Layout

project-root/
├─ build.gradle.kts
├─ settings.gradle.kts
├─ gradlew / gradlew.bat / gradle/wrapper/
├─ src/
│  ├─ main/java/ca/bgiroux/gravitrips/
│  │  ├─ model/        (Board, GameState, players, strategies)
│  │  ├─ view/         (BoardView, ConnectFourApp)
│  │  └─ controller/   (GameController, GameMode, StrategyLoader)
│  └─ test/java/ca/bgiroux/gravitrips/ (JUnit tests)
├─ ai_players/   (optional plugin drop-in folder)
├─ README.md, AGENTS.md, CODE_OF_CONDUCT.md, CONTRIBUTING.md,
LICENSE

## Major Components

- GameConfig: rows, columns, winLength (default 6x7, connect 4).
- Board: 2D grid, move application, win/draw detection, copies
  for AI, available columns query.
- Move, MoveResult (VALID, INVALID, WIN, DRAW).
- GameState: tracks current board, current player, game over,
  winner, reset.
- Player base; HumanPlayer; ComputerPlayer (holds MoveStrategy).
- MoveStrategy interface: String getName(); int chooseMove(Board
  board, int playerId);
- RandomMoveStrategy: picks a random available column.
- StrategyLoader: loads built-in random plus plugins from
  ai_players/ (.class/.jar).
- GameMode: HUMAN_VS_HUMAN, HUMAN_VS_COMPUTER.
- GameController: wires view, applies moves, schedules AI, shows
  dialogs, logs events.
- BoardView (JavaFX): grid rendering, column click handler,
  invalid move alert.
- ConnectFourApp: JavaFX entrypoint, menus, new game dialog,
  app lifecycle.

## Build & Runtime

- Gradle wrapper targets Java 21 toolchain.
- Dependencies:
  org.openjfx:javafx-{base,graphics,controls,fxml}:21.0.2 with
  platform classifier (linux-aarch64|linux|mac|mac-aarch64|win|
  win-aarch64) set from OS/arch.
- Tests: JUnit BOM 5.10.2 + Jupiter.
- Start scripts (installDist/distZip) set:
    - --module-path "$APP_HOME/lib"
    - --add-modules javafx.controls,javafx.fxml
    - -Djavafx.cachedir="$APP_HOME/cache"
- ./gradlew run adds module-path args via JavaExec configuration.
- No module-info.java.

## Coding Conventions

- Java 21, standard formatting (4-space indent).
- Classes PascalCase; methods/fields camelCase; constants
  UPPER_SNAKE_CASE.
- Avoid magic numbers; use GameConfig or constants.
- Keep methods small; limit side effects; prefer immutability for
  configs/copies.
- UI logic only in view; no JavaFX in model.

## Testing

- JUnit 5 tests under src/test/java/ca/bgiroux/gravitrips/.
- Required coverage:
    - Board: horizontal, vertical, diagonal wins; full column
      invalid; draw detection.
    - RandomMoveStrategy: always selects an available column.
- Command: ./gradlew test.

## AI Plugin Instructions

- Implement MoveStrategy with public no-arg constructor and
  unique getName().
- Compile to .class or package as .jar.
- Place in ai_players/ at working directory root.
- Restart app; strategies appear in New Game dialog dropdown.

## Future Extensions

- Stronger AI (minimax/MCTS), difficulty settings.
- UI polish: animations, responsive layout, accessibility
  improvements.
- Configurable board sizes/win lengths via UI; persisted settings
  and scores.
- Plugin discovery UI with metadata; sandboxing for untrusted
  plugins.
- Packaging: native installers or jlink image with bundled
  JavaFX/Java.

