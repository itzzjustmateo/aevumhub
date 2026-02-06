package me.aevum.aevumlobby.util;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class SchedulerUtil {

    private SchedulerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static BukkitTask runSync(final Runnable task) {
        return Bukkit.getScheduler().runTask(AevumLobbyPlugin.getInstance(), task);
    }

    public static BukkitTask runAsync(final Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(AevumLobbyPlugin.getInstance(), task);
    }

    public static BukkitTask runLater(final Runnable task, final long delay) {
        return Bukkit.getScheduler().runTaskLater(AevumLobbyPlugin.getInstance(), task, delay);
    }

    public static BukkitTask runLaterAsync(final Runnable task, final long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(AevumLobbyPlugin.getInstance(), task, delay);
    }

    public static BukkitTask runTimer(final Runnable task, final long delay, final long period) {
        return Bukkit.getScheduler().runTaskTimer(AevumLobbyPlugin.getInstance(), task, delay, period);
    }

    public static BukkitTask runTimerAsync(final Runnable task, final long delay, final long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(AevumLobbyPlugin.getInstance(), task, delay, period);
    }

    public static <T> CompletableFuture<T> supplyAsync(final Supplier<T> supplier) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        runAsync(() -> {
            try {
                future.complete(supplier.get());
            } catch (final Exception exception) {
                future.completeExceptionally(exception);
            }
        });

        return future;
    }

    public static <T> CompletableFuture<T> supplySync(final Supplier<T> supplier) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        runSync(() -> {
            try {
                future.complete(supplier.get());
            } catch (final Exception exception) {
                future.completeExceptionally(exception);
            }
        });

        return future;
    }
}
