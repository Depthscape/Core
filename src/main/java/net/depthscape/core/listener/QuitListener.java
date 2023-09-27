package net.depthscape.core.listener;

import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        User user = User.getUser(event.getPlayer());
        UserManager.removeUser(user);
    }
}
