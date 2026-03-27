package de.devflare.aevumlobby.service;

public interface Lifecycle {

    void initialize();

    void shutdown();

    void reload();

    String getName();

    default int getPriority() {
        return 100;
    }
}
