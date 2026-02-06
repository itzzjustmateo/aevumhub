package me.aevum.aevumlobby;

import lombok.Getter;
import me.aevum.aevumlobby.bootstrap.BootstrapManager;
import me.aevum.aevumlobby.service.ServiceRegistry;
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

        final me.aevum.aevumlobby.config.ConfigManager configManager = new me.aevum.aevumlobby.config.ConfigManager(this);
        this.serviceRegistry.register(me.aevum.aevumlobby.config.ConfigManager.class, configManager);

        final me.aevum.aevumlobby.cache.CacheManager cacheManager = new me.aevum.aevumlobby.cache.CacheManager(this);
        this.serviceRegistry.register(me.aevum.aevumlobby.cache.CacheManager.class, cacheManager);

        final me.aevum.aevumlobby.spawn.SpawnManager spawnManager = new me.aevum.aevumlobby.spawn.SpawnManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.spawn.SpawnManager.class, spawnManager);

        final me.aevum.aevumlobby.protection.ProtectionManager protectionManager = new me.aevum.aevumlobby.protection.ProtectionManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.protection.ProtectionManager.class, protectionManager);

        final me.aevum.aevumlobby.hotbar.HotbarManager hotbarManager = new me.aevum.aevumlobby.hotbar.HotbarManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.hotbar.HotbarManager.class, hotbarManager);

        final me.aevum.aevumlobby.visibility.VisibilityManager visibilityManager = new me.aevum.aevumlobby.visibility.VisibilityManager(this, configManager, cacheManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.visibility.VisibilityManager.class, visibilityManager);

        final me.aevum.aevumlobby.gui.GUIManager guiManager = new me.aevum.aevumlobby.gui.GUIManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.gui.GUIManager.class, guiManager);

        final me.aevum.aevumlobby.scoreboard.ScoreboardManager scoreboardManager = new me.aevum.aevumlobby.scoreboard.ScoreboardManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.scoreboard.ScoreboardManager.class, scoreboardManager);

        final me.aevum.aevumlobby.npc.NPCManager npcManager = new me.aevum.aevumlobby.npc.NPCManager(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.npc.NPCManager.class, npcManager);

        final me.aevum.aevumlobby.ai.AIFormatter aiFormatter = new me.aevum.aevumlobby.ai.AIFormatter(this, configManager);
        this.serviceRegistry.register(me.aevum.aevumlobby.ai.AIFormatter.class, aiFormatter);

        this.registerCommands();
        this.registerListeners();
    }

    private void registerCommands() {
        this.getLogger().info("Registering commands...");

        this.getCommand("aevumlobby").setExecutor(new me.aevum.aevumlobby.command.AevumLobbyCommand(this));
        this.getCommand("setlobby").setExecutor(new me.aevum.aevumlobby.command.SetLobbyCommand(this));
        this.getCommand("sethub").setExecutor(new me.aevum.aevumlobby.command.SetHubCommand(this));
        this.getCommand("hub").setExecutor(new me.aevum.aevumlobby.command.HubCommand(this));
        this.getCommand("lobby").setExecutor(new me.aevum.aevumlobby.command.HubCommand(this));
        this.getCommand("info").setExecutor(new me.aevum.aevumlobby.command.InfoCommand(this));
        this.getCommand("socials").setExecutor(new me.aevum.aevumlobby.command.SocialsCommand(this));
    }

    private void registerListeners() {
        this.getLogger().info("Registering listeners...");

        this.getServer().getPluginManager().registerEvents(new me.aevum.aevumlobby.listener.PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new me.aevum.aevumlobby.listener.PlayerQuitListener(this), this);
        this.getServer().getPluginManager().registerEvents(new me.aevum.aevumlobby.listener.ProtectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new me.aevum.aevumlobby.listener.HotbarInteractListener(this), this);
        this.getServer().getPluginManager().registerEvents(new me.aevum.aevumlobby.listener.GUIListener(this), this);
    }
}
