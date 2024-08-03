/*
 * CorePlugin
 * Core
 *
 * Created by leobaehre on 9/6/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core;

import lombok.Getter;
import lombok.Setter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import net.depthscape.core.command.*;
import net.depthscape.core.command.commands.*;
import net.depthscape.core.command.commands.gamemode.GameModeCommand;
import net.depthscape.core.command.commands.gamemode.VanillaGameModeCommand;
import net.depthscape.core.config.DatabaseConfig;
import net.depthscape.core.config.MainConfig;
import net.depthscape.core.config.model.Websocket;
import net.depthscape.core.listener.*;
import net.depthscape.core.menu.MenuListener;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.socket.WSClient;
import net.depthscape.core.socket.WSServer;
import net.depthscape.core.tasks.CheckRestartTask;
import net.depthscape.core.tasks.NoClipCheckTask;
import net.depthscape.core.tasks.UpdateTimeBoxTask;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public final class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    @Getter
    private MainConfig mainConfig;
    @Getter
    private DatabaseConfig databaseConfig;

    @Getter
    private CheckRestartTask checkRestartTask;
    @Getter
    private UpdateTimeBoxTask updateTimeBoxTask;
    @Getter
    private NoClipCheckTask noClipCheckTask;

    @Getter @Setter
    private static boolean isJoiningAllowed;

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
        System.out.println("Connected to database: " + DatabaseUtils.isConnected());

        RankManager.loadRanks();

        this.mainConfig = new MainConfig(this);
        this.mainConfig.load();

        // websocket
        isServer = this.mainConfig.getWebsocket().isServer();
        setWebsocket();
//        if (isServer) {
        if (true) {
            checkRestartTask = new CheckRestartTask();
            checkRestartTask.runTaskTimer(this, 0, 20L);
        }


        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        //getServer().getPluginManager().registerEvents(new MoveListener(), this);
        //getServer().getPluginManager().registerEvents(new PingListener(), this);

        registerTabEvents();
        registerCommand(new ReloadCommand());
        registerCommand(new StaffChatCommand());
        registerCommand(new VanishCommand());
        registerCommand(new TestCommand());
        registerCommand(new NoClipCommand());
        registerCommand(new DailyRestartCommand());
        registerCommand(new ProfileCommand());
        registerCommand(new PunishCommand());

        registerCommand(new GameModeCommand("gmc", GameMode.CREATIVE));
        registerCommand(new GameModeCommand("gms", GameMode.SURVIVAL));
        registerCommand(new GameModeCommand("gma", GameMode.ADVENTURE));
        registerCommand(new GameModeCommand("gmsp", GameMode.SPECTATOR));
        registerCommand(new VanillaGameModeCommand());

        getCommand("websocketrestart").setExecutor(new WebSocketRestartCommand());

        updateTimeBoxTask = new UpdateTimeBoxTask();
        updateTimeBoxTask.runTaskTimer(this, 0, 20L);

        noClipCheckTask = new NoClipCheckTask();
        noClipCheckTask.runTaskTimer(this, 0, 1L);

        isJoiningAllowed = true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (User user : UserManager.getOnlineUsers()) {
            user.save(false);
            Bukkit.getLogger().info("User " + user.getName() + " saved");
        }
        if (checkRestartTask != null) checkRestartTask.cancel();
        updateTimeBoxTask.cancel();
        noClipCheckTask.cancel();
        DatabaseUtils.disconnect();
    }

    public void setWebsocket() {
        Websocket websocket = this.mainConfig.getWebsocket();
        if (isServer) {
            this.webSocketServer = new WSServer(new InetSocketAddress(websocket.getHost(), websocket.getPort()));
            this.webSocketServer.start();
            Bukkit.getLogger().info("Websocket server started on address " + websocket.getHost() + ":" + websocket.getPort());
        } else {
            this.webSocketClient = new WSClient(URI.create("ws://" + websocket.getHost() + ":" + websocket.getPort()));
            try {
                boolean success = webSocketClient.connectBlocking(10, TimeUnit.SECONDS);
                if (!success) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to connect to websocket server");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
