package de.devflare.aevumlobby.npc;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public final class NPCEntity {

    private final UUID uuid;
    private final String name;
    private final Location location;
    private final String texture;
    private final String signature;
    private int entityId;

    public NPCEntity(final String name, final Location location, final String texture, final String signature) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.location = location;
        this.texture = texture;
        this.signature = signature;
    }

    public void spawn(org.bukkit.entity.Player player) {
        // Packet logic will go here
    }

    public void remove(org.bukkit.entity.Player player) {
        // Packet logic will go here
    }

    public void remove() {
        // Global removal logic
    }
}
