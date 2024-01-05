package net.depthscape.core.command;

import net.depthscape.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WebSocketRestartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CorePlugin.getInstance().setWebsocket();
        sender.sendMessage("Websocket restarted!");
        return true;
    }
}
