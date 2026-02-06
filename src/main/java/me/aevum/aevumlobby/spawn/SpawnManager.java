package me.aevum.aevumlobby.spawn;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public final class SpawnManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, Location> spawns;
    private File spawnFile;
    private FileConfiguration spawnConfig;
    private boolean enabled;

    public SpawnManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.spawns = new HashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Loading spawn locations...");

        this.spawnFile = new File(this.plugin.getDataFolder(), "spawns.yml");
        if (!this.spawnFile.exists()) {
            try {
                this.spawnFile.createNewFile();
            } catch (final IOException exception) {
                this.plugin.getLogger().severe("Failed to create spawns.yml");
                exception.printStackTrace();
            }
        }

        this.spawnConfig = YamlConfiguration.loadConfiguration(this.spawnFile);
        this.loadSpawns();

        this.plugin.getLogger().info("Loaded " + this.spawns.size() + " spawn locations");
    }

    @Override
    public void shutdown() {
        this.spawns.clear();
    }

    @Override
    public void reload() {
        this.spawns.clear();
        this.spawnConfig = YamlConfiguration.loadConfiguration(this.spawnFile);
        this.loadSpawns();
        this.plugin.getLogger().info("Reloaded spawn locations");
    }

    @Override
    public String getName() {
        return "SpawnManager";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    private void loadSpawns() {
        for (final String worldName : this.spawnConfig.getKeys(false)) {
            final String path = worldName + ".";
            final String worldNameValue = this.spawnConfig.getString(path + "world");
            final double x = this.spawnConfig.getDouble(path + "x");
            final double y = this.spawnConfig.getDouble(path + "y");
            final double z = this.spawnConfig.getDouble(path + "z");
            final float yaw = (float) this.spawnConfig.getDouble(path + "yaw");
            final float pitch = (float) this.spawnConfig.getDouble(path + "pitch");

            final World world = this.plugin.getServer().getWorld(worldNameValue);
            if (world == null) {
                this.plugin.getLogger().warning("World not found for spawn: " + worldNameValue);
                continue;
            }

            final Location location = new Location(world, x, y, z, yaw, pitch);
            this.spawns.put(worldName, location);
        }
    }

    public void setSpawn(final String identifier, final Location location) {
        this.spawns.put(identifier, location);

        final String path = identifier + ".";
        this.spawnConfig.set(path + "world", location.getWorld().getName());
        this.spawnConfig.set(path + "x", location.getX());
        this.spawnConfig.set(path + "y", location.getY());
        this.spawnConfig.set(path + "z", location.getZ());
        this.spawnConfig.set(path + "yaw", location.getYaw());
        this.spawnConfig.set(path + "pitch", location.getPitch());

        this.saveSpawns();
    }

    private void saveSpawns() {
        try {
            this.spawnConfig.save(this.spawnFile);
        } catch (final IOException exception) {
            this.plugin.getLogger().severe("Failed to save spawn locations");
            exception.printStackTrace();
        }
    }

    public Optional<Location> getSpawn(final String identifier) {
        return Optional.ofNullable(this.spawns.get(identifier));
    }

    public Optional<Location> getWorldSpawn(final World world) {
        return this.getSpawn(world.getName());
    }

    public void teleportToSpawn(final Player player) {
        final Optional<Location> spawn = this.getWorldSpawn(player.getWorld());
        if (spawn.isPresent()) {
            player.teleportAsync(spawn.get());
        } else {
            player.teleportAsync(player.getWorld().getSpawnLocation());
        }
    }

    public void teleportToSpawn(final Player player, final String identifier) {
        final Optional<Location> spawn = this.getSpawn(identifier);
        spawn.ifPresent(player::teleportAsync);
    }

    public boolean hasSpawn(final String identifier) {
        return this.spawns.containsKey(identifier);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
