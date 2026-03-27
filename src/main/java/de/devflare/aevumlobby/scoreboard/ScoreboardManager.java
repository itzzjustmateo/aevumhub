package de.devflare.aevumlobby.scoreboard;

import lombok.Getter;
import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.service.Service;
import de.devflare.aevumlobby.util.SchedulerUtil;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

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
        final Objective objective = scoreboard.registerNewObjective("aevumlobby", org.bukkit.scoreboard.Criteria.DUMMY,
                TextUtil.parse(this.title));
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

        // Use lines from config
        int scoreValue = this.lines.size();

        for (int i = 0; i < this.lines.size(); i++) {
            final String line = this.lines.get(i);
            final String parsed = this.parsePlaceholders(line, player);

            // Use invisible colors as unique keys for entries to prevent flickering
            String entryKey = ChatColor.values()[i].toString();

            Score score = objective.getScore(entryKey);
            score.setScore(scoreValue);

            // Set the custom name using parsed component (supports MiniMessage & Legacy)
            score.customName(TextUtil.parse(parsed));

            scoreValue--;
        }

        // Clean up any extra lines if list shrank
        for (int i = this.lines.size(); i < ChatColor.values().length; i++) {
            String entryKey = ChatColor.values()[i].toString();
            // Check if this entry exists in the scoreboard and remove it if so
            // Since we can't easily check existence without iterating all entries,
            // and we shouldn't reset indiscriminately, we assume our key strategy is
            // consistent.
            // But resetting a non-existent score is harmless.
            scoreboard.resetScores(entryKey);
        }
    }

    private String parsePlaceholders(String text, final Player player) {
        text = text.replace("{player}", player.getName())
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{max}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{max_players}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{world}", player.getWorld().getName());
        return text;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
