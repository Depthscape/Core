package net.depthscape.core.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Punishment {

    private OfflineUser player;
    private String ip;
    private OfflineUser moderator;
    private PunishmentType type;
    private PunishmentReason reason;
    private LocalDateTime issued;
    private LocalDateTime expires;
    private boolean cancelled;


    public boolean isExpired() {
        if (isPermanent()) return false;
        return LocalDateTime.now().isAfter(expires);
    }

    public boolean isPermanent() {
        return expires == null;
    }

    public long getDuration() {
        if (isPermanent()) return -1;
        return ChronoUnit.SECONDS.between(issued, expires);
    }

    public long getRemaining() {
        if (isPermanent()) return -1;
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), expires);
    }

    public void cancel() {
        this.cancelled = true;
        DatabaseUtils.executeUpdate("UPDATE `punishments` SET `cancelled` = 1 WHERE `uuid` = '" + player.getUniqueId() + "' AND `issued` = '" + issued + "'");
    }

    /**
     * Create a punishment in the database
     * WARNING THIS METHOD SHOULD BE RUNNED ASYNC
     * @param resultSet The result set from the database
     */
    public static Punishment fromResultSet(ResultSet resultSet) throws SQLException {

        return new Punishment(
                UserManager.getOfflineUserSync(UUID.fromString(resultSet.getString("uuid"))),
                resultSet.getString("ip"),
                UserManager.getOfflineUserSync(UUID.fromString(resultSet.getString("moderator"))),
                PunishmentType.valueOf(resultSet.getString("type")),
                PunishmentReason.fromReasonString(resultSet.getString("reason")),
                ChatUtils.parseDate(resultSet.getString("issued")),
                ChatUtils.parseDate(resultSet.getString("expires")),
                resultSet.getBoolean("cancelled")
        );
    }

}
