package net.depthscape.core.listener;

import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        User user = User.getUser(event.getPlayer());
//        user.getNametagUpdateTask().cancel();
//        Bukkit.getOnlinePlayers().forEach(op -> {
//            Hologram hologram = user.getNametag();
//            if (hologram.getArmorStand() != null) hologram.remove(op);
//        });
        UserManager.removeUser(user);

    }
}
