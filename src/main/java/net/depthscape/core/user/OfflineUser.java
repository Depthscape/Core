/*
 * OfflineUser
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.exception.PlayerNullException;
import net.depthscape.core.model.Callback;
import net.depthscape.core.punishment.PunishmentManager;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OfflineUser {

    private UUID uniqueId;
    private String name;
    private Rank rank;
    private int coins;
    private boolean vanished;
    private long discordId;
    private String lastIp;

    private boolean isMuted;

    public OfflineUser(ResultSet data) {

        try {
            uniqueId = UUID.fromString(data.getString("uuid"));
            name = data.getString("name");
            rank = RankManager.getRank(data.getString("rank"));
            coins = data.getInt("coins");
            vanished = data.getBoolean("vanished");
            discordId = data.getLong("discord_id");
            lastIp = data.getString("last_ip");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public OfflineUser(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().info("Player is null");
            return;
        }
        this.uniqueId = uuid;
        this.name = player.getName();
        this.rank = RankManager.getDefaultRank();
        this.coins = 0;
        this.vanished = false;
        this.discordId = 0;
        this.lastIp = player.getAddress().getAddress().getHostAddress();
    }

    public static void getOfflineUser(String name, Callback<OfflineUser> callback) {
        UserManager.getOfflineUser(name, callback);
    }

    public static void getOfflineUser(UUID uuid, Callback<OfflineUser> callback) {
        UserManager.getOfflineUser(uuid, callback);
    }

    public static void getOfflineUser(OfflinePlayer player, Callback<OfflineUser> callback) {
        UserManager.getOfflineUser(player.getUniqueId(), callback);
    }

    public User getUser() {
        return UserManager.getUser(uniqueId);
    }
}
