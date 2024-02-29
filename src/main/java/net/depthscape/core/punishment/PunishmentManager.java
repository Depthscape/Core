package net.depthscape.core.punishment;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.model.Callback;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentManager {


    public static void punishPlayer(OfflineUser user, User moderator, PunishmentReason reason, Callback<Punishment> callback) {
        String userIp = user.getLastIp();
        if (userIp == null) {
            Bukkit.getLogger().severe("P3: An error occurred while punishing " + user.getName());
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), () -> {
            int count = getPunishmentsCount(user, reason);
            PunishmentType type = reason.getType(count);
            long duration = reason.getDuration(count);

            insertNewPunishment(user, userIp, moderator, type, reason, duration, punishment -> {
                Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> {
                    handlePunishment(punishment);
                    callback.call(punishment);
                });
            });
        });
    }

    private static void handlePunishment(Punishment punishment) {
        OfflineUser player = punishment.getPlayer();
        User user = player.getUser();
        if (user == null) {
            Bukkit.getLogger().severe("P2: An error occurred while handling punishment for user: " + player.getName());
            return;
        }

        switch (punishment.getType()) {
            case TEMP_BAN, IP_BAN -> {
                user.getPlayer().kickPlayer(ChatUtils.format(ChatUtils.getBanMessageFormat(punishment)));
            }
            case MUTE -> {
                user.setMuted(true);
                user.sendMessage(ChatUtils.getMuteMessageFormat(punishment));
            }
        }
    }

    private static void insertNewPunishment(OfflineUser player, String ip, OfflineUser moderator, PunishmentType type, PunishmentReason reason, long duration, Callback<Punishment> callback) {


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = duration == -1 ? null : now.plusSeconds(duration);


        Punishment punishment = new Punishment(player, ip, moderator, type, reason, now, expires, false);
        callback.call(punishment);

        if (expires == null) {
            DatabaseUtils.executeUpdateAsync(
                    "INSERT INTO `punishments` (`uuid`, `ip`, `moderator`, `type`, `reason`, `issued`) VALUES (" +
                            "'" + player.getUniqueId() + "', " +
                            "'" + ip + "', " +
                            "'" + moderator.getUniqueId() + "', " +
                            "'" + type.name() + "', " +
                            "'" + reason.getReason() + "', " +
                            "'" + ChatUtils.formatDate(now) + "')"
            );
            return;
        }

        DatabaseUtils.executeUpdateAsync(
                "INSERT INTO `punishments` (`uuid`, `ip`, `moderator`, `type`, `reason`, `issued`, `expires`) VALUES (" +
                        "'" + player.getUniqueId() + "', " +
                        "'" + ip + "', " +
                        "'" + moderator.getUniqueId() + "', " +
                        "'" + type.name() + "', " +
                        "'" + reason.getReason() + "', " +
                        "'" + ChatUtils.formatDate(now) + "', " +
                        "'" + ChatUtils.formatDate(expires) + "')"
        );
    }

    public static List<Punishment> getPunishments(OfflineUser user, PunishmentReason reason) {
        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM `punishments` WHERE `uuid` = '" + user.getUniqueId() + "' AND `reason` = '" + reason.getReason() + "'");
        if (resultSet == null) {
            return new ArrayList<>();
        }
        List<Punishment> punishments = new ArrayList<>();
        try {
            while (resultSet.next()) {
                punishments.add(Punishment.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("P1: An error occurred while getting punishments for " + user.getName());
        }
        return punishments;
    }

    public static int getPunishmentsCount(OfflineUser user, PunishmentReason reason) {
        List<Punishment> punishments = getPunishments(user, reason);
        return punishments.size();
    }

    /**
     * Check if a player is banned by UUID
     *
     * @param uuid The UUID of the player
     * @return The punishment if the player is banned, null otherwise
     */
    public static Punishment isBannedByUUID(UUID uuid) {
        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM `punishments` WHERE `uuid` = '" + uuid + "' AND `type` = 'TEMP_BAN' AND NOT `cancelled`");
        if (resultSet == null) {
            return null;
        }
        try {
            if (resultSet.next()) {
                return Punishment.fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("An error occurred while checking if " + uuid + " is banned");
        }
        return null;
    }

    /**
     * Check if a player is banned by IP
     *
     * @param ip The IP of the player
     * @return The punishment if the player is banned, null otherwise
     */
    public static Punishment isBannedByIP(String ip) {
        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM `punishments` WHERE `ip` = '" + ip + "' AND `type` = 'IP_BAN' AND NOT `cancelled`");
        if (resultSet == null) {
            return null;
        }
        try {
            if (resultSet.next()) {
                return Punishment.fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("An error occurred while checking if " + ip + " is banned");
        }
        return null;
    }

    /**
     * Check if a player is muted
     *
     * @param user The user to check
     * @return The punishment if the player is muted, null otherwise
     */
    public static Punishment isMuted(OfflineUser user) {
        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM `punishments` WHERE `uuid` = '" + user.getUniqueId() + "' AND `type` = 'MUTE' AND NOT `cancelled`");
        if (resultSet == null) {
            return null;
        }
        try {
            if (resultSet.next()) {
                return Punishment.fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("An error occurred while checking if " + user.getName() + " is muted");
        }
        return null;
    }
}