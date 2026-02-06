package me.aevum.aevumlobby.command;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class AevumLobbyCommand implements CommandExecutor {

    private final AevumLobbyPlugin plugin;

    public AevumLobbyCommand(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission("aevumlobby.admin")) {
            sender.sendMessage(TextUtil.parse("<red>You don't have permission to use this command.</red>"));
            return true;
        }

        if (args.length == 0) {
            this.sendUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> this.handleReload(sender);
            case "debug" -> this.handleDebug(sender);
            case "status" -> this.handleStatus(sender);
            default -> this.sendUsage(sender);
        }

        return true;
    }

    private void sendUsage(final CommandSender sender) {
        sender.sendMessage(TextUtil.parse("<gradient:#ff6b6b:#ffd93d>AevumLobby Commands:</gradient>"));
        sender.sendMessage(TextUtil.parse("<gray>/aevumlobby reload - Reload configuration"));
        sender.sendMessage(TextUtil.parse("<gray>/aevumlobby debug - Toggle debug mode"));
        sender.sendMessage(TextUtil.parse("<gray>/aevumlobby status - View system status"));
    }

    private void handleReload(final CommandSender sender) {
        sender.sendMessage(TextUtil.parse("<yellow>Reloading AevumLobby..."));

        try {
            this.plugin.getServiceRegistry().reloadAll();
            sender.sendMessage(TextUtil.parse("<green>Successfully reloaded AevumLobby!"));
        } catch (final Exception exception) {
            sender.sendMessage(TextUtil.parse("<red>Failed to reload AevumLobby. Check console for errors."));
            exception.printStackTrace();
        }
    }

    private void handleDebug(final CommandSender sender) {
        sender.sendMessage(TextUtil.parse("<yellow>Debug information:"));
        sender.sendMessage(TextUtil.parse("<gray>Services: <white>" + this.plugin.getServiceRegistry().getServices().size()));
        sender.sendMessage(TextUtil.parse("<gray>Players: <white>" + this.plugin.getServer().getOnlinePlayers().size()));
    }

    private void handleStatus(final CommandSender sender) {
        sender.sendMessage(TextUtil.parse("<gradient:#ff6b6b:#ffd93d>AevumLobby Status:</gradient>"));
        sender.sendMessage(TextUtil.parse("<gray>Version: <white>" + this.plugin.getDescription().getVersion()));
        sender.sendMessage(TextUtil.parse("<gray>Registered Services: <white>" + this.plugin.getServiceRegistry().getServices().size()));

        this.plugin.getServiceRegistry().getServices().forEach(service -> {
            final String status = service.isEnabled() ? "<green>✓</green>" : "<red>✗</red>";
            sender.sendMessage(TextUtil.parse(status + " <gray>" + service.getName()));
        });
    }
}
