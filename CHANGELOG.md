# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.5] - 2026-02-11

### Added
- Implemented a comprehensive Help Menu for `/aevumhub` and `/hubnpc`.
- Added `/aevumhub` and `/hubplugin` aliases for easier access.
- Enhanced NPC system with `/hubnpc list` and `/hubnpc remove <name>` commands.

## [1.0.4] - 2026-02-11

### Added
- Complete NPC System implemented from scratch.
  - NPC Data Management (Persistence in `npcs.yml`).
  - Spawning logic using ArmorStands with Player Heads.
  - `/hubnpc` command for administration.
  - Auto-respawn and visibility handling for joining players.

## [1.0.3] - 2026-02-07

### Fixed

- Fixed deprecated `registerNewObjective` method by using `Criteria.DUMMY` instead of string literal.

### Added

- Added `{max_players}` placeholder support to scoreboard system.

## [1.0.2] - 2026-02-07

### Added
- Implemented `BaseGUI` and `ConfigurableGUI` classes for the menu system.
- Added logic to `GUIManager` to load menus from `menus.yml`.
- Wired up `HotbarManager` click actions to open the corresponding GUIs.

### Fixed
- Fixed hotbar items not responding to right-click interactions.
- Updated `HotbarInteractListener` to use slot-based lookup instead of item comparison for better reliability.

## [1.0.1] - 2026-02-07

### Fixed
- Resolved `NoClassDefFoundError` for `MiniMessage` by removing conflicting shaded library.
- Fixed dependency relocation issues in `pom.xml`.

### Changed
- Switched to using Paper's native Adventure API instead of shading `adventure-platform-bukkit`.
- Updated documentation to reflect the new version.

## [1.0.0] - 2026-02-07

### Added
- Initial release of AevumLobby.
- Core systems: Spawn, Protection, Hotbar, Scoreboard, Tablist, NPC, Visibility.
- Configuration files: `config.yml`, `language.yml`, `menus.yml`, `ai_templates.yml`.
