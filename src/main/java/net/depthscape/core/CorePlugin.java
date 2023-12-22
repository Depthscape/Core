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
import net.depthscape.core.config.DatabaseConfig;
import net.depthscape.core.config.MainConfig;
import net.depthscape.core.listener.ChatListener;
import net.depthscape.core.listener.JoinListener;
import net.depthscape.core.listener.QuitListener;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.tasks.UpdateTimeBoxTask;
import net.depthscape.core.user.User;
import net.depthscape.core.user.UserManager;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    @Getter
    private MainConfig mainConfig;
    @Getter
    private DatabaseConfig databaseConfig;

    @Getter
    private UpdateTimeBoxTask updateTimeBoxTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        //DisguiseManager.setPlugin(this);

        this.databaseConfig = new DatabaseConfig(this);
        this.databaseConfig.load();
        DatabaseUtils.connect(false);

        RankManager.loadRanks();

        this.mainConfig = new MainConfig(this);
        this.mainConfig.load();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        registerTabEvents();
        registerCommand(new ReloadCommand());
        registerCommand(new StaffChatCommand());
        registerCommand(new VanishCommand());
        registerCommand(new DikkeLulCommand());


        updateTimeBoxTask = new UpdateTimeBoxTask();
        updateTimeBoxTask.runTaskTimer(this, 0, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (User user : UserManager.getOnlineUsers()) {
            user.save();
            Bukkit.getLogger().info("User " + user.getName() + " saved");
        }
        updateTimeBoxTask.cancel();
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
