/*
 * ChatUtils
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import lombok.experimental.UtilityClass;
import net.depthscape.core.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ChatUtils {

    public static String format(String message) {
        Pattern unicode = Pattern.compile("\\\\u\\+[a-fA-F0-9]{4}");
        Matcher match = unicode.matcher(message);
        while (match.find()) {
            String code = message.substring(match.start(), match.end());
            message = message.replace(code, Character.toString((char) Integer.parseInt(code.replace("\\u+",""),16)));
            match = unicode.matcher(message);
        }
        return ColorUtils.colorize(message);
    }

    public ChatColor getLastColor(BaseComponent[] bc) {
        BaseComponent last = bc[bc.length - 1];
        List<BaseComponent> e = last.getExtra();
        if(!e.isEmpty()) {
            return getLastColor(e);
        }
        return last.getColor();
    }

    public ChatColor getLastColor(List<BaseComponent> bc) {
        BaseComponent last = bc.get(bc.size() - 1);
        List<BaseComponent> e = last.getExtra();
        if(!e.isEmpty()) {
            return getLastColor(e);
        }
        return last.getColor();
    }

    public static String getInfoMessage(String message) {
        return CustomFontCharacter.INFO_ICON + " &#3A86FF" + message;
    }

    public static String getWarningMessage(String message) {
        return CustomFontCharacter.WARNING_ICON + " &#FB2407" + message;
    }

    public static String getChatFormat() {
        return "%prefix% %player% &f: %message%";
    }

    public static String getStaffChatFormat() {
        return "&#3A86FF[SC] %prefix% %player% &#A5A5A5: %message%";
    }

    public static String getDiscordChatFormat() {
        return "&#5661EA[D] &f%prefix% %player% &f: %message%";
    }

    public static void sendDiscordMessage(JSONObject data) {
        UserManager.getOnlineUsers().forEach(user -> {
            user.sendMessage(getDiscordChatFormat()
                    .replace("%prefix%", data.getString("prefix"))
                    .replace("%player%", data.getString("player"))
                    .replace("%message%", data.getString("message"))
            );
        });
    }
}

