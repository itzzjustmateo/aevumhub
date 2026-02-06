package me.aevum.aevumlobby.ai;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;

import java.util.List;
import java.util.Random;

@Getter
public final class AIFormatter implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Random random;
    private boolean enabled;

    public AIFormatter(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.random = new Random();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing AI formatter...");
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void reload() {
        this.plugin.getLogger().info("AI formatter reloaded");
    }

    @Override
    public String getName() {
        return "AIFormatter";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    public String format(final String template, final String... placeholders) {
        final List<String> templates = this.configManager.getAiTemplates().getStringList("templates." + template);

        if (templates.isEmpty()) {
            return template;
        }

        String result = templates.get(this.random.nextInt(templates.size()));

        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                result = result.replace(placeholders[i], placeholders[i + 1]);
            }
        }

        return result;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
