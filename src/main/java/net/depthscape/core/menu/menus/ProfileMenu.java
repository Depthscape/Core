package net.depthscape.core.menu.menus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.depthscape.core.menu.Menu;
import net.depthscape.core.menu.MenuItem;
import net.depthscape.core.rank.Rank;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.UnicodeSpace;
import net.depthscape.core.utils.UnicodeTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/***
 * Menu for the profile of a user
 * This menu displays stats for players to see each other's profiles
 * It also allows staff to punish players and change settings
 */

public class ProfileMenu extends Menu {

    private final static int MENU_WIDTH = 176;
    private final static int TEXT_BOX_WIDTH = 129;
    private final static int TEXT_BOX_OFFSET_RIGHT = 23;


    OfflineUser target;
    boolean isStaff;
    boolean isSelf;

    /***
     * Constructor for the profile menu
     * @param target the user to display the profile of
     * @param viewer the user that is viewing the profile
     */
    public ProfileMenu(OfflineUser target, User viewer) {
        super("", 4);

        this.target = target;
        this.isStaff = viewer.getRank().isStaff();
        this.isSelf = target.equals(viewer);

        String menuCode = isStaff ? "\u51E1" : "\u5385";
        setTitle(ChatUtils.format(UnicodeSpace.find(-9) + "&f" + menuCode + getTitleName(target.getName(), target.getRank())));

    }

    @Override
    public MenuItem getItemAt(int slot) {
        switch (slot) {
            case 21 -> {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(target.getUniqueId()));
                meta.setCustomModelData(1);
                head.setItemMeta(meta);
                return MenuItem.of(head);
            }
            case 7,8, 16, 17, 25, 26 -> {
                return new MenuItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getEmptyItem("&fHome", List.of());
                    }
                };
            }
            case 34 -> {
                if (!isStaff && isSelf) return null;
                return new MenuItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getEmptyItem("&fPunish", List.of());
                    }

                    @Override
                    public void onClick(User user, Menu menu, InventoryClickEvent event) {
                        if (event.getClick() != ClickType.LEFT) return;

                        new PunishmentMenu(target, 1).open(user);
                    }
                };
            }
            case 35 -> {
                if (!isStaff) return null;
                return new MenuItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getEmptyItem("&fSettings", List.of());
                    }
                };
            }

        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    private enum OnlineState {
        ONLINE("\u53CB"),
        OFFLINE("\u6B79");

        private final String unicode;
    }


    /***
     * Gets the title of the menu
     * @param name the name of the player
     * @return the title of the menu
     */
    private String getTitleName(String name, Rank rank) {

        StringBuilder builder = new StringBuilder();

        String profilePrefix = rank.getProfilePrefix();
        if (profilePrefix != null) {
            builder
                    .append(UnicodeSpace.find(-MENU_WIDTH - 1))
                    .append(profilePrefix);
        }

        boolean isOnline = Bukkit.getPlayer(target.getUniqueId()) != null;
        String onlineStatus = target.isVanished() || !isOnline ? OnlineState.OFFLINE.getUnicode() : OnlineState.ONLINE.getUnicode();
        builder
                .append(UnicodeSpace.find(-MENU_WIDTH - 1))
                .append(onlineStatus);

        String cutName = name.length() > 13 ? name.substring(0, 13) : name;
        String suffix = cutName + "'s profile";
        String translatedSuffix = UnicodeTranslator.translateToUnicode(suffix);

        int wordLength = suffix.length() * 6 - 4; // get the length of the word in pixels -4 because of the '

        int backSpaces = -(TEXT_BOX_WIDTH + TEXT_BOX_OFFSET_RIGHT + wordLength); // get the amount of spaces to remove to go to the left of the text box
        int spaces = wordLength + (TEXT_BOX_WIDTH - wordLength) / 2; // get the amount of spaces forward to center the word in the text box

        int targetSpaces = backSpaces + spaces; // go backspaces back and go spaces forward

        builder.append(UnicodeSpace.find(targetSpaces) + "&#2B2B2B" + translatedSuffix);

        return builder.toString();
    }
}
