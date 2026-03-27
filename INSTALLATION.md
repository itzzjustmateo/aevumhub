# AevumLobby Installation Guide

This guide will walk you through installing and configuring AevumLobby on your Minecraft server.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation Steps](#installation-steps)
3. [First-Time Setup](#first-time-setup)
4. [Configuration](#configuration)
5. [Verification](#verification)
6. [Next Steps](#next-steps)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before installing AevumLobby, ensure you have:

### Server Requirements

- **Minecraft Server**: Paper 1.21.4 or newer
  - Download from: https://papermc.io/downloads
  - Purpur and other Paper forks are supported

- **Java Version**: Java 21 or higher
  - Check your version: `java -version`
  - Download from: https://adoptium.net/

- **Server Permissions**: Access to server files and console

### Recommended Setup

- **RAM**: At least 2GB allocated to server
- **CPU**: 2+ cores recommended
- **Storage**: 50MB+ free space for plugin and data

---

## Installation Steps

### Step 1: Download the Plugin

#### Option A: Pre-built Release
1. Go to the [Releases](https://github.com/AevumMC/AevumLobby/releases) page
2. Download the latest `AevumLobby-x.x.x.jar` file
3. Save it to your desktop or downloads folder

#### Option B: Build from Source
```bash
# Clone repository
git clone https://github.com/AevumMC/AevumLobby.git
cd AevumLobby

# Build with Maven
mvn clean package

# Find JAR in target/ directory
ls target/AevumLobby-*.jar
```

### Step 2: Install to Server

1. **Stop your Minecraft server** if it's running
   ```bash
   stop
   ```

2. **Locate your plugins folder**
   - Usually: `server_directory/plugins/`

3. **Copy the JAR file**
   ```bash
   cp AevumLobby-1.0.3.jar /path/to/server/plugins/
   ```

4. **Set permissions** (Linux/Mac only)
   ```bash
   chmod 644 /path/to/server/plugins/AevumLobby-1.0.3.jar
   ```

### Step 3: Start the Server

1. **Start your server**
   ```bash
   ./start.sh  # or your start script
   ```

2. **Watch the console** for AevumLobby initialization:
   ```
   [AevumLobby] ========================================
   [AevumLobby]      AevumLobby v1.0.3
   [AevumLobby]      Bootstrapping plugin...
   [AevumLobby] ========================================
   [AevumLobby] Validating environment...
   [AevumLobby] Environment validation passed
   [AevumLobby] Initializing 10 services...
   [AevumLobby] All services initialized successfully
   [AevumLobby] AevumLobby has been enabled successfully!
   ```

3. **Verify installation**
   ```
   /plugins
   ```
   You should see AevumLobby in green

---

## First-Time Setup

### Initial Configuration

After the first start, AevumLobby creates these files:

```
plugins/
└── AevumLobby/
    ├── config.yml
    ├── language.yml
    ├── menus.yml
    ├── ai_templates.yml
    └── spawns.yml
```

### Set Spawn Points

1. **Join your server** as an operator

2. **Go to your desired lobby location**

3. **Set the lobby spawn**:
   ```
   /setlobby
   ```
   Output: `Lobby spawn has been set!`

4. **Set the hub spawn** (optional):
   ```
   /sethub
   ```
   Output: `Hub spawn has been set!`

### Test Basic Functions

1. **Test spawn teleport**:
   ```
   /hub
   ```
   You should teleport to the set location

2. **Check hotbar items**:
   - Leave and rejoin the server
   - Your inventory should contain hotbar items

3. **Test scoreboard**:
   - Scoreboard should appear on the right side
   - Shows server info and player count

---

## Configuration

### Essential Settings

Edit `plugins/AevumLobby/config.yml`:

#### 1. Server Branding

```yaml
general:
  plugin-name: "YourServerName"  # Change this!
```

#### 2. Social Links

```yaml
socials:
  website: "https://yourserver.com"
  discord: "https://discord.gg/yourserver"
  youtube: "https://youtube.com/@yourserver"
  tiktok: "https://tiktok.com/@yourserver"
  twitch: "https://twitch.tv/yourserver"
```

#### 3. Protection Rules

```yaml
protection:
  enabled: true
  disable-damage: true          # Prevent player damage
  disable-hunger: true          # Prevent hunger loss
  disable-block-break: true     # Prevent block breaking
  disable-block-place: true     # Prevent block placing
  disable-item-drop: true       # Prevent item dropping
  disable-item-pickup: true     # Prevent item pickup
  disable-inventory-click: false # Allow inventory clicks
```

#### 4. Hotbar Items

```yaml
hotbar:
  enabled: true
  give-on-join: true
  clear-inventory: true        # Clear inventory on join
  items:
    profile:
      enabled: true
      slot: 0                  # Leftmost slot
      name: "<gradient:#ff6b6b:#ffd93d>Profile</gradient>"
    navigator:
      enabled: true
      slot: 4                  # Middle slot
    visibility:
      enabled: true
      slot: 8                  # Rightmost slot
```

#### 5. Scoreboard

```yaml
scoreboard:
  enabled: true
  update-interval: 20          # Update every 1 second
  title: "<gradient:#ff6b6b:#ffd93d>Your Server</gradient>"
  lines:
    - ""
    - "<gray>Players: <white>{online}</white>"
    - "<gray>Rank: <white>Member</white>"
    - ""
    - "<yellow>play.yourserver.com</yellow>"
```

### Apply Changes

After editing configs:
```
/aevumlobby reload
```

---

## Verification

### Checklist

✅ **Plugin Loads Successfully**
- Check console for errors
- `/plugins` shows AevumLobby in green

✅ **Spawn Points Work**
- `/hub` teleports correctly
- Players spawn at lobby on join

✅ **Protection Active**
- Cannot take damage
- Cannot break blocks
- Cannot drop items

✅ **Hotbar Items Appear**
- Items given on join
- Items are clickable
- Actions work correctly

✅ **Scoreboard Displays**
- Shows on right side
- Updates correctly
- No flickering

✅ **Commands Work**
- `/info` displays server info
- `/socials` shows links
- Admin commands functional

### Test Commands

```bash
# As player
/hub
/lobby
/info
/socials

# As admin
/aevumlobby status
/aevumlobby debug
/setlobby
/sethub
```

---

## Next Steps

### Customization

1. **Customize Messages**
   - Edit `language.yml`
   - Use MiniMessage format
   - Add custom placeholders

2. **Create Custom Menus**
   - Edit `menus.yml`
   - Define GUI layouts
   - Add custom actions

3. **Configure Navigator**
   - Add your servers in `config.yml`
   - Set up categories
   - Configure server icons

4. **Set Up NPCs**
   - Place NPCs with `/aevumlobby setnpc`
   - Configure actions
   - Add server redirect

### Permissions

Set up permissions in your permission plugin:

```yaml
# LuckPerms example
/lp group default permission set aevumlobby.use true
/lp group admin permission set aevumlobby.admin true
/lp group admin permission set aevumlobby.bypass true
```

### Integration

**Proxy Setup** (BungeeCord/Velocity):
- Configure server names in navigator
- Match names with proxy config
- Test server switching

**PlaceholderAPI** (optional):
- Install PlaceholderAPI
- Use placeholders in configs
- Register custom placeholders

---

## Troubleshooting

### Plugin Doesn't Load

**Symptom**: Plugin shows red in `/plugins`

**Solutions**:
1. Check Java version: `java -version` (need Java 21+)
2. Verify Paper version: `version` (need 1.21.4+)
3. Check console for errors
4. Remove other lobby plugins (conflicts)

**Example Error**:
```
Java 21 or higher is required. Current: Java 17
```
**Fix**: Upgrade Java to version 21

---

### Spawn Not Working

**Symptom**: Players don't teleport on join

**Solutions**:
1. Set spawn first: `/setlobby`
2. Check config: `spawn.teleport-on-join: true`
3. Verify spawns.yml has data
4. Reload: `/aevumlobby reload`

**Check spawns.yml**:
```yaml
lobby:
  world: world
  x: 0.0
  y: 100.0
  z: 0.0
  yaw: 0.0
  pitch: 0.0
```

---

### Hotbar Items Missing

**Symptom**: No items in hotbar on join

**Solutions**:
1. Enable in config: `hotbar.enabled: true`
2. Enable individual items: `hotbar.items.profile.enabled: true`
3. Check `clear-inventory: true` is set
4. Reload and rejoin

**Debug**:
```
/aevumlobby debug
# Check HotbarManager status
```

---

### Commands Not Working

**Symptom**: "Unknown command" error

**Solutions**:
1. Check plugin.yml is valid
2. Verify command is registered (console on startup)
3. Check for typos in command name
4. Restart server (not just reload)

**Test**:
```
/aevumlobby status
# Should show system status
```

---

### Permission Issues

**Symptom**: "No permission" errors

**Solutions**:
1. Check permission plugin is installed
2. Verify permissions are set:
   ```
   /lp user YourName permission check aevumlobby.use
   ```
3. Give yourself all permissions:
   ```
   /lp user YourName permission set aevumlobby.* true
   ```
4. Check you're OP: `/op YourName`

---

### Performance Issues

**Symptom**: Server lag with AevumLobby

**Solutions**:
1. Increase scoreboard update interval:
   ```yaml
   scoreboard:
     update-interval: 40  # 2 seconds instead of 1
   ```

2. Disable unused features:
   ```yaml
   scoreboard:
     enabled: false  # If not needed
   ```

3. Check for conflicting plugins

4. Monitor with:
   ```
   /timings
   ```

---

## Advanced Setup

### Multi-World Configuration

If you have multiple lobby worlds:

```yaml
# Set spawn for each world
/setlobby lobby1
/setlobby lobby2
```

Each world gets its own spawn in `spawns.yml`.

### Proxy Integration

For BungeeCord/Velocity networks:

1. **Configure servers in config.yml**:
   ```yaml
   navigator:
     categories:
       - name: "Games"
         servers:
           - name: "SkyBlock"
             server: "skyblock"  # Must match proxy config
   ```

2. **Test connection**:
   - Click navigator item
   - Select server
   - Should transfer to target server

### Database Setup (Advanced)

AevumLobby uses file-based storage by default. For advanced setups:

1. Future versions will support MySQL/PostgreSQL
2. Current version: All data in YAML files
3. Backups: Copy `plugins/AevumLobby/` folder

---

## Support

Need help? We're here!

- **Discord**: https://discord.gg/aevummc
- **GitHub Issues**: https://github.com/AevumMC/AevumLobby/issues
- **Wiki**: https://github.com/AevumMC/AevumLobby/wiki

---

## Uninstallation

To remove AevumLobby:

1. Stop the server
2. Delete `plugins/AevumLobby-x.x.x.jar`
3. Delete `plugins/AevumLobby/` folder (optional, for clean removal)
4. Start the server

Your server will return to default behavior.

---

**Installation complete! Enjoy AevumLobby! 🎉**
