package me.aevum.aevumlobby.service;

public interface Service extends Lifecycle {

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
