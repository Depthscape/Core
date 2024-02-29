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
import net.depthscape.core.punishment.Punishment;
import net.depthscape.core.punishment.PunishmentManager;
import net.depthscape.core.punishment.PunishmentType;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Date;
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

        // notify network that user has joined
        if (CorePlugin.isServer()) {

        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        if (!CorePlugin.isJoiningAllowed()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.format("&cThe server is not available to join. Please try again later."));
            return;
        }

        UUID uuid = event.getUniqueId();

        // check if user is banned
        // first check ip then uuid
        String ip = event.getAddress().getHostAddress();

        Punishment ipp = PunishmentManager.isBannedByIP(ip);
        if (ipp != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatUtils.format(ChatUtils.getBanMessageFormat(ipp)));
            return;
        }

        Punishment uuidp = PunishmentManager.isBannedByUUID(uuid);
        if (uuidp != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatUtils.format(ChatUtils.getBanMessageFormat(uuidp)));
            return;
        }

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

        boolean whitelisted = checkWhitelist(user, event);
        if (!whitelisted) {
            UserManager.removeOfflineUser(user);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, ChatUtils.format("&cYou are not whitelisted on this server."));
            return;
        }
        pendingUsers.add(user);
    }

    private boolean checkWhitelist(OfflineUser user, AsyncPlayerPreLoginEvent event) {
        List<String> whitelistedRanks = CorePlugin.getInstance().getMainConfig().getWhitelist().getWhitelistedRanks();
        if (CorePlugin.getInstance().getMainConfig().getWhitelist().isEnabled() && !whitelistedRanks.isEmpty()) {
            return whitelistedRanks.contains(user.getRank().getName());
        }
        return true;
    }

    private void errorKick(Player player) {
        player.kickPlayer(ChatUtils.format("&cThere was an error loading your data. Please try again later."));
        System.out.println("Kicked player " + player.getName() + " because of error");
    }
}
