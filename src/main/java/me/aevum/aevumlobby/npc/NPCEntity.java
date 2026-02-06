package me.aevum.aevumlobby.npc;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public final class NPCEntity {

    private final UUID uuid;
    private final String name;
    private final Location location;

    public NPCEntity(final String name, final Location location) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.location = location;
    }

    public void spawn() {
    }

    public void remove() {
    }
}
