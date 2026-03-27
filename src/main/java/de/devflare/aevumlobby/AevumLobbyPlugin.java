package de.devflare.aevumlobby;

import lombok.Getter;
import de.devflare.aevumlobby.bootstrap.BootstrapManager;
import de.devflare.aevumlobby.service.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AevumLobbyPlugin extends JavaPlugin {

    @Getter
    private static AevumLobbyPlugin instance;

    private BootstrapManager bootstrapManager;
    private ServiceRegistry serviceRegistry;

    @Override
    public void onEnable() {
        instance = this;

        try {
            this.bootstrapManager = new BootstrapManager(this);
            this.bootstrapManager.bootstrap();

            this.serviceRegistry = new ServiceRegistry(this.getLogger());

            this.registerServices();
            this.serviceRegistry.initializeAll();

            this.getLogger().info("AevumLobby has been enabled successfully!");
        } catch (final Exception exception) {
            this.getLogger().severe("Failed to enable AevumLobby!");
            exception.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (this.serviceRegistry != null) {
                this.serviceRegistry.shutdownAll();
            }

            if (this.bootstrapManager != null) {
                this.bootstrapManager.performShutdown();
            }

            this.getLogger().info("AevumLobby has been disabled successfully!");
        } catch (final Exception exception) {
            this.getLogger().severe("Error during shutdown!");
            exception.printStackTrace();
        }
    }

    private void registerServices() {
        this.getLogger().info("Registering services...");

        final de.devflare.aevumlobby.config.ConfigManager configManager = new de.devflare.aevumlobby.config.ConfigManager(
                this);
        this.serviceRegistry.register(de.devflare.aevumlobby.config.ConfigManager.class, configManager);

        final de.devflare.aevumlobby.cache.CacheManager cacheManager = new de.devflare.aevumlobby.cache.CacheManager(this);
        this.serviceRegistry.register(de.devflare.aevumlobby.cache.CacheManager.class, cacheManager);

        final de.devflare.aevumlobby.spawn.SpawnManager spawnManager = new de.devflare.aevumlobby.spawn.SpawnManager(this,
                configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.spawn.SpawnManager.class, spawnManager);

        final de.devflare.aevumlobby.protection.ProtectionManager protectionManager = new de.devflare.aevumlobby.protection.ProtectionManager(
                this, configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.protection.ProtectionManager.class, protectionManager);

        final de.devflare.aevumlobby.hotbar.HotbarManager hotbarManager = new de.devflare.aevumlobby.hotbar.HotbarManager(
                this, configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.hotbar.HotbarManager.class, hotbarManager);

        final de.devflare.aevumlobby.visibility.VisibilityManager visibilityManager = new de.devflare.aevumlobby.visibility.VisibilityManager(
                this, configManager, cacheManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.visibility.VisibilityManager.class, visibilityManager);

        final de.devflare.aevumlobby.gui.GUIManager guiManager = new de.devflare.aevumlobby.gui.GUIManager(this,
                configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.gui.GUIManager.class, guiManager);

        final de.devflare.aevumlobby.scoreboard.ScoreboardManager scoreboardManager = new de.devflare.aevumlobby.scoreboard.ScoreboardManager(
                this, configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.scoreboard.ScoreboardManager.class, scoreboardManager);

        final de.devflare.aevumlobby.npc.NPCManager npcManager = new de.devflare.aevumlobby.npc.NPCManager(this,
                configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.npc.NPCManager.class, npcManager);

        final de.devflare.aevumlobby.ai.AIFormatter aiFormatter = new de.devflare.aevumlobby.ai.AIFormatter(this,
                configManager);
        this.serviceRegistry.register(de.devflare.aevumlobby.ai.AIFormatter.class, aiFormatter);

        this.registerCommands();
        this.registerListeners();
    }

    private void registerCommands() {
        this.getLogger().info("Registering commands...");

        this.getCommand("aevumlobby").setExecutor(new de.devflare.aevumlobby.command.AevumLobbyCommand(this));
        this.getCommand("setlobby").setExecutor(new de.devflare.aevumlobby.command.SetLobbyCommand(this));
        this.getCommand("sethub").setExecutor(new de.devflare.aevumlobby.command.SetHubCommand(this));
        this.getCommand("hub").setExecutor(new de.devflare.aevumlobby.command.HubCommand(this));
        this.getCommand("lobby").setExecutor(new de.devflare.aevumlobby.command.HubCommand(this));
        this.getCommand("info").setExecutor(new de.devflare.aevumlobby.command.InfoCommand(this));
        this.getCommand("socials").setExecutor(new de.devflare.aevumlobby.command.SocialsCommand(this));
        this.getCommand("hubnpc").setExecutor(new de.devflare.aevumlobby.command.HubNPCCommand(this));
    }

    private void registerListeners() {
        this.getLogger().info("Registering listeners...");

        this.getServer().getPluginManager().registerEvents(new de.devflare.aevumlobby.listener.PlayerJoinListener(this),
                this);
        this.getServer().getPluginManager().registerEvents(new de.devflare.aevumlobby.listener.PlayerQuitListener(this),
                this);
        this.getServer().getPluginManager().registerEvents(new de.devflare.aevumlobby.listener.ProtectionListener(this),
                this);
        this.getServer().getPluginManager()
                .registerEvents(new de.devflare.aevumlobby.listener.HotbarInteractListener(this), this);
        this.getServer().getPluginManager().registerEvents(new de.devflare.aevumlobby.listener.GUIListener(this), this);
        this.getServer().getPluginManager().registerEvents(new de.devflare.aevumlobby.npc.NPCListener(this), this);
    }
}
