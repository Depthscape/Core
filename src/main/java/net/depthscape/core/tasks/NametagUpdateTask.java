package net.depthscape.core.tasks;

import lombok.AllArgsConstructor;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class NametagUpdateTask extends BukkitRunnable {

    private User user;
    private Hologram hologram;

    @Override
    public void run() {
        hologram.setLocation(user.getPlayer().getLocation());
        Bukkit.getOnlinePlayers().stream()
                //.filter(op -> !op.getUniqueId().equals(user.getUniqueId()))
                .forEach(hologram::refresh);
    }

    public void start() {
        runTaskTimer(CorePlugin.getInstance(), 1, 1L);
        //runTask(CorePlugin.getInstance());
    }
}
