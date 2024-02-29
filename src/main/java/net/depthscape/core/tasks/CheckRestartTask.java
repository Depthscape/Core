package net.depthscape.core.tasks;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.config.model.DailyRestart;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CheckRestartTask extends BukkitRunnable {
    @Override
    public void run() {

        DailyRestart config = CorePlugin.getInstance().getMainConfig().getDailyRestart();

        LocalDateTime now = LocalDateTime.now();
        LocalTime time = LocalTime.ofSecondOfDay(now.toLocalTime().toSecondOfDay());
        LocalTime restartTime = CorePlugin.getInstance().getMainConfig().getDailyRestart().getTime();

        int max = config.getMessages().keySet().stream().mapToInt(v -> v).max().orElse(0);
        LocalTime startTime = restartTime.minusSeconds(max);

        if (time.equals(startTime)) {
            Bukkit.getLogger().info("Daily midnight restart in 10 seconds");
            RestartTask.runRestart(false);
        }
    }
}
