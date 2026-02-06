package me.aevum.aevumlobby.service;

public interface Lifecycle {

    void initialize();

    void shutdown();

    void reload();

    String getName();

    default int getPriority() {
        return 100;
    }
}
