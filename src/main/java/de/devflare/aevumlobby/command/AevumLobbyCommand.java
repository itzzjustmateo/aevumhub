package de.devflare.aevumlobby.command;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.util.TextUtil;
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
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
            @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission("aevumlobby.admin")) {
            sender.sendMessage(TextUtil.parse("<red>You don't have permission to use this command.</red>"));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
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
        sender.sendMessage(TextUtil.parse(
                "<st><gray>      </gray></st> <gradient:#ff6b6b:#ffd93d><bold>AEVUMLOBBY HELP</bold></gradient> <st><gray>      </gray></st>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<yellow><bold>ADMIN COMMANDS</bold></yellow>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/al reload</white> <gray>-</gray> <italic>Reload the plugin</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/al debug</white> <gray>-</gray> <italic>Toggle debug info</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/al status</white> <gray>-</gray> <italic>Check service health</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/sethub</white> <gray>-</gray> <italic>Set the hub spawn point</italic>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<yellow><bold>NPC SYSTEM</bold></yellow>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/hubnpc spawn <name> [skin]</white> <gray>-</gray> <italic>Create an NPC</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/hubnpc remove <name></white> <gray>-</gray> <italic>Delete an NPC</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/hubnpc list</white> <gray>-</gray> <italic>List all NPCs</italic>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<yellow><bold>PLAYER COMMANDS</bold></yellow>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/hub</white> <gray>-</gray> <italic>Back to spawn</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/info</white> <gray>-</gray> <italic>Server status and info</italic>"));
        sender.sendMessage(TextUtil.parse(
                " <gradient:#ffd93d:#ff6b6b>»</gradient> <white>/socials</white> <gray>-</gray> <italic>Links and community</italic>"));
        sender.sendMessage(TextUtil.parse(""));
        sender.sendMessage(TextUtil.parse("<st><gray>                                          </gray></st>"));
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
        sender.sendMessage(
                TextUtil.parse("<gray>Services: <white>" + this.plugin.getServiceRegistry().getServices().size()));
        sender.sendMessage(
                TextUtil.parse("<gray>Players: <white>" + this.plugin.getServer().getOnlinePlayers().size()));
    }

    private void handleStatus(final CommandSender sender) {
        sender.sendMessage(TextUtil.parse("<gradient:#ff6b6b:#ffd93d>AevumLobby Status:</gradient>"));
        sender.sendMessage(TextUtil.parse("<gray>Version: <white>" + this.plugin.getDescription().getVersion()));
        sender.sendMessage(TextUtil
                .parse("<gray>Registered Services: <white>" + this.plugin.getServiceRegistry().getServices().size()));

        this.plugin.getServiceRegistry().getServices().forEach(service -> {
            final String status = service.isEnabled() ? "<green>✓</green>" : "<red>✗</red>";
            sender.sendMessage(TextUtil.parse(status + " <gray>" + service.getName()));
        });
    }
}
