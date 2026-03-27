package de.devflare.aevumlobby.bootstrap;

import lombok.Getter;
import de.devflare.aevumlobby.AevumLobbyPlugin;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@Getter
public final class BootstrapManager {

    private final AevumLobbyPlugin plugin;
    private final Logger logger;
    private boolean bootstrapped;

    public BootstrapManager(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.bootstrapped = false;
    }

    public void bootstrap() {
        if (this.bootstrapped) {
            throw new IllegalStateException("Plugin is already bootstrapped");
        }

        this.logger.info("========================================");
        this.logger.info("     AevumLobby v" + this.plugin.getDescription().getVersion());
        this.logger.info("     Bootstrapping plugin...");
        this.logger.info("========================================");

        this.validateEnvironment();

        this.logger.info("Bootstrap completed successfully");
        this.bootstrapped = true;
    }

    private void validateEnvironment() {
        this.logger.info("Validating environment...");

        final String version = this.plugin.getServer().getVersion();
        this.logger.info("Server: " + version);

        final String bukkitVersion = this.plugin.getServer().getBukkitVersion();
        this.logger.info("Bukkit API: " + bukkitVersion);

        final String javaVersion = System.getProperty("java.version");
        this.logger.info("Java: " + javaVersion);

        final int majorVersion = Integer.parseInt(javaVersion.split("\\.")[0]);
        if (majorVersion < 21) {
            throw new IllegalStateException("Java 21 or higher is required. Current: " + javaVersion);
        }

        this.logger.info("Environment validation passed");
    }

    public void performShutdown() {
        if (!this.bootstrapped) {
            return;
        }

        this.logger.info("========================================");
        this.logger.info("     Shutting down AevumLobby...");
        this.logger.info("========================================");

        this.bootstrapped = false;
        this.logger.info("Shutdown completed");
    }
}
