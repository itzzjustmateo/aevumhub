package de.devflare.aevumlobby.npc;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NPCListener implements Listener {

    private final NPCManager npcManager;

    public NPCListener(AevumLobbyPlugin plugin) {
        this.npcManager = plugin.getServiceRegistry().getService(NPCManager.class).orElse(null);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        npcManager.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        npcManager.handleQuit(event.getPlayer());
    }
}
