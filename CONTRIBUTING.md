# Contributing to Gravitrips

Thanks for your interest in Gravitrips. Please note this project is **largely unmaintained**, and **pull requests, feature requests, and bug reports may not be addressed in a timely manner**. The majority of the code was originally produced by AI (Codex CLI).

## Code Style
- Keep code readable and follow the existing conventions in `src/main/java/ca/bgiroux/gravitrips`.
- Maintain the MVC separation: rules/state in `model`, UI rendering in `view`, orchestration/AI/threading in `controller`.
- Avoid magic numbers; use `GameConfig` or constants.

## Development Workflow
- Prefer small, focused changes with clear commit messages.
- Keep dependencies minimal; stick to standard library and current build plugins.
- Before submitting, run:
  ```sh
  ./gradlew test
  ```
  Ensure tests pass; add/update tests when logic changes.

## Submitting a Pull Request
1. Fork and branch for your change.
2. Make updates following the style and architecture guidelines.
3. Run `./gradlew test` and mention results in the PR.
4. Provide a clear title and description; include screenshots/gifs for UI changes and note any known gaps.

## Code of Conduct
Participation is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). Please be respectful, inclusive, and constructive.
