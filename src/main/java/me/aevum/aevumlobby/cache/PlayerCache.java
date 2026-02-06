package me.aevum.aevumlobby.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public final class PlayerCache {

    private final UUID uuid;
    private final Map<String, Object> data;
    private boolean playersVisible;
    private long lastInteraction;

    public PlayerCache(final UUID uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
        this.playersVisible = true;
        this.lastInteraction = System.currentTimeMillis();
    }

    public void set(final String key, final Object value) {
        this.data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final Class<T> clazz) {
        return (T) this.data.get(key);
    }

    public <T> T get(final String key, final Class<T> clazz, final T defaultValue) {
        final Object value = this.data.get(key);
        return value != null ? clazz.cast(value) : defaultValue;
    }

    public boolean has(final String key) {
        return this.data.containsKey(key);
    }

    public void remove(final String key) {
        this.data.remove(key);
    }

    public void updateInteraction() {
        this.lastInteraction = System.currentTimeMillis();
    }
}
