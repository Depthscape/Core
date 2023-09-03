/*
 * DatabaseUtils
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import lombok.Getter;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.model.Callback;
import net.depthscape.core.model.SQLCallback;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    @Getter
    private static Connection connection;

    public static void connect(boolean async) {
        if (isConnected()) {
            Bukkit.getLogger().info("Already connected to the database!");
            return;
        }

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), DatabaseUtils::setConnection);
        } else {
            setConnection();
        }

    }

    public static void connect(String host, int port, String database, String username, String password) {
        if (isConnected()) {
            Bukkit.getLogger().info("Already connected to the database!");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void disconnect() {
        if (!isConnected()) {
            Bukkit.getLogger().info("Already disconnected from the database!");
            return;
        }

        try {
            connection.close();
            Bukkit.getLogger().info("Successfully disconnected from the database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeQuery(String query, SQLCallback callback) {
        if (!isConnected()) {
            Bukkit.getLogger().info("You are not connected to the database!");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), () -> {
            try {
                callback.call(connection.createStatement().executeQuery(query));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void executeQuerySync(String query, SQLCallback callback) {
        if (!isConnected()) {
            Bukkit.getLogger().info("You are not connected to the database!");
            return;
        }

        try {
            callback.call(connection.createStatement().executeQuery(query));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void executeUpdateSync(String query) {
        if (!isConnected()) {
            Bukkit.getLogger().info("You are not connected to the database!");
            return;
        }

        try {
            connection.createStatement().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String query) {
        if (!isConnected()) {
            Bukkit.getLogger().info("You are not connected to the database!");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), () -> {
            try {
                connection.createStatement().executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void setConnection() {
        try {

            String host = CorePlugin.getInstance().getDatabaseConfig().getHost();
            int port = CorePlugin.getInstance().getDatabaseConfig().getPort();
            String database = CorePlugin.getInstance().getDatabaseConfig().getDatabase();
            String username = CorePlugin.getInstance().getDatabaseConfig().getUsername();
            String password = CorePlugin.getInstance().getDatabaseConfig().getPassword();

            Bukkit.getLogger().info("Host: " + host);
            Bukkit.getLogger().info("Port: " + port);
            Bukkit.getLogger().info("Database: " + database);
            Bukkit.getLogger().info("Username: " + username);
            Bukkit.getLogger().info("Password: " + password);

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);

            Bukkit.getLogger().info("&aSuccessfully connected to the database!");
        } catch (Exception e) {
            Bukkit.getLogger().info("Failed to connect to the database!");
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        if (connection != null) {
            try {
                return !connection.isClosed();
            } catch (final Exception e) {
                Bukkit.getLogger().info("Failed to check if connection is closed!");
                e.printStackTrace();
            }
        }
        return false;
    }
}
