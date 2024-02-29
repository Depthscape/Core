/*
 * ChatListener
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.listener;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.event.OfflineUserAsyncChatEvent;
import net.depthscape.core.event.WebSocketClientRecieveDataEvent;
import net.depthscape.core.punishment.Punishment;
import net.depthscape.core.punishment.PunishmentManager;
import net.depthscape.core.socket.DataType;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        User user = User.getUser(event.getPlayer());
        String prefix = user.getRank().getChatPrefix();
        String name = user.getName();
        String message = event.getMessage();

        // check muted
        Punishment punishment = PunishmentManager.isMuted(user);
        if (punishment != null) {
            user.sendMessage(ChatUtils.getMuteMessageFormat(punishment));
            return;
        }

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

        if (!CorePlugin.isServer() && CorePlugin.getInstance().getWebSocketClient() != null) {
            // handle client chat
            JSONObject data = new JSONObject();
            data.put("server", CorePlugin.getInstance().getMainConfig().getServerName());
            data.put("player", user.getUniqueId());
            data.put("message", message);
            CorePlugin.getInstance().getWebSocketClient().send(DataType.CHAT_MESSAGE, data);
        } else {
            // handle server chat
            new BukkitRunnable() {
                @Override
                public void run() {
                    OfflineUserAsyncChatEvent chatEvent = new OfflineUserAsyncChatEvent(user, message, CorePlugin.getInstance().getMainConfig().getServerName());
                    Bukkit.getPluginManager().callEvent(chatEvent);
                }
            }.runTask(CorePlugin.getInstance());

        }

    }

    @EventHandler
    public void onNetworkChat(OfflineUserAsyncChatEvent event) {
        OfflineUser user = event.getUser();
        String message = event.getMessage();
        String server = event.getServer();

        System.out.println("User " + user.getName() + " sent message " + message + " from server " + server);
    }

    @EventHandler
    public void onDataRecieve(WebSocketClientRecieveDataEvent event) {
        System.out.println("Recieved data " + event.getType() + " " + event.getContent());
    }
}
