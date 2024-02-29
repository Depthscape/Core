package net.depthscape.core.listener;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.config.model.MOTD;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        MOTD motd = CorePlugin.getInstance().getMainConfig().getMotd();

        String line1 = motd.getLine1();
        String line1formatted = ChatUtils.getCenteredMessage(ChatUtils.format(line1), ChatUtils.CenterPixel.MOTD);

        String line2 = motd.getLine2();
        String line2formatted = ChatUtils.getCenteredMessage(ChatUtils.format(line2), ChatUtils.CenterPixel.MOTD);

        event.setMotd(line1formatted + ChatColor.RESET + "\n" + line2formatted);
    }
}
