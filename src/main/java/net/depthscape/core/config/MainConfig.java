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
import net.depthscape.core.config.model.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MainConfig {

    private final String path = "config.yml";

    private final YamlConfiguration config;

    private String serverName;
    private Whitelist whitelist;
    private Tablist tablist;
    private Websocket websocket;
    private MOTD motd;
    private DailyRestart dailyRestart;

    private String chatFormat;
    private String staffChatFormat;
    private String discordChatFormat;


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

        this.motd = new MOTD(
                config.getString("MOTD.Line_One"),
                config.getString("MOTD.Line_Two")
        );

        boolean isEnabled = config.getBoolean("Daily_Restart.Enabled");
        boolean popups = config.getBoolean("Daily_Restart.Popups");

        String time = config.getString("Daily_Restart.Time");
        if (time == null) time = "00:00:00";
        LocalTime localTime = LocalTime.parse(time);

        Map<Integer, String> messages = new HashMap<>();

        for (String key : config.getConfigurationSection("Daily_Restart.Messages").getKeys(false)) {
            int timeInt;
            try {
                timeInt = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                Bukkit.getLogger().warning("Invalid time for daily restart message: " + key);
                continue;
            }
            messages.put(timeInt, config.getString("Daily_Restart.Messages." + key));
        }


        this.dailyRestart = new DailyRestart(
                isEnabled,
                popups,
                localTime,
                messages
        );

        this.chatFormat = config.getString("Messages.Formats.Chat");
        this.staffChatFormat = config.getString("Messages.Formats.Staff_Chat");
        this.discordChatFormat = config.getString("Messages.Formats.Discord_Chat");
    }
}
