package me.aevum.aevumlobby.scoreboard;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;
import me.aevum.aevumlobby.util.SchedulerUtil;
import me.aevum.aevumlobby.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class ScoreboardManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final ConcurrentHashMap<UUID, Scoreboard> scoreboards;
    private BukkitTask updateTask;
    private int updateInterval;
    private String title;
    private List<String> lines;
    private boolean enabled;

    public ScoreboardManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.scoreboards = new ConcurrentHashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing scoreboard system...");
        this.loadSettings();
        this.startUpdateTask();
    }

    @Override
    public void shutdown() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
        this.scoreboards.clear();
    }

    @Override
    public void reload() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
        this.loadSettings();
        this.startUpdateTask();
        this.plugin.getLogger().info("Scoreboard system reloaded");
    }

    @Override
    public String getName() {
        return "ScoreboardManager";
    }

    @Override
    public int getPriority() {
        return 80;
    }

    private void loadSettings() {
        this.updateInterval = this.configManager.getInt("scoreboard.update-interval", 20);
        this.title = this.configManager.getString("scoreboard.title", "<gradient:#ff6b6b:#ffd93d>AevumMC</gradient>");
        this.lines = this.configManager.getStringList("scoreboard.lines");
    }

    private void startUpdateTask() {
        if (!this.enabled) {
            return;
        }

        this.updateTask = SchedulerUtil.runTimer(() -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                this.updateScoreboard(player);
            }
        }, 20L, this.updateInterval);
    }

    public void createScoreboard(final Player player) {
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("aevumlobby", "dummy", TextUtil.parse(this.title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreboards.put(player.getUniqueId(), scoreboard);
        player.setScoreboard(scoreboard);

        this.updateScoreboard(player);
    }

    public void removeScoreboard(final Player player) {
        this.scoreboards.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    private void updateScoreboard(final Player player) {
        final Scoreboard scoreboard = this.scoreboards.get(player.getUniqueId());
        if (scoreboard == null) {
            return;
        }

        final Objective objective = scoreboard.getObjective("aevumlobby");
        if (objective == null) {
            return;
        }

        for (final String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int score = this.lines.size();
        for (final String line : this.lines) {
            final String parsed = this.parsePlaceholders(line, player);
            objective.getScore(parsed).setScore(score--);
        }
    }

    private String parsePlaceholders(String text, final Player player) {
        text = text.replace("{player}", player.getName());
        text = text.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replace("{max}", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replace("{world}", player.getWorld().getName());
        return text;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
