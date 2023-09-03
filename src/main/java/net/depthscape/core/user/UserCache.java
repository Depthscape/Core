/*
 * UserCache
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.depthscape.core.model.Callback;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class UserCache {
    private static final Cache<UUID, User> userCache = CacheBuilder.newBuilder().build();

    public static void checkOrCreateUser(UUID uuid, Callback<User> callback) {
        User user = userCache.getIfPresent(uuid);

        if (user == null) {
            registerNewUser(uuid, u -> {
                userCache.put(uuid, u);
                callback.call(u);
            });
        } else {
            callback.call(user);
        }
    }

    public static User checkOrCreateUserSync(UUID uuid) {
        User user = userCache.getIfPresent(uuid);

        if (user == null) {
            User u = registerNewUserSync(uuid);
            userCache.put(uuid, u);
            return u;
        } else {
            return user;
        }
    }

    /**
     * Get user from uuid
     * @param uniqueId input uuid
     * @return output user
     */
    public static User getUser(UUID uniqueId) {
        return userCache.getIfPresent(uniqueId);
    }

    /**
     * Get user from player
     * @param player input player
     * @return output user
     */
    public static User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    private static void registerNewUser(UUID uniqueId, Callback<User> callback) {
        getUserFromDatabase(uniqueId, user -> {
            userCache.put(uniqueId, user);
            callback.call(user);
        });
    }

    private static User registerNewUserSync(UUID uniqueId) {
        User user = getUserFromDatabaseSync(uniqueId);
        userCache.put(uniqueId, user);
        return user;
    }

    private static void getUserFromDatabase(UUID uniqueId, Callback<User> userCallback) {
        User user = new User(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    //user.setName(resultSet.getString("name"));
                    user.setRank(RankManager.getRank(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                } else {
                    // create new user in database
                    saveNewUser(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userCallback.call(user);
        });
    }

    public static User getUserFromDatabaseSync(UUID uniqueId) {
        User user = new User(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuerySync("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    //user.setName(resultSet.getString("name"));
                    user.setRank(RankManager.getRank(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                } else {
                    // create new user in database
                    saveNewUser(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return user;
    }

    public static void getOfflineUserFromDatabase(UUID uniqueId, Callback<OfflineUser> userCallback) {
        OfflineUser user = new OfflineUser(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "';", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                } else {
                    userCallback.call(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userCallback.call(user);
        });
    }

    public static void getOfflineUserFromDatabase(String name, Callback<OfflineUser> userCallback) {

        getUserUUID(name, uuid -> {
            if (uuid == null) {
                userCallback.call(null);
                return;
            }

            getOfflineUserFromDatabase(uuid, userCallback);
        });
    }

    public static void getUserUUID(String name, Callback<UUID> uuidCallback) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE name='" + name + "';", resultSet -> {
            try {
                if (resultSet.next()) {
                    // get user uuid
                    UUID uniqueId = UUID.fromString(resultSet.getString("uuid"));
                    uuidCallback.call(uniqueId);
                } else {
                    uuidCallback.call(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void saveNewUser(User user) {
        DatabaseUtils.executeUpdate("INSERT INTO players (id, uuid, name, rank, coins, vanished) VALUES (default, '" + user.getUniqueId() + "', '"+ user.getName() + "', '" + user.getRank().getName() + "', 0, 0)");
    }

    public static void refreshUser(User user, Callback<User> callback) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setRank(RankManager.getRank(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                    updatePlayerInfo(user);
                    callback.call(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static User refreshUserSync(User user) {
        DatabaseUtils.executeQuerySync("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setId(resultSet.getInt("id"));
                    user.setRank(RankManager.getRank(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                    updatePlayerInfo(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return user;
    }

    public static void updatePlayerInfo(User user) {
        DatabaseUtils.executeUpdate("UPDATE players SET name='" + user.getName() + "' WHERE uuid='" + user.getUniqueId().toString() + "'");
    }

    public static void updatePlayerInfoSync(User user) {
        DatabaseUtils.executeUpdateSync("UPDATE players SET name='" + user.getName() + "' WHERE uuid='" + user.getUniqueId().toString() + "'");
    }
}
