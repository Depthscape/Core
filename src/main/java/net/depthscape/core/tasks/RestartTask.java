package net.depthscape.core.tasks;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.config.model.DailyRestart;
import net.depthscape.core.utils.ChatUtils;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class RestartTask extends BukkitRunnable {

    Map<Integer, String> messages;
    boolean popups;

    int seconds;

    public RestartTask() {
        DailyRestart config = CorePlugin.getInstance().getMainConfig().getDailyRestart();
        this.messages = config.getMessages();
        this.popups = config.isPopups();

        seconds = messages.keySet().stream().mapToInt(v -> v).max().orElse(0);
    }

    @Override
    public void run() {

        if (messages.containsKey(seconds)) {
            ChatUtils.broadcastMessage(messages.get(seconds));
            if (popups) ChatUtils.broadcastNotification(messages.get(seconds));
        }

        if (seconds == 0) {
            restart();
            this.cancel();
            return;
        }
        seconds--;
    }

    public void restart() {
        CorePlugin.setJoiningAllowed(false);
        if (CorePlugin.isServer()) {
            CorePlugin.getInstance().getWebSocketServer().broadcastRestart();
        }

        // kick all players
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Server restarting"));

        BackupTask.runBackup(file -> {
            Bukkit.getLogger().info("Restarting server");
            Bukkit.spigot().restart();
        });
    }

    public static void runRestart(boolean instant) {
       if (instant) {
           new RestartTask().restart();
       } else {
           new RestartTask().runTaskTimer(CorePlugin.getInstance(), 0, 20);
       }
    }
}