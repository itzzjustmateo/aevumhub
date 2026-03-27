package de.devflare.aevumlobby.command;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class SocialsCommand implements CommandExecutor {

    private final AevumLobbyPlugin plugin;

    public SocialsCommand(final AevumLobbyPlugin plugin) {
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

        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<gradient:#ff6b6b:#ffd93d>Social Media</gradient>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<aqua>Website: <white>" + configManager.getString("socials.website", "N/A")));
        sender.sendMessage(TextUtil.parse("<aqua>Discord: <white>" + configManager.getString("socials.discord", "N/A")));
        sender.sendMessage(TextUtil.parse("<aqua>YouTube: <white>" + configManager.getString("socials.youtube", "N/A")));
        sender.sendMessage(TextUtil.parse("<aqua>TikTok: <white>" + configManager.getString("socials.tiktok", "N/A")));
        sender.sendMessage(TextUtil.parse("<aqua>Twitch: <white>" + configManager.getString("socials.twitch", "N/A")));
        sender.sendMessage(TextUtil.parse(""));

        return true;
    }
}
