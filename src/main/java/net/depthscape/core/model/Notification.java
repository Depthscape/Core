package net.depthscape.core.model;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class Notification {

    private final BossBar bossBar;

    private final BukkitRunnable runnable;

    public Notification(User user, String message, Callback<Notification> callback) {

        String modifiedMessage = ChatUtils.format(message);

        bossBar = Bukkit.getServer().createBossBar(modifiedMessage, BarColor.WHITE, BarStyle.SOLID);

        bossBar.addPlayer(user.getPlayer());
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.removePlayer(user.getPlayer());
            }
        };

        runnable.runTaskLater(CorePlugin.getInstance(), 20 * 3);

        callback.call(this);
    }

    public void remove() {
        runnable.cancel();
        bossBar.removeAll();
    }
}
