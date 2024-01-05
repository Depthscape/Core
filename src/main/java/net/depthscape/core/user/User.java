/*
 * User
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.user;

import com.google.j2objc.annotations.Property;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.ability.Ability;
import net.depthscape.core.model.Box;
import net.depthscape.core.model.Notification;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
public class User extends OfflineUser {

    BossBar bossBar;
    private Player player;

    private List<Notification> currentNotifications = new ArrayList<>();

    private Map<String, Box> infoPanel = new HashMap<>();
    private BukkitTask infoPanelRunnable;

    private boolean staffChatSendEnabled = false;
    private boolean noClipEnabled = false;
    private boolean wasFlyingBeforeNoClip = false;

    private Map<Ability, Boolean> enabledAbilities = new HashMap<>();

    public User(UUID uniqueId) {
        super(uniqueId);
    }

    public User(OfflineUser user) {
        super(user.getUniqueId(), user.getName(), user.getRank(), user.getCoins(), user.isVanished(), user.getDiscordId());
    }

    public static User getUser(Player player) {
        return UserManager.getUser(player.getUniqueId());
    }

    public void save() {
        UserManager.saveUser(this);
    }

    /*--- Abilities ---*/

    public boolean hasPermissionForAbility(Ability ability) {
        return enabledAbilities.containsKey(ability);
    }

    public boolean isAbilityEnabled(Ability ability) {
        if (enabledAbilities.containsKey(ability)) {
            return enabledAbilities.get(ability);
        }
        return false;
    }

    public void enableAbility(Ability ability) {
        if (enabledAbilities.containsKey(ability)) {
            if (!isAbilityEnabled(ability)) {
                ability.enable(this);
                enabledAbilities.put(ability, true);
            }
        }
    }

    public void disableAbility(Ability ability) {
        if (enabledAbilities.containsKey(ability)) {
            if (isAbilityEnabled(ability)) {
                ability.disable(this);
                enabledAbilities.put(ability, false);
            }
        }
    }





    /*--- Nametag ---*/

    public void setNametag(String prefix) {
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(getUniqueId());
        if (tabPlayer == null) {
            return;
        }
        TabAPI.getInstance().getNameTagManager().setPrefix(tabPlayer, prefix);
        TabAPI.getInstance().getTabListFormatManager().setPrefix(tabPlayer, prefix);

        String teamName = getRank().getName().toLowerCase();
        if (isVanished()) {
            teamName = "vanish_" + teamName;
        }

        tabPlayer.setTemporaryGroup(teamName);
    }

    public void setNametag() {
        String prefix = ChatUtils.format(getRank().getTabPrefix() + " ");
        setNametag(prefix);

    }
    /*--- Bossbar ---*/
    public void setInfoPanel() {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        }
        if (!bossBar.getPlayers().contains(this.player)) bossBar.addPlayer(this.player);

        infoPanelRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.setTitle(ChatUtils.format(getCoolBar(infoPanel.values().stream().toList())));
            }
        }.runTaskTimer(CorePlugin.getInstance(), 0, 2);
    }

    public void addInfoPanelBox(String id, Box box, boolean left) {
        if (!left) {
            infoPanel.put(id, box);
            return;
        }
        Map<String, Box> newMap = new HashMap<>();
        newMap.put(id, box);
        newMap.putAll(infoPanel);
        infoPanel = newMap;
    }

    public void removeInfoPanelBox(String id) {
        infoPanel.remove(id);
    }

    public void getBoxId() {
        infoPanel.clear();
    }

    public void sendNotification(String message) {
        if (!currentNotifications.isEmpty()) {
            currentNotifications.get(0).remove();
            currentNotifications.remove(0);
        }
        currentNotifications.add(new Notification(this, message, n -> currentNotifications.remove(n)));
        player.playSound(player.getLocation(), "custom.ui.popup", 1, 1);
    }


    public String getCoolBar(List<Box> boxes) {
        int padding = 3;

        StringBuilder boxBuilder = new StringBuilder();
        boxBuilder.append(ChatUtils.format("&#FFFEFD"));
        for (int i = 0; i < boxes.size(); i++) {
            if (i != 0) {
                boxBuilder.append("  ");
            }

            String text = boxes.get(i).getText();

            int textWidth = DefaultFontInfo.getStringLength(text);
            List<BossBarCharacter> closestBossbarCenters = BossBarCharacter.getCenters(textWidth + padding * 2 - 1);
            int centerWidth = closestBossbarCenters.stream().mapToInt(BossBarCharacter::getWidth).sum();
            // end + -1 + center + -1 + end + x + text
            //x = textWidth / 2 + centerWidth / 2

            int x = -(textWidth / 2 + centerWidth / 2) - 2;

            // (closestBossbarCenters.size() > 1 ? closestBossbarCenters.size() : 0) / 2)

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(BossBarCharacter.BOSSBAR_END).append(UnicodeSpace.MINUS_1);
            for (BossBarCharacter customFontCharacter : closestBossbarCenters) {
                stringBuilder.append(customFontCharacter.getCharacter()).append(UnicodeSpace.MINUS_1);

            }
            stringBuilder.append(BossBarCharacter.BOSSBAR_END).append(UnicodeSpace.findBestCombination(x)).append(text);

            boxBuilder.append(ChatUtils.format(stringBuilder.toString()));
        }

        return boxBuilder.toString();
    }

    /*--- Vanish ---*/

    public void vanish(boolean silent) {


    }

    public void unvanish(boolean silent) {

        if (!isVanished()) return;
        setVanished(false);

        Rank rank = getRank();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == player) continue;
            User targetUser = User.getUser(target);
            Rank targetRank = targetUser.getRank();

            if (targetRank.getWeight() <= rank.getWeight()) {
                if (!silent) {
                    targetUser.sendMessage(ChatUtils.getInfoMessage(player.getName() + " has unvanished."));
                }
                continue;
            }
            target.showPlayer(CorePlugin.getInstance(), player);
        }

        setNametag();

        removeInfoPanelBox("vanished");
        if (!silent) {
            sendNotification(CustomFontCharacter.VANISH_OFF.toString());
            sendMessage(ChatUtils.getInfoMessage("You have unvanished."));
        }
    }

    public void hideVanished() {
        for (Player target : Bukkit.getOnlinePlayers()) {
            User targetUser = User.getUser(target);
            if (!targetUser.isVanished()) continue;
            Rank targetRank = targetUser.getRank();
            if (targetRank.getWeight() >= getRank().getWeight()) {
                target.hidePlayer(CorePlugin.getInstance(), player);
            }
        }

        if (isVanished()) {
            vanish(true);
        }
    }

    public void toggleVanish() {
        if (isVanished()) {
            unvanish(false);
        } else {
            vanish(false);
        }
    }

    /*--- Staff Chat ---*/

    public void toggleStaffChat() {
        staffChatSendEnabled = !staffChatSendEnabled;
        if (staffChatSendEnabled) {
            sendMessage(ChatUtils.getInfoMessage("You are now in staff chat mode."));
        } else {
            sendMessage(ChatUtils.getInfoMessage("You are no longer in staff chat mode."));
        }
    }

    /*--- NoClip ---*/
    public void toggleNoClip() {
        noClipEnabled = !noClipEnabled;
        if (noClipEnabled) {
            if (player.isFlying()) {
                wasFlyingBeforeNoClip = true;
            }
            sendMessage(ChatUtils.getInfoMessage("You are now in noclip mode."));
        } else {
            if (wasFlyingBeforeNoClip) {
                player.setFlying(true);
                wasFlyingBeforeNoClip = false;
            }
            sendMessage(ChatUtils.getInfoMessage("You are no longer in noclip mode."));
        }
    }

    public void sendMessage(String message) {
        this.player.sendMessage(ChatUtils.format(message));
    }
}
