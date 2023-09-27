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
import net.depthscape.core.rank.Rank;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;
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

    public OfflineUser(ResultSet data) {

        try {
            uniqueId = UUID.fromString(data.getString("uuid"));
            name = data.getString("name");
            rank = RankManager.getRank(data.getString("rank"));
            coins = data.getInt("coins");
            vanished = data.getBoolean("vanished");
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
    }

}
