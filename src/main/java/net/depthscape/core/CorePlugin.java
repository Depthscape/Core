/*
 * CorePlugin
 * Core
 *
 * Created by leobaehre on 9/6/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core;

import lombok.Getter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import net.depthscape.core.command.*;
import net.depthscape.core.command.commands.*;
import net.depthscape.core.config.DatabaseConfig;
import net.depthscape.core.config.MainConfig;
import net.depthscape.core.listener.ChatListener;
import net.depthscape.core.listener.JoinListener;
import net.depthscape.core.listener.QuitListener;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.socket.WSClient;
import net.depthscape.core.socket.WSServer;
import net.depthscape.core.tasks.NoClipCheckTask;
import net.depthscape.core.tasks.UpdateTimeBoxTask;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;


public final class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    @Getter
    private MainConfig mainConfig;
    @Getter
    private DatabaseConfig databaseConfig;

    @Getter
    private UpdateTimeBoxTask updateTimeBoxTask;
    @Getter
    private NoClipCheckTask noClipCheckTask;

    @Getter
    private static boolean isServer;
    @Getter @Nullable
    private WSServer webSocketServer;
    @Getter @Nullable
    private WSClient webSocketClient;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.databaseConfig = new DatabaseConfig(this);
        this.databaseConfig.load();
        DatabaseUtils.connect(false);

        RankManager.loadRanks();

        this.mainConfig = new MainConfig(this);
        this.mainConfig.load();

        // websocket
        isServer = this.mainConfig.getWebsocket().isServer();

        if (isServer) {
            this.webSocketServer = new WSServer(new InetSocketAddress(this.mainConfig.getWebsocket().getHost(), this.mainConfig.getWebsocket().getPort()));
            this.webSocketServer.start();
            Bukkit.getLogger().info("Websocket server started on address " + this.mainConfig.getWebsocket().getHost() + ":" + this.mainConfig.getWebsocket().getPort());
        } else {
            this.webSocketClient = new WSClient(URI.create("ws://" + this.mainConfig.getWebsocket().getHost() + ":" + this.mainConfig.getWebsocket().getPort()));
            try {
                boolean success = webSocketClient.connectBlocking(10, TimeUnit.SECONDS);
                System.out.println(success);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bukkit.getLogger().info("Websocket client connected to address " + this.mainConfig.getWebsocket().getHost() + this.mainConfig.getWebsocket().getPort());
        }


        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        registerTabEvents();
        registerCommand(new ReloadCommand());
        registerCommand(new StaffChatCommand());
        registerCommand(new VanishCommand());
        registerCommand(new TestCommand());
        registerCommand(new NoClipCommand());


        updateTimeBoxTask = new UpdateTimeBoxTask();
        updateTimeBoxTask.runTaskTimer(this, 0, 20L);

        noClipCheckTask = new NoClipCheckTask();
        noClipCheckTask.runTaskTimer(this, 0, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (User user : UserManager.getOnlineUsers()) {
            user.save();
            Bukkit.getLogger().info("User " + user.getName() + " saved");
        }
        updateTimeBoxTask.cancel();
        noClipCheckTask.cancel();
        DatabaseUtils.disconnect();
    }

    private void registerCommand(BaseCommand command) {
        command.register(this);
    }

    private void registerTabEvents() {
        TabAPI.getInstance().getEventBus().register(PlayerLoadEvent.class, (event) -> {
            TabPlayer tabPlayer = event.getPlayer();
            User user = UserManager.getUser(tabPlayer.getUniqueId());

            user.setNametag();
        });
    }
}
