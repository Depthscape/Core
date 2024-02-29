/*
 * ChatUtils
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import io.netty.buffer.ByteBufHolder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.depthscape.core.CorePlugin;
import net.depthscape.core.punishment.Punishment;
import net.depthscape.core.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.json.JSONObject;

import javax.crypto.interfaces.PBEKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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



    @Getter
    public enum CenterPixel {
        CHAT(122),
        MOTD(145);

        private final int center;

        CenterPixel(int center) {
            this.center = center;
        }
    }

    public static String getCenteredMessage(String message, CenterPixel lineLength){
        if(message == null || message.equals("")) return message;
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getLength() + 1 : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CenterPixel.CHAT.getCenter() - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }

    public static String getInfoMessage(String message) {
        return CustomFontCharacter.INFO_ICON + " &#3A86FF" + message;
    }

    public static String getWarningMessage(String message) {
        return CustomFontCharacter.WARNING_ICON + " &#FB2407" + message;
    }

    public static String getChatFormat() {
        return CorePlugin.getInstance().getMainConfig().getChatFormat();
    }

    public static String getStaffChatFormat() {
        return CorePlugin.getInstance().getMainConfig().getStaffChatFormat();
    }

    public static String getDiscordChatFormat() {
        return CorePlugin.getInstance().getMainConfig().getDiscordChatFormat();
    }

    public static String getBanMessageFormat(Punishment punishment) {

        String durationString = punishment.isPermanent() ? "Permanent" : ChatUtils.formatSeconds(punishment.getRemaining());

        final String[] lines = new String[]{
                "&cYou got banned from the server\n",
                "&cReason: &e" +  punishment.getReason().getReason() + "\n",
                "&cDuration: &e" + durationString + "\n",
        };

        StringBuilder kickMessage = new StringBuilder();
        Arrays.stream(lines).forEach(kickMessage::append);
        return kickMessage.toString();
    }

    public static String getMuteMessageFormat(Punishment punishment) {
        if (punishment.isPermanent()) {
            return getWarningMessage("You have been muted indefinitely.");
        } else {
            return getWarningMessage("You have been muted for " + punishment.getReason().getReason() + ". You can't speak for " + formatSeconds(punishment.getRemaining()) + ".");
        }
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

    public static void broadcastMessage(String message) {
        UserManager.getOnlineUsers().forEach(user -> {
            user.sendMessage(message);
        });
    }

    public static void broadcastNotification(String message) {
        UserManager.getOnlineUsers().forEach(user -> {
            user.sendNotification(message);
        });
    }

    public static String formatSeconds(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();
        if (days > 0) {
            formattedTime.append(days).append("d ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append("m ");
        }
        if (remainingSeconds > 0) {
            formattedTime.append(remainingSeconds).append("s");
        }
        return formattedTime.toString().trim();
    }

    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime parseDate(String date) {
        if (date == null || date.isEmpty()) return null;
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

