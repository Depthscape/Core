/*
 * CorePlugin
 * Core
 *
 * Created by leobaehre on 9/6/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core;

import lombok.Getter;
import net.depthscape.core.command.BaseCommand;
import net.depthscape.core.command.InventoryTestCommand;
import net.depthscape.core.command.ReloadCommand;
import net.depthscape.core.command.TagCommand;
import net.depthscape.core.config.DatabaseConfig;
import net.depthscape.core.config.MainConfig;
import net.depthscape.core.listener.JoinListener;
import net.depthscape.core.listener.ChatListener;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.DatabaseUtils;
import net.depthscape.core.utils.menu.MenuListener;
import net.depthscape.core.utils.nametag.NametagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    @Getter
    private MainConfig mainConfig;
    @Getter
    private DatabaseConfig databaseConfig;

    @Getter
    private static NametagManager nametagManager;

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


        nametagManager = new NametagManager();


        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        registerCommand(new ReloadCommand());
        registerCommand(new InventoryTestCommand());
        registerCommand(new TagCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseUtils.disconnect();
    }

    private void registerCommand(BaseCommand command) {
        command.register(this);
    }
}
