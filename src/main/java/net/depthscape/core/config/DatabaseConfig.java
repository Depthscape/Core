/*
 * DatabaseConfig
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.config;

import lombok.Getter;
import net.depthscape.core.CorePlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class DatabaseConfig {

    private final String path = "database.yml";

    private final YamlConfiguration config;

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public DatabaseConfig(CorePlugin plugin) {

        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            plugin.saveResource(path, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        this.host = config.getString("Host");
        this.port = config.getInt("Port");
        this.database = config.getString("Database");
        this.username = config.getString("Username");
        this.password = config.getString("Password");
    }

}
