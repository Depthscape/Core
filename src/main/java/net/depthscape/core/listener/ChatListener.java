/*
 * ChatListener
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.listener;

import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        User user = User.getUser(event.getPlayer());
        String prefix = user.getRank().getChatPrefix();
        String name = user.getName();
        String message = event.getMessage();

        if (user.isStaffChatSendEnabled()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User target = User.getUser(player);
                if (target.getRank().isStaff()) {
                    target.sendMessage(ChatUtils.getStaffChatFormat()
                            .replace("%prefix%", prefix)
                            .replace("%player%", name)
                            .replace("%message%", message)
                    );
                }
            }
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            User target = User.getUser(player);
            target.sendMessage(ChatUtils.getChatFormat()
                    .replace("%prefix%", prefix)
                    .replace("%player%", name)
                    .replace("%message%", message)
            );
        }

    }
}
