/*
 * JoinListener
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.listener;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.model.Box;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinListener implements Listener {

    private final List<OfflineUser> pendingUsers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (pendingUsers.isEmpty()) {
            errorKick(player);
            return;
        }

        OfflineUser offlineUser = pendingUsers.stream().filter(user -> user.getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);

        if (offlineUser == null) {
            errorKick(player);
            return;
        }

        pendingUsers.remove(offlineUser);


        User user = UserManager.setOnline(offlineUser, player);

        player.setPlayerListHeaderFooter(
                ChatUtils.format(CorePlugin.getInstance().getMainConfig().getTablist().getHeader()),
                ChatUtils.format(CorePlugin.getInstance().getMainConfig().getTablist().getFooter()));

        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
        if (tabPlayer != null && tabPlayer.isLoaded()) {
            user.setNametag();
        }

        user.hideVanished();

        user.addInfoPanelBox("time", CorePlugin.getInstance().getUpdateTimeBoxTask().getBox(), false);

        //user.setCoolBar("Test");
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();
        OfflineUser user = UserManager.getOfflineUserSync(uuid);

        if (user == null) {
            // Handle user not found
            UserManager.createNewUserSync(uuid);
            System.out.println("Created new user " + uuid);
            user = UserManager.getOfflineUserSync(uuid);
            if (user == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.format("&cThere was an error loading your data. Please try again later."));
                return;
            }
        }

        checkWhitelist(user, event);

        pendingUsers.add(user);
    }

    private void checkWhitelist(OfflineUser user, AsyncPlayerPreLoginEvent event) {
        List<String> whitelistedRanks = CorePlugin.getInstance().getMainConfig().getWhitelist().getWhitelistedRanks();
        if (CorePlugin.getInstance().getMainConfig().getWhitelist().isEnabled() && !whitelistedRanks.isEmpty()) {
            if (!whitelistedRanks.contains(user.getRank().getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, ChatUtils.format(CorePlugin.getInstance().getMainConfig().getWhitelist().getMessage()));
            }
        }
    }

    private void errorKick(Player player) {
        player.kickPlayer(ChatUtils.format("&cThere was an error loading your data. Please try again later."));
        System.out.println("Kicked player " + player.getName() + " because of error");
    }
}
