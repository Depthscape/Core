/*
 * MainConfig
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.config;

import lombok.Getter;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.config.model.Tablist;
import net.depthscape.core.config.model.Websocket;
import net.depthscape.core.config.model.Whitelist;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class MainConfig {

    private final String path = "config.yml";

    private final YamlConfiguration config;

    private String serverName;
    private Whitelist whitelist;
    private Tablist tablist;
    private Websocket websocket;


    public MainConfig(CorePlugin plugin) {
        File file = new File(plugin.getDataFolder(), path);

        if (!file.exists()) {
            plugin.saveResource(path, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        this.serverName = config.getString("Server_Name");

        this.whitelist = new Whitelist(
                config.getBoolean("Whitelist.Enabled"),
                config.getString("Whitelist.Block_Message"),
                config.getStringList("Whitelist.Whitelisted_Ranks")
        );

        this.tablist = new Tablist(
                config.getString("Tablist.Header"),
                config.getString("Tablist.Footer")
        );

        this.websocket = new Websocket(
                config.getBoolean("Web_Socket.Server"),
                config.getString("Web_Socket.Host"),
                config.getInt("Web_Socket.Port")
        );
    }
}
