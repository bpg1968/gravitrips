# Repository Guidelines

## Project Structure & Module Organization
- Code follows IntelliJ-style layout: `src/main/java/ca/bgiroux/gravitrips/{model,view,controller}` and tests in `src/test/java/ca/bgiroux/gravitrips`.
- Gradle Kotlin DSL config lives in `build.gradle.kts` and `settings.gradle.kts`; `.gitignore` tuned for Gradle/IDEA/OS artifacts.
- JavaFX entry point: `ca.bgiroux.gravitrips.view.ConnectFourApp`. Core gameplay logic sits in `model`, UI in `view`, orchestration/AI loading in `controller`.

## Build, Test, and Development Commands
- `gradle wrapper --gradle-version 8.7` — generate wrapper (fix native platform issues first if any). Once present, prefer `./gradlew`.
- `./gradlew test` — run JUnit 5 suite.
- `./gradlew run` — launch the JavaFX app (main window “Gravitrips”).
- `./gradlew clean` — remove build outputs.

## Coding Style & Naming Conventions
- Language: Java 21 on the classpath (no module-info). Use clear names; classes in PascalCase, methods/fields in camelCase, constants in UPPER_SNAKE_CASE.
- Keep logic small and readable; avoid magic numbers—use `GameConfig` or constants for board sizes and win lengths.
- MVC separation: rules and state in `model` (no JavaFX), UI in `view` (render and emit events), control/AI and threading in `controller`.
- Prefer immutability for configs/state snapshots; avoid global static state.

## Testing Guidelines
- Framework: JUnit 5 (platform BOM). Tests reside under `src/test/java/ca/bgiroux/gravitrips`.
- Coverage expectations: board win detection (horizontal/vertical/diagonal), full-column handling, draw detection, and AI validity (RandomMoveStrategy chooses open columns).
- Naming: mirror class under test (e.g., `BoardTest`, `RandomMoveStrategyTest`) with descriptive test method names.
- Run tests via `./gradlew test`; keep tests deterministic (avoid relying on randomness without control).

## Commit & Pull Request Guidelines
- Commits: use concise, imperative summaries (e.g., “Add board win detection tests”). Group related changes; avoid mixing formatting-only changes with logic.
- Pull requests: include a short description, key changes, testing performed (`./gradlew test` output), and screenshots/gifs for UI-visible changes (menus, board rendering). Link issues/tasks when applicable and mention any follow-ups or known gaps (e.g., plugin loading edge cases).
