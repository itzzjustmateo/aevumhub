package de.devflare.aevumlobby.command;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.npc.NPCEntity;
import de.devflare.aevumlobby.npc.NPCManager;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HubNPCCommand implements CommandExecutor {

    private final NPCManager npcManager;

    public HubNPCCommand(AevumLobbyPlugin plugin) {
        this.npcManager = plugin.getServiceRegistry().getService(NPCManager.class).orElse(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextUtil.parse("<red>Only players can use this command.</red>"));
            return true;
        }

        if (!player.hasPermission("aevumhub.admin")) {
            player.sendMessage(TextUtil.parse("<red>You don't have permission to use this command.</red>"));
            return true;
        }

        if (args.length == 0) {
            this.sendUsage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "spawn" -> this.handleSpawn(player, args);
            case "remove" -> this.handleRemove(player, args);
            case "list" -> this.handleList(player);
            default -> this.sendUsage(player);
        }

        return true;
    }

    private void handleSpawn(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(TextUtil.parse("<red>Usage: /hubnpc spawn <name> [skin_name]</red>"));
            return;
        }

        String name = args[1];
        String skinName = args.length > 2 ? args[2] : name;

        player.sendMessage(TextUtil.parse("<yellow>Spawning NPC <white>" + name + "</white> with skin <white>"
                + skinName + "</white>...</yellow>"));

        NPCEntity npc = new NPCEntity(name, player.getLocation(), skinName, null);
        npcManager.registerNPC(npc);
        npc.spawn(player);

        player.sendMessage(TextUtil.parse("<green>NPC spawned successfully!</green>"));
    }

    private void handleRemove(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(TextUtil.parse("<red>Usage: /hubnpc remove <name></red>"));
            return;
        }

        String name = args[1];
        if (npcManager.removeNPCByName(name)) {
            player.sendMessage(TextUtil.parse("<green>NPC <white>" + name + "</white> removed successfully!</green>"));
        } else {
            player.sendMessage(TextUtil.parse("<red>Could not find an NPC named <white>" + name + "</white>.</red>"));
        }
    }

    private void handleList(Player player) {
        Collection<NPCEntity> npcs = npcManager.getNpcs().values();
        if (npcs.isEmpty()) {
            player.sendMessage(TextUtil.parse("<red>There are no NPCs registered.</red>"));
            return;
        }

        player.sendMessage(TextUtil
                .parse("<gradient:#ff6b6b:#ffd93d><bold>Registered NPCs (" + npcs.size() + "):</bold></gradient>"));
        for (NPCEntity npc : npcs) {
            player.sendMessage(TextUtil.parse(" <gradient:#ffd93d:#ff6b6b>»</gradient> <white>" + npc.getName()
                    + " <gray>(Location: " + formatLocation(npc.getLocation()) + ")"));
        }
    }

    private String formatLocation(org.bukkit.Location loc) {
        return loc.getWorld().getName() + " " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    private void sendUsage(Player player) {
        player.sendMessage(TextUtil.parse("<red>Usage: /hubnpc <spawn|remove|list></red>"));
    }
}
