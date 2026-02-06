package me.aevum.aevumlobby.cache;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.service.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class CacheManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final Map<UUID, PlayerCache> playerCaches;
    private boolean enabled;

    public CacheManager(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
        this.playerCaches = new ConcurrentHashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing cache system...");
    }

    @Override
    public void shutdown() {
        this.playerCaches.clear();
    }

    @Override
    public void reload() {
    }

    @Override
    public String getName() {
        return "CacheManager";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    public PlayerCache getOrCreateCache(final UUID uuid) {
        return this.playerCaches.computeIfAbsent(uuid, PlayerCache::new);
    }

    public Optional<PlayerCache> getCache(final UUID uuid) {
        return Optional.ofNullable(this.playerCaches.get(uuid));
    }

    public void removeCache(final UUID uuid) {
        this.playerCaches.remove(uuid);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
