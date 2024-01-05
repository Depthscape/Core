package net.depthscape.core.tasks;

import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoClipCheckTask extends BukkitRunnable {

    @Override
    public void run() {
        checkPlayers();
    }

    private void checkPlayers() {
        for (User user: UserManager.getOnlineUsers()) {
            if (user.isNoClipEnabled()) {
                if (user.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    boolean noClip;
                    if (user.getPlayer().getLocation().add(0.0D, -0.1D, 0.0D).getBlock().getType().isSolid() && user.getPlayer().isSneaking()) {
                        noClip = true;
                    } else {
                        noClip = shouldNoClip(user.getPlayer());
                    }
                    if (noClip)
                        user.getPlayer().setGameMode(GameMode.SPECTATOR);
                    continue;
                }
                if (user.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                    boolean noClip;
                    if (user.getPlayer().getLocation().add(0.0D, -0.1D, 0.0D).getBlock().getType().isSolid()) {
                        noClip = true;
                    } else {
                        noClip = shouldNoClip(user.getPlayer());
                    }
                    if (!noClip)
                        user.getPlayer().setGameMode(GameMode.CREATIVE);
                }
            }
        }
    }

    private boolean shouldNoClip(Player player) {
        return (player.getLocation().add(0.4D, 0.0D, 0.0D).getBlock().getType().isSolid() || player
                .getLocation().add(-0.4D, 0.0D, 0.0D).getBlock().getType().isSolid() || player
                .getLocation().add(0.0D, 0.0D, 0.4D).getBlock().getType().isSolid() || player
                .getLocation().add(0.0D, 0.0D, -0.4D).getBlock().getType().isSolid() || player
                .getLocation().add(0.4D, 1.0D, 0.0D).getBlock().getType().isSolid() || player
                .getLocation().add(-0.4D, 1.0D, 0.0D).getBlock().getType().isSolid() || player
                .getLocation().add(0.0D, 1.0D, 0.4D).getBlock().getType().isSolid() || player
                .getLocation().add(0.0D, 1.0D, -0.4D).getBlock().getType().isSolid() || player
                .getLocation().add(0.0D, 1.9D, 0.0D).getBlock().getType().isSolid());
    }
}
