package de.devflare.aevumlobby.command;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class InfoCommand implements CommandExecutor {

    private final AevumLobbyPlugin plugin;

    public InfoCommand(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        final ConfigManager configManager = this.plugin.getServiceRegistry()
                .getService(ConfigManager.class)
                .orElse(null);

        if (configManager == null) {
            sender.sendMessage(TextUtil.parse("<red>Configuration manager is not available."));
            return true;
        }

        final String serverName = configManager.getString("general.plugin-name", "AevumMC");
        final int online = Bukkit.getOnlinePlayers().size();
        final int max = Bukkit.getMaxPlayers();

        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<gradient:#ff6b6b:#ffd93d>" + serverName + "</gradient>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<gray>Players: <white>" + online + "/" + max));
        sender.sendMessage(TextUtil.parse("<gray>Website: <white>" + configManager.getString("socials.website", "N/A")));
        sender.sendMessage(TextUtil.parse(""));

        return true;
    }
}
