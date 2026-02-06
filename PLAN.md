# AevumLobby - Technical Architecture & Design Plan

## Project Vision

AevumLobby is a production-grade, fully open-source Minecraft Hub/Lobby core plugin designed to rival and surpass commercial alternatives like PhoenixLobby. It provides complete control over the lobby experience while maintaining long-term maintainability, modularity, and scalability.

**Core Goals:**
- Full ownership and customization capabilities
- PhoenixLobby-level feature parity
- Superior code quality and maintainability
- Zero dependency on paid services or external APIs
- Runtime extensibility and hot-reload support
- Performance-optimized for high player counts

## PhoenixLobby Comparison

### Feature Parity
| Feature | PhoenixLobby | AevumLobby | Notes |
|---------|--------------|------------|-------|
| Spawn Management | ✓ | ✓ | Enhanced multi-world support |
| Player Protection | ✓ | ✓ | Modular flag system |
| Hotbar Items | ✓ | ✓ | Runtime extensible |
| GUI Framework | ✓ | ✓ | More flexible architecture |
| Server Navigator | ✓ | ✓ | Proxy-agnostic |
| Visibility System | ✓ | ✓ | Persistent preferences |
| Scoreboard | ✓ | ✓ | Animated with placeholders |
| NPCs | ✓ | ✓ | Packet-level, lightweight |
| Social Commands | ✓ | ✓ | Fully customizable |
| Admin Tools | ✓ | ✓ | Debug & diagnostics |

### Advantages Over PhoenixLobby
1. **Open Source**: Full code access and modification rights
2. **No Licensing Costs**: Completely free
3. **Modular Architecture**: Better separation of concerns
4. **Hot Reload**: All systems support runtime reloading
5. **Extensibility**: Plugin-like module system
6. **Performance**: Async-first design with zero main-thread blocking
7. **Maintainability**: Clean code with comprehensive documentation

## Architectural Overview

### Core Design Principles

1. **Service-Oriented Architecture**
   - Each major system is an independent service
   - Services register with central ServiceRegistry
   - Loose coupling through interfaces
   - Dependency injection where appropriate

2. **Lifecycle Management**
   - Explicit initialization phases
   - Graceful degradation on errors
   - Clean shutdown procedures
   - State preservation during reloads

3. **Configuration-Driven Behavior**
   - All features configurable without code changes
   - Hot-reload support across all systems
   - Validation and fallback mechanisms
   - Version migration support

4. **Async-First Philosophy**
   - All I/O operations are asynchronous
   - Main thread reserved for game logic only
   - Scheduler abstraction for clean threading
   - Thread-safe data structures throughout

5. **Defensive Programming**
   - Null-safe by default
   - Optional for uncertain values
   - Try-catch at service boundaries
   - Graceful error handling with logging

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     AevumLobbyPlugin                        │
│                    (Main Entry Point)                       │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    ServiceRegistry                          │
│              (Central Service Coordinator)                  │
└───────────────────────────┬─────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│ Configuration │   │  Bootstrap    │   │    Cache      │
│    System     │   │   Manager     │   │   Manager     │
└───────────────┘   └───────────────┘   └───────────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
        ┌───────────────────┴───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│     Spawn     │   │  Protection   │   │    Hotbar     │
│  Management   │   │    System     │   │      HUD      │
└───────────────┘   └───────────────┘   └───────────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
        ┌───────────────────┴───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│   UI/GUI      │   │  Navigator    │   │  Visibility   │
│   Platform    │   │    System     │   │    System     │
└───────────────┘   └───────────────┘   └───────────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
        ┌───────────────────┴───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  Scoreboard   │   │      NPC      │   │      AI       │
│   & Tablist   │   │    Engine     │   │   Formatter   │
└───────────────┘   └───────────────┘   └───────────────┘
```

## System Interactions

### Player Join Flow
```
Player Joins World
    ↓
JoinListener triggered
    ↓
├─→ SpawnManager: Teleport to spawn
├─→ ProtectionSystem: Apply protection rules
├─→ HotbarManager: Give hotbar items
├─→ VisibilityManager: Load preferences
├─→ ScoreboardManager: Display scoreboard
└─→ TablistManager: Update tablist
```

### Hotbar Item Click Flow
```
Player Clicks Hotbar Item
    ↓
ItemInteractListener
    ↓
HotbarManager: Identify item
    ↓
Action Router
    ↓
├─→ UIManager: Open GUI
├─→ VisibilityManager: Toggle players
├─→ NavigatorManager: Show servers
└─→ Custom Action Handler
```

### GUI Navigation Flow
```
Player Opens GUI
    ↓
UIManager: Create GUI instance
    ↓
GUIRenderer: Build inventory view
    ↓
Player Clicks Button
    ↓
GUIClickListener
    ↓
Action Executor
    ↓
├─→ Open nested GUI
├─→ Execute command
├─→ Send to server
└─→ Close GUI
```

### Configuration Reload Flow
```
/aevumlobby reload
    ↓
ReloadCommand
    ↓
├─→ ConfigManager: Reload all configs
│   ├─→ Validate syntax
│   ├─→ Validate values
│   └─→ Rollback on error
├─→ ServiceRegistry: Notify all services
│   └─→ Each service reloads itself
└─→ Return success/failure report
```

## Data Flow

### Configuration Data Flow
```
YAML Files (disk)
    ↓
ConfigManager (loads & validates)
    ↓
Configuration Objects (in-memory)
    ↓
Services (consume config)
    ↓
Runtime Behavior
```

### Player Data Flow
```
Player Action
    ↓
Event Listener
    ↓
Service Layer (business logic)
    ↓
├─→ Cache (memory)
├─→ Storage (disk/database)
└─→ Response to Player
```

### NPC Data Flow
```
NPC Configuration (YAML)
    ↓
NPCManager (loads definitions)
    ↓
NPCEntity (packet-level entities)
    ↓
Player Interaction
    ↓
Action Binding
    ↓
Execute Action (command/server transfer)
```

## Threading Model

### Thread Categories

1. **Main Thread (Game Thread)**
   - Event handling
   - Entity manipulation
   - Inventory operations
   - Packet sending
   - **Rule**: No blocking operations ever

2. **Async Pool**
   - File I/O operations
   - Configuration loading
   - Database queries
   - HTTP requests
   - **Rule**: Never touch Bukkit API directly

3. **Scheduler Thread**
   - Scheduled tasks
   - Periodic updates
   - Cache cleanup
   - **Rule**: Sync tasks for Bukkit API, async for everything else

### Thread Safety Mechanisms

1. **Concurrent Collections**
   - ConcurrentHashMap for player data
   - CopyOnWriteArrayList for listeners
   - Thread-safe queues for async operations

2. **Synchronization Points**
   - Main thread callbacks for Bukkit operations
   - CompletableFuture for async chains
   - Synchronized blocks only where necessary

3. **Atomic Operations**
   - AtomicInteger for counters
   - AtomicReference for state
   - Lock-free where possible

### Example Threading Flow
```java
// Configuration reload (async)
CompletableFuture.supplyAsync(() -> {
    // Load YAML from disk (async thread)
    return configLoader.load();
})
.thenApplyAsync(config -> {
    // Process and validate (async thread)
    return configValidator.validate(config);
})
.thenAcceptSync(validConfig -> {
    // Apply to runtime state (main thread)
    configManager.apply(validConfig);
}, mainThreadScheduler);
```

## Memory Management

### Caching Strategy

1. **Player Data Cache**
   - In-memory cache with TTL
   - Weak references for inactive players
   - Automatic cleanup on logout
   - Size-limited with LRU eviction

2. **Configuration Cache**
   - Loaded once on startup
   - Reloaded on command
   - No automatic expiration
   - Memory-efficient storage

3. **GUI Instance Cache**
   - Per-player GUI sessions
   - Cleaned up on close
   - Pooled reusable components
   - Lazy initialization

4. **NPC Entity Cache**
   - Loaded on world load
   - Persistent until reload
   - Packet data cached
   - Skin data cached

### Memory Optimization Techniques

1. **Lazy Loading**
   - GUIs created on demand
   - Configurations loaded when needed
   - Player data fetched on access

2. **Object Pooling**
   - Reusable GUI components
   - Packet builders
   - ItemStack templates

3. **Weak References**
   - Inactive player data
   - Temporary GUI state
   - Event listener cleanup

4. **Garbage Collection Friendly**
   - Avoid object retention
   - Clear collections properly
   - Null out references
   - Use try-with-resources

### Memory Footprint Targets

- Base plugin: < 10 MB
- Per player overhead: < 50 KB
- GUI instance: < 100 KB
- NPC entity: < 20 KB
- Total for 100 players: < 25 MB

## Configuration Schema

### config.yml Structure
```yaml
# General settings
general:
  plugin-name: "AevumMC"
  debug-mode: false
  language: "en_US"

# Spawn system
spawn:
  enabled: true
  teleport-on-join: true
  void-rescue-enabled: true
  void-rescue-y-level: -64
  teleport-delay: 0

# Protection system
protection:
  enabled: true
  disable-damage: true
  disable-hunger: true
  disable-block-break: true
  disable-block-place: true
  disable-item-drop: true
  disable-item-pickup: true
  disable-inventory-click: true
  bypass-permission: "aevumlobby.bypass"

# Hotbar system
hotbar:
  enabled: true
  give-on-join: true
  give-on-respawn: true
  clear-inventory: true
  items:
    profile:
      enabled: true
      slot: 0
      material: "PLAYER_HEAD"
      name: "<gradient:#ff6b6b:#ffd93d>Profile</gradient>"
      lore:
        - "<gray>Click to view your profile"
    navigator:
      enabled: true
      slot: 4
      material: "COMPASS"
      name: "<gradient:#4facfe:#00f2fe>Server Navigator</gradient>"
    visibility:
      enabled: true
      slot: 8
      material: "LIME_DYE"
      name: "<green>Players Visible</green>"

# UI system
ui:
  enabled: true
  update-interval: 20
  close-on-move: false

# Visibility system
visibility:
  enabled: true
  default-state: "VISIBLE"
  save-preferences: true
  bypass-permission: "aevumlobby.staff"

# Scoreboard system
scoreboard:
  enabled: true
  update-interval: 20
  title: "<gradient:#ff6b6b:#ffd93d>AevumMC</gradient>"
  lines:
    - ""
    - "<gray>Players: <white>{online}</white>"
    - "<gray>Rank: <white>{rank}</white>"
    - ""
    - "<yellow>play.aevummc.net</yellow>"

# Navigator system
navigator:
  enabled: true
  categories:
    - name: "Main Games"
      icon: "DIAMOND_SWORD"
      servers:
        - name: "SkyBlock"
          material: "GRASS_BLOCK"
          server: "skyblock"
        - name: "Survival"
          material: "DIAMOND_PICKAXE"
          server: "survival"

# Socials
socials:
  website: "https://aevummc.net"
  discord: "https://discord.gg/aevummc"
  youtube: "https://youtube.com/@aevummc"
  tiktok: "https://tiktok.com/@aevummc"
  twitch: "https://twitch.tv/aevummc"

# Performance
performance:
  async-file-operations: true
  cache-player-data: true
  cache-ttl-seconds: 300
```

### language.yml Structure
```yaml
prefix: "<gradient:#ff6b6b:#ffd93d>[AevumLobby]</gradient> "

messages:
  spawn-set: "<green>Spawn set successfully!"
  spawn-teleport: "<green>Teleported to spawn!"
  reload-success: "<green>Configuration reloaded successfully!"
  reload-failed: "<red>Failed to reload configuration. Check console for errors."

errors:
  no-permission: "<red>You don't have permission to use this command."
  spawn-not-set: "<red>Spawn is not set for this world."
  player-not-found: "<red>Player not found."
  invalid-arguments: "<red>Invalid arguments. Usage: {usage}"

placeholders:
  online: "{online_players}"
  max: "{max_players}"
  rank: "{player_rank}"
  world: "{world_name}"
```

### menus.yml Structure
```yaml
menus:
  profile:
    title: "<gradient:#ff6b6b:#ffd93d>Your Profile</gradient>"
    size: 27
    items:
      stats:
        slot: 11
        material: "DIAMOND"
        name: "<aqua>Statistics</aqua>"
        lore:
          - "<gray>View your stats"
      settings:
        slot: 13
        material: "COMPARATOR"
        name: "<yellow>Settings</yellow>"
      close:
        slot: 22
        material: "BARRIER"
        name: "<red>Close</red>"
```

### ai_templates.yml Structure
```yaml
templates:
  welcome:
    - "Welcome to {server}!"
    - "Hey there! Welcome to {server}!"
    - "Greetings! You've joined {server}!"

  goodbye:
    - "See you later!"
    - "Thanks for playing!"
    - "Goodbye! Come back soon!"

grammar:
  greetings:
    - "Hello"
    - "Hey"
    - "Hi"
    - "Greetings"
```

## UI/HUD Framework

### Component Hierarchy
```
GUIManager (Registry & Factory)
    │
    ├─→ GUI (Abstract Base)
    │   ├─→ StaticGUI (Fixed layout)
    │   ├─→ PaginatedGUI (Multi-page)
    │   └─→ DynamicGUI (Runtime-generated)
    │
    ├─→ GUIButton (Interactive Element)
    │   ├─→ ActionButton (Execute action)
    │   ├─→ NavigationButton (Open GUI)
    │   └─→ CloseButton (Close GUI)
    │
    └─→ GUIContext (Player Session)
        ├─→ Current GUI
        ├─→ Previous GUI (back stack)
        └─→ Session Data
```

### HUD Item Framework
```
HotbarManager (Registry & Provider)
    │
    ├─→ HotbarItem (Abstract Base)
    │   ├─→ MenuOpenerItem
    │   ├─→ VisibilityToggleItem
    │   ├─→ NavigatorItem
    │   └─→ CustomActionItem
    │
    └─→ HotbarSlot (Position Mapping)
        ├─→ Slot Number
        ├─→ Item Provider
        └─→ Player Context
```

### Runtime Registration Example
```java
// Register custom hotbar item
hotbarManager.registerItem("custom_tool", new HotbarItem() {
    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.STICK)
            .name("<gold>Custom Tool</gold>")
            .build();
    }

    @Override
    public void onClick(Player player, ClickAction action) {
        player.sendMessage("Custom tool clicked!");
    }
});

// Register custom GUI
guiManager.registerGUI("custom_menu", new StaticGUI() {
    @Override
    public String getTitle(Player player) {
        return "<rainbow>Custom Menu</rainbow>";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void populate(GUIContext context) {
        context.setButton(13, new ActionButton(
            Material.DIAMOND,
            "<aqua>Click Me</aqua>",
            player -> player.sendMessage("Clicked!")
        ));
    }
});
```

## Security Model

### Permission Hierarchy
```
aevumlobby.*
    ├─→ aevumlobby.use (Basic lobby features)
    ├─→ aevumlobby.admin (Admin commands)
    │   ├─→ aevumlobby.reload
    │   ├─→ aevumlobby.debug
    │   └─→ aevumlobby.npc
    ├─→ aevumlobby.bypass (Protection bypass)
    └─→ aevumlobby.staff (Staff-only features)
```

### Command Security
- Permission validation before execution
- Argument sanitization
- Player vs console detection
- Rate limiting for expensive operations

### Data Security
- Player data isolation
- File path validation
- SQL injection prevention (if database used)
- XSS prevention in messages

### Configuration Security
- Type validation on reload
- Range checks for numeric values
- Whitelist for enum values
- Rollback on invalid configuration

## Scaling Strategy

### Horizontal Scaling (Multiple Lobbies)
- Each lobby instance is independent
- No shared state between instances
- Proxy-level load balancing
- Consistent player experience

### Vertical Scaling (More Players per Lobby)
- Async operations prevent blocking
- Efficient packet batching
- Memory-conscious caching
- Lazy loading where possible

### Performance Targets
- 500+ concurrent players per lobby
- < 1ms tick impact with 100 players
- < 5 second configuration reload
- < 50ms GUI open time

### Optimization Points
1. **Packet Optimization**
   - Batch NPC spawns
   - Throttle scoreboard updates
   - Cache unchanged data

2. **Event Optimization**
   - Priority ordering
   - Early exit conditions
   - Minimal listener count

3. **Memory Optimization**
   - Weak references for caches
   - Object pooling for common items
   - Lazy initialization

## Roadmap

### Phase 1: Core Foundation (v1.0.0)
- [x] Project structure
- [x] Maven configuration
- [x] Bootstrap system
- [x] Configuration management
- [x] Service registry

### Phase 2: Essential Systems (v1.1.0)
- [x] Spawn management
- [x] Player protection
- [x] Hotbar framework
- [x] Basic commands

### Phase 3: UI & Navigation (v1.2.0)
- [x] GUI platform
- [x] Server navigator
- [x] Menu system
- [x] Item providers

### Phase 4: Advanced Features (v1.3.0)
- [x] Visibility system
- [x] Scoreboard engine
- [x] Tablist management
- [x] NPC system

### Phase 5: Polish & Performance (v1.4.0)
- [x] AI formatter
- [x] Admin tools
- [x] Debug utilities
- [x] Performance optimization

### Phase 6: Community & Extension (v1.5.0+)
- [ ] API documentation
- [ ] Developer examples
- [ ] Extension system
- [ ] Community modules

## Technical Decisions & Rationale

### Why Paper over Spigot?
- Modern API features
- Better performance
- Active maintenance
- Adventure API native support

### Why Adventure API?
- Modern text formatting
- Component-based architecture
- MiniMessage syntax
- Future-proof

### Why No Database by Default?
- Reduces complexity for simple setups
- File-based storage sufficient for lobbies
- Optional integration for advanced needs
- Easier to deploy and maintain

### Why Custom NPC System?
- Full control over behavior
- No external dependencies
- Lightweight packet-based approach
- Tailored to lobby needs

### Why Offline AI Formatter?
- No external API costs
- No rate limits
- Privacy-friendly
- Instant responses
- Full control over output

## Success Criteria

A successful v1.0.0 release includes:

1. ✓ All core systems functional
2. ✓ Zero compilation errors
3. ✓ Complete configuration files
4. ✓ Hot-reload working
5. ✓ All commands operational
6. ✓ Comprehensive documentation
7. ✓ Installation guide
8. ✓ Clean code with Lombok
9. ✓ No TODOs or placeholders
10. ✓ Production-ready quality

## Conclusion

AevumLobby represents a complete, production-grade lobby solution that provides full control, excellent performance, and long-term maintainability. The modular architecture ensures extensibility while maintaining clean separation of concerns. With proper implementation of all planned systems, AevumLobby will meet or exceed the capabilities of commercial alternatives while remaining completely free and open-source.
