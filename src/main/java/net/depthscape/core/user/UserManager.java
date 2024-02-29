package net.depthscape.core.user;

import lombok.Getter;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.model.Callback;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    @Getter
    private static final List<OfflineUser> offlineUsers = new ArrayList<>();

    @Getter
    private static final List<User> onlineUsers = new ArrayList<>();

    public static User setOnline(OfflineUser offlineUser, Player player) {
        if (onlineUsers.contains(getUser(player.getUniqueId()))) {
            return getUser(player.getUniqueId());
        }

        User user = new User(offlineUser);

        user.setPlayer(player);
        user.setName(player.getName());
        user.setLastIp(player.getAddress().getAddress().getHostAddress());
        saveUser(user, true);

        System.out.println("TEST - User " + player.getName() + " updated");
        user.setInfoPanel();


        onlineUsers.add(user);

        return user;
    }


    public static void getOnlineUser(Player player, Callback<User> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call(getOnlineUserSync(player));
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static User getOnlineUserSync(Player player) {
        if (onlineUsers.contains(getUser(player.getUniqueId()))) {
            return getUser(player.getUniqueId());
        }

        OfflineUser offlineUser = getOfflineUserSync(player.getUniqueId());
        if (offlineUser == null) {
            return null;
        }

        User user = new User(offlineUser);
        onlineUsers.add(user);
        return user;
    }

    public static User getUser(UUID uniqueId) {
        return onlineUsers.stream().filter(user -> user.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public static User getUser(String name) {
        return onlineUsers.stream().filter(user -> user.getName().equals(name)).findFirst().orElse(null);
    }

    public static void getOfflineUser(UUID uniqueId, Callback<OfflineUser> callback) {

        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call(getOfflineUserSync(uniqueId));
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static OfflineUser getOfflineUserSync(UUID uniqueId) {

        if (onlineUsers.contains(getUser(uniqueId))) {
            return getUser(uniqueId);
        }

        OfflineUser offlineUser = offlineUsers.stream().filter(user -> user.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
        if (offlineUser != null) {
            return offlineUser;
        }

        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid = '" + uniqueId.toString() + "'");

        if (resultSet == null) {
            System.out.println("TEST ResultSet is null");
            return null;
        }


        try {
            if (resultSet.next()) {
                OfflineUser user = new OfflineUser(resultSet);
                offlineUsers.add(user);
                return user;
            } else {
                System.out.println("TEST ResultSet is empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getOfflineUser(String name, Callback<OfflineUser> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call(getOfflineUserSync(name));
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static OfflineUser getOfflineUserSync(String name) {
        if (onlineUsers.contains(getUser(name))) {
            return getUser(name);
        }

        OfflineUser offlineUser = offlineUsers.stream().filter(user -> user.getName().equals(name)).findFirst().orElse(null);
        if (offlineUser != null) {
            return offlineUser;
        }

        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM players WHERE name = '" + name + "'");

        try {
            if (resultSet.next()) {
                OfflineUser user = new OfflineUser(resultSet);
                offlineUsers.add(user);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getOfflineUser(long discordId, Callback<OfflineUser> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call(getOfflineUserSync(discordId));
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static OfflineUser getOfflineUserSync(long discordId) {
        User user = onlineUsers.stream().filter(u -> u.getDiscordId() == discordId).findFirst().orElse(null);
        if (user != null) {
            return user;
        }

        OfflineUser offlineUser = offlineUsers.stream().filter(u -> u.getDiscordId() == discordId).findFirst().orElse(null);
        if (offlineUser != null) {
            return offlineUser;
        }

        ResultSet resultSet = DatabaseUtils.executeQuery("SELECT * FROM players WHERE discord_id = '" + discordId + "'");

        try {
            if (resultSet.next()) {
                OfflineUser u = new OfflineUser(resultSet);
                offlineUsers.add(u);
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createNewUser(UUID uniqueId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                createNewUserSync(uniqueId);
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static void createNewUserSync(UUID uniqueId) {
        DatabaseUtils.executeUpdate(
                "INSERT INTO players"
                        + " (uuid, name, rank, coins, vanished) VALUES"
                        + " ('" + uniqueId + "', NULL, '" + RankManager.getDefaultRank().getName() + "', 0, 0)");
    }

    public static void saveUser(User user, boolean async) {
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                saveUserSync(user);
            }
        };

        if (async) {
            br.runTaskAsynchronously(CorePlugin.getInstance());
        } else {
            br.run();
        }
    }

    public static void saveUserSync(User user) {
        DatabaseUtils.executeUpdate(
                "UPDATE players SET"
                        + " name = '" + user.getName() + "',"
                        + " vanished = " + (user.isVanished() ? 1 : 0) + ","
                        + " discord_id = '" + user.getDiscordId() + "',"
                        + " last_ip = '" + user.getLastIp() + "'"
                        + " WHERE uuid = '" + user.getUniqueId() + "'");
    }

    public static void update(OfflineUser offlineUser) {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateSync(offlineUser);
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

    public static void updateSync(OfflineUser offlineUser) {
        DatabaseUtils.executeQueryAsync("SELECT * FROM players WHERE uuid = '" + offlineUser.getUniqueId() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    offlineUser.setName(resultSet.getString("name"));
                    offlineUser.setRank(RankManager.getRank(resultSet.getString("rank")));
                    offlineUser.setCoins(resultSet.getInt("coins"));
                    offlineUser.setVanished(resultSet.getBoolean("vanished"));
                    offlineUser.setDiscordId(resultSet.getLong("discord_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void removeUser(User user) {
        onlineUsers.remove(user);
    }

    public static void removeOfflineUser(OfflineUser user) {
        offlineUsers.remove(user);
    }

    public static List<User> getOnlineUsers() {
        return onlineUsers.stream().filter(user -> user.getPlayer() != null && user.getPlayer().isOnline()).toList();
    }
}
