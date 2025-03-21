package me.lagbug.emailer.spigot.utils;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import me.lagbug.emailer.spigot.Emailer;

public class PluginScheduler {
    public final Emailer plugin;
    public final FoliaLib foliaLib;
    public PluginScheduler(Emailer plugin) {
        this.plugin = plugin;
        this.foliaLib = new FoliaLib(plugin);
    }

    public WrappedTask scheduleSyncRepeatingTask(Runnable run, long delay, long period) {
        return foliaLib.getScheduler().runTimer(run, delay, period);
    }

    public void runTaskAsync(Runnable run) {
        foliaLib.getScheduler().runAsync((t) -> run.run());
    }

    public WrappedTask runTaskTimerAsync(Runnable run, long delay, long period) {
        return foliaLib.getScheduler().runTimerAsync(run, delay, period);
    }

    public void runTask(Runnable run) {
        foliaLib.getScheduler().runNextTick((t) -> run.run());
    }

    public void runTaskLater(Runnable run, long delay) {
        foliaLib.getScheduler().runLater(run, delay);
    }

    public void onDisable() {
        this.foliaLib.getScheduler().cancelAllTasks();
    }
}
