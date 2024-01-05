package net.depthscape.core.ability.abilities;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.ability.Ability;
import net.depthscape.core.model.Box;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.CustomFontCharacter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class VanishAbility extends Ability {

    public VanishAbility() {
        super("Vanish");
    }

    @Override
    public void enable(User user) {
        if (user.isVanished()) return;
        user.setVanished(true);

        Rank rank = user.getRank();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == user.getPlayer()) continue;
            User targetUser = User.getUser(target);
            Rank targetRank = targetUser.getRank();
            if (targetRank.getWeight() <= rank.getWeight()) {
                targetUser.sendMessage(ChatUtils.getInfoMessage(user.getPlayer().getName() + " has vanished."));
                continue;
            }

            target.hidePlayer(CorePlugin.getInstance(), user.getPlayer());
        }

        String prefix = ChatUtils.format(user.getRank().getVanishPrefix() + " ");
        user.setNametag(prefix);


        user.addInfoPanelBox("vanished", new Box(CustomFontCharacter.VANISHED + " Vanished"), true);
        user.sendNotification(CustomFontCharacter.VANISH_ON.toString());
        user.sendMessage(ChatUtils.getInfoMessage("You have vanished."));
    }

    @Override
    public void disable(User user) {
        if (!user.isVanished()) return;
        user.setVanished(false);

        Rank rank = user.getRank();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == user.getPlayer()) continue;
            User targetUser = User.getUser(target);
            Rank targetRank = targetUser.getRank();
            if (targetRank.getWeight() <= rank.getWeight()) {
                targetUser.sendMessage(ChatUtils.getInfoMessage(user.getPlayer().getName() + " has unvanished."));
                continue;
            }
            target.showPlayer(CorePlugin.getInstance(), user.getPlayer());
        }

        user.setNametag();

        user.removeInfoPanelBox("vanished");
        user.sendNotification(CustomFontCharacter.VANISH_OFF.toString());
        user.sendMessage(ChatUtils.getInfoMessage("You have unvanished."));
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        User user = User.getUser(event.getPlayer());
        if (user.isVanished()) {
            user.setVanished(true);

            Rank rank = user.getRank();

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target == user.getPlayer()) continue;
                User targetUser = User.getUser(target);
                Rank targetRank = targetUser.getRank();
                if (targetRank.getWeight() <= rank.getWeight()) {
                    continue;
                }

                target.hidePlayer(CorePlugin.getInstance(), user.getPlayer());
            }

            String prefix = ChatUtils.format(user.getRank().getVanishPrefix() + " ");
            user.setNametag(prefix);
            user.addInfoPanelBox("vanished", new Box(CustomFontCharacter.VANISHED + " Vanished"), true);
        }
    }
}
