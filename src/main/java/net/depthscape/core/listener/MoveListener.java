package net.depthscape.core.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoveListener implements Listener {

    public static final List<UUID> CACHED_PLAYERS = new ArrayList<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Vector direction = player.getLocation().getDirection();

        List<Player> nearbyPlayers = player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof Player)
                .filter(entity -> !entity.equals(player))
                .map(entity -> (Player) entity)
                .toList();

        for (Player nearbyPlayer : nearbyPlayers) {
            Location location = nearbyPlayer.getLocation();
            Vector toPlayer = location.toVector().subtract(player.getLocation().toVector());
            double dot = direction.dot(toPlayer.normalize());

            if (dot > 0.01 && getDistance(player.getLocation(), location) < 2) {
                if (!CACHED_PLAYERS.contains(player.getUniqueId())) {
                    CACHED_PLAYERS.add(player.getUniqueId());
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§a§lYou are looking at " + nearbyPlayer.getName()));

            } else {
                if (CACHED_PLAYERS.contains(player.getUniqueId())) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                    CACHED_PLAYERS.remove(player.getUniqueId());
                }
            }
            break;
        }

    }

    private double getDistance(Location loc1, Location loc2) {
        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2));
    }
}
