package me.aevum.aevumlobby.npc;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class NPCManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Map<UUID, NPCEntity> npcs;
    private boolean enabled;

    public NPCManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.npcs = new ConcurrentHashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing NPC system...");
    }

    @Override
    public void shutdown() {
        this.npcs.values().forEach(NPCEntity::remove);
        this.npcs.clear();
    }

    @Override
    public void reload() {
        this.plugin.getLogger().info("NPC system reloaded");
    }

    @Override
    public String getName() {
        return "NPCManager";
    }

    @Override
    public int getPriority() {
        return 90;
    }

    public void registerNPC(final NPCEntity npc) {
        this.npcs.put(npc.getUuid(), npc);
    }

    public void removeNPC(final UUID uuid) {
        final NPCEntity npc = this.npcs.remove(uuid);
        if (npc != null) {
            npc.remove();
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
