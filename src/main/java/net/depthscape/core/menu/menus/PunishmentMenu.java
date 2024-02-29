package net.depthscape.core.menu.menus;

import net.depthscape.core.menu.Menu;
import net.depthscape.core.menu.MenuItem;
import net.depthscape.core.punishment.PunishmentManager;
import net.depthscape.core.punishment.PunishmentReason;
import net.depthscape.core.punishment.PunishmentType;
import net.depthscape.core.user.OfflineUser;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.UnicodeSpace;
import net.depthscape.core.utils.UnicodeTranslator;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PunishmentMenu extends Menu {

    private final static int MENU_WIDTH = 176;
    private final static int TEXT_BOX_WIDTH = 129;
    private final static int TEXT_BOX_OFFSET_RIGHT = 23;

    OfflineUser target;
    int page;

    public PunishmentMenu(OfflineUser target, int page) {
        super("", 4);

        this.target = target;
        this.page = page;

        String menuCode = "";
        switch (page) {
            case 1 -> menuCode = "\u5C24";
            case 2 -> menuCode = "\u5339";
            case 3 -> menuCode = "";
        }
        setTitle(ChatUtils.format(UnicodeSpace.find(-9) + "&f" + menuCode + getTitleName(target.getName())));
    }

    @Override
    public MenuItem getItemAt(int slot) {
        switch (slot) {
            case 0, 1, 2 -> {
                return getCategoryItem(1, "Chat");
            } // cat 1
            case 3, 4, 5 -> {
                return getCategoryItem(2, "Behavior");
            } // cat 2
            case 6, 7, 8 -> {
                return getCategoryItem(3, "Mods");
            } // cat 3

            case 9, 10, 11, 12 -> {
                return getButtonItem(1, page);
            } // button 1
            case 18, 19, 20, 21 -> {
                return getButtonItem(2, page);
            } // button 3
            case 14, 15, 16, 17 -> {
                return getButtonItem(4, page);
            } // button 2
            case 27, 28, 29, 30 -> {
                return getButtonItem(3, page);
            } // button 5
            case 23, 24, 25, 26 -> {
                return getButtonItem(5, page);
            } // button 4
            case 32, 33, 34, 35 -> {
                return getButtonItem(6, page);
            } // button 6
        }
        return null;
    }

    /***
     * Gets the title of the menu
     * @param name the name of the player
     * @return the title of the menu
     */
    private String getTitleName(String name) {

        StringBuilder builder = new StringBuilder();

        String cutName = name.length() > 14 ? name.substring(0, 14) : name;
        String suffix = "punish " + cutName;
        String translatedSuffix = UnicodeTranslator.translateToUnicode(suffix);

        int wordLength = suffix.length() * 6;

        int backSpaces = -(TEXT_BOX_WIDTH + TEXT_BOX_OFFSET_RIGHT + wordLength); // get the amount of spaces to remove to go to the left of the text box
        int spaces = wordLength + (TEXT_BOX_WIDTH - wordLength) / 2; // get the amount of spaces forward to center the word in the text box

        int targetSpaces = backSpaces + spaces; // go backspaces back and go spaces forward

        builder.append(UnicodeSpace.find(targetSpaces)).append("&#2B2B2B").append(translatedSuffix);

        return builder.toString();
    }

    private PunishmentReason getButtonReason(int button, int page) {
        switch (page) {
            case 1 -> {
                switch (button) {
                    case 1 -> {
                        return PunishmentReason.ADVERTISING;
                    }
                    case 2 -> {
                        return PunishmentReason.SPAMMING;
                    }
                    case 3 -> {
                        return PunishmentReason.THREATS;
                    }
                    case 4 -> {
                        return PunishmentReason.TOXICITY;
                    }
                    case 5 -> {
                        return PunishmentReason.DISRESPECT;
                    }
                    case 6 -> {
                        return PunishmentReason.DISCRIMINATION;
                    }
                }
            }
            case 2 -> {
                switch (button) {
                    case 1 -> {
                        return PunishmentReason.ABUSE;
                    }
                    case 2 -> {
                        return PunishmentReason.GRIEFING;
                    }
                    case 3 -> {
                        return PunishmentReason.BAN_EVASION;
                    }
                    case 4 -> {
                        return PunishmentReason.DUPING;
                    }
                    case 5 -> {
                        return PunishmentReason.MISC_BEHAVIOR;
                    }
                }
            }
            case 3 -> {
                switch (button) {
                    case 1 -> {
                        return PunishmentReason.XRAY;
                    }
                    case 2 -> {
                        return PunishmentReason.COMBAT_HACKS;
                    }
                    case 3 -> {
                        return PunishmentReason.MOVEMENT_HACKS;
                    }
                    case 4 -> {
                        return PunishmentReason.ANTI_AFK;
                    }
                    case 5 -> {
                        return PunishmentReason.MISC_HACKS;
                    }
                }
            }
        }
        return null;
    }

    private MenuItem getButtonItem(int button, int page) {
        PunishmentReason reason = getButtonReason(button, page);
        if (reason == null) return null;
        return new MenuItem() {
            @Override
            public ItemStack getItemStack() {

                String firstTime = reason.getFirstTime() <= 0 ? "Permanent" : ChatUtils.formatSeconds(reason.getFirstTime());
                String secondTime = reason.getSecondTime() <= 0 ? "Permanent" : ChatUtils.formatSeconds(reason.getSecondTime());
                String firstType = reason.getFirstType().name().toLowerCase().replace("_", " ");
                String secondType = reason.getSecondType().name().toLowerCase().replace("_", " ");

                List<String> lore = List.of(
                        "1st: " + firstTime + " - " + firstType,
                        "2nd: " + secondTime + " - " + secondType,
                        "3th: Permanent - ip ban"
                );
                return getEmptyItem(ChatUtils.format(reason.getReason()), lore);
            }

            @Override
            public void onClick(User user, Menu menu, InventoryClickEvent event) {
                if (event.getClick() != ClickType.LEFT) return;
                new ConfirmationMenu(reason.getReason(), reason.getReason().length() * 6, confirmation -> {
                    if (!confirmation) {
                        new PunishmentMenu(target, page).open(user);
                    } else {
                        PunishmentManager.punishPlayer(target, user, reason, reason1 ->
                                user.sendMessage(ChatUtils.getInfoMessage("You have punished " + target.getName() + " for " + reason.getReason()))
                        );
                        user.getPlayer().closeInventory();
                    }
                }).open(user);
            }
        };
    }

    public MenuItem getCategoryItem(int category, String name) {
        return new MenuItem() {
            @Override
            public ItemStack getItemStack() {
                return getEmptyItem(ChatUtils.format(name), List.of());
            }

            @Override
            public void onClick(User user, Menu menu, InventoryClickEvent event) {
                if (event.getClick() != ClickType.LEFT) return;
                if (page == category) return;
                page = category;
                new PunishmentMenu(target, page).open(user);

            }
        };
    }
}
