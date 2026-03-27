package de.devflare.aevumlobby.npc;

import lombok.Getter;
import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.service.Service;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class NPCManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Map<UUID, NPCEntity> npcs;
    private final FileConfiguration npcConfig;
    private final File npcFile;
    private boolean enabled;

    public NPCManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.npcs = new ConcurrentHashMap<>();
        this.npcFile = new File(plugin.getDataFolder(), "npcs.yml");
        if (!npcFile.exists()) {
            plugin.saveResource("npcs.yml", false);
        }
        this.npcConfig = YamlConfiguration.loadConfiguration(npcFile);
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing NPC system...");
        this.loadNPCs();
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
        this.saveNPCs();
    }

    public void removeNPC(final UUID uuid) {
        final NPCEntity npc = this.npcs.remove(uuid);
        if (npc != null) {
            npc.remove();
            this.saveNPCs();
        }
    }

    public boolean removeNPCByName(final String name) {
        final UUID uuid = this.npcs.values().stream()
                .filter(npc -> npc.getName().equalsIgnoreCase(name))
                .map(NPCEntity::getUuid)
                .findFirst()
                .orElse(null);

        if (uuid != null) {
            this.removeNPC(uuid);
            return true;
        }
        return false;
    }

    private void loadNPCs() {
        if (!npcConfig.contains("npcs"))
            return;

        final ConfigurationSection section = npcConfig.getConfigurationSection("npcs");
        if (section == null)
            return;

        for (final String key : section.getKeys(false)) {
            final ConfigurationSection npcSection = section.getConfigurationSection(key);
            if (npcSection == null)
                continue;

            final String name = npcSection.getString("name");
            final String worldName = npcSection.getString("location.world");
            final double x = npcSection.getDouble("location.x");
            final double y = npcSection.getDouble("location.y");
            final double z = npcSection.getDouble("location.z");
            final float yaw = (float) npcSection.getDouble("location.yaw");
            final float pitch = (float) npcSection.getDouble("location.pitch");

            final org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
            if (world == null)
                continue;

            final Location location = new Location(world, x, y, z, yaw, pitch);
            final String texture = npcSection.getString("skin.texture");
            final String signature = npcSection.getString("skin.signature");

            final NPCEntity npc = new NPCEntity(name, location, texture, signature);
            this.npcs.put(npc.getUuid(), npc);
        }
    }

    private void saveNPCs() {
        npcConfig.set("npcs", null);
        for (final NPCEntity npc : npcs.values()) {
            final String key = npc.getName().toLowerCase().replace(" ", "_");
            final ConfigurationSection npcSection = npcConfig.createSection("npcs." + key);
            npcSection.set("name", npc.getName());
            npcSection.set("location.world", npc.getLocation().getWorld().getName());
            npcSection.set("location.x", npc.getLocation().getX());
            npcSection.set("location.y", npc.getLocation().getY());
            npcSection.set("location.z", npc.getLocation().getZ());
            npcSection.set("location.yaw", npc.getLocation().getYaw());
            npcSection.set("location.pitch", npc.getLocation().getPitch());
            npcSection.set("skin.texture", npc.getTexture());
            npcSection.set("skin.signature", npc.getSignature());
        }

        try {
            npcConfig.save(npcFile);
        } catch (final Exception e) {
            plugin.getLogger().severe("Could not save npcs.yml!");
            e.printStackTrace();
        }
    }

    public void handleJoin(final org.bukkit.entity.Player player) {
        this.npcs.values().forEach(npc -> npc.spawn(player));
    }

    public void handleQuit(final org.bukkit.entity.Player player) {
        this.npcs.values().forEach(npc -> npc.remove(player));
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
