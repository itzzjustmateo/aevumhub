package me.aevum.aevumlobby.config;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.service.Service;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public final class ConfigManager implements Service {

    private final AevumLobbyPlugin plugin;
    private FileConfiguration config;
    private FileConfiguration language;
    private FileConfiguration menus;
    private FileConfiguration aiTemplates;
    private boolean enabled;

    public ConfigManager(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Loading configuration files...");

        this.saveDefaultConfigs();
        this.loadConfigs();

        this.plugin.getLogger().info("Configuration files loaded successfully");
    }

    @Override
    public void shutdown() {
        this.config = null;
        this.language = null;
        this.menus = null;
        this.aiTemplates = null;
    }

    @Override
    public void reload() {
        this.plugin.getLogger().info("Reloading configurations...");

        try {
            this.loadConfigs();
            this.plugin.getLogger().info("Configurations reloaded successfully");
        } catch (final Exception exception) {
            this.plugin.getLogger().severe("Failed to reload configurations");
            exception.printStackTrace();
            throw exception;
        }
    }

    @Override
    public String getName() {
        return "ConfigManager";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    private void saveDefaultConfigs() {
        this.saveDefaultConfig("config.yml");
        this.saveDefaultConfig("language.yml");
        this.saveDefaultConfig("menus.yml");
        this.saveDefaultConfig("ai_templates.yml");
    }

    private void saveDefaultConfig(final String fileName) {
        final File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }

    private void loadConfigs() {
        this.config = this.loadConfig("config.yml");
        this.language = this.loadConfig("language.yml");
        this.menus = this.loadConfig("menus.yml");
        this.aiTemplates = this.loadConfig("ai_templates.yml");
    }

    private FileConfiguration loadConfig(final String fileName) {
        final File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            this.plugin.getLogger().warning("Configuration file not found: " + fileName);
            return new YamlConfiguration();
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig(final String fileName, final FileConfiguration configuration) {
        final File file = new File(this.plugin.getDataFolder(), fileName);

        try {
            configuration.save(file);
        } catch (final IOException exception) {
            this.plugin.getLogger().severe("Failed to save configuration: " + fileName);
            exception.printStackTrace();
        }
    }

    public String getString(final String path, final String defaultValue) {
        return this.config.getString(path, defaultValue);
    }

    public int getInt(final String path, final int defaultValue) {
        return this.config.getInt(path, defaultValue);
    }

    public boolean getBoolean(final String path, final boolean defaultValue) {
        return this.config.getBoolean(path, defaultValue);
    }

    public double getDouble(final String path, final double defaultValue) {
        return this.config.getDouble(path, defaultValue);
    }

    public List<String> getStringList(final String path) {
        return this.config.getStringList(path);
    }

    public String getLanguageString(final String path, final String defaultValue) {
        return this.language.getString(path, defaultValue);
    }

    public List<String> getLanguageStringList(final String path) {
        return this.language.getStringList(path);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
