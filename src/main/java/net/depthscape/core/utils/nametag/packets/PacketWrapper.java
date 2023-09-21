/*
 * PacketWrapper
 * Core
 *
 * Created by leobaehre on 9/2/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.nametag.packets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class PacketWrapper {

    public String error;
    private final Object packet = PacketAccessor.createPacket();
    private final Object packetParams = PacketAccessor.createPacketParams();

    private static Method CraftChatMessage;
    private static Class<? extends Enum> typeEnumChatFormat;
    private static Method getByHexValue;
    private static Enum RESET_COLOR;

    static {
        try {
            typeEnumChatFormat = (Class<? extends Enum>) Class.forName("net.minecraft.EnumChatFormat");
            // get methode from EnumChatFormat called getByHexValue(int i)
            getByHexValue = typeEnumChatFormat.getMethod("getByHexValue", int.class);

            Class<?> typeCraftChatMessage = Class.forName("org.bukkit.craftbukkit." + PacketAccessor.VERSION + ".util.CraftChatMessage");
            CraftChatMessage = typeCraftChatMessage.getMethod("fromString", String.class);
            RESET_COLOR = Enum.valueOf(typeEnumChatFormat, "RESET");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PacketWrapper(String name, int param, List<String> members) {
        if (param != 3 && param != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        setupDefaults(name, param);
        setupMembers(members);
    }

    @SuppressWarnings("unchecked")
    public PacketWrapper(String name, String prefix, String suffix, int param, Collection<?> players, boolean visible) {
        Bukkit.getLogger().info("Name: " + name);
        setupDefaults(name, param);
        if (param == 0 || param == 2) {
            try {
                String color = ChatColor.getLastColors(prefix);
                String colorCode = null;
                Enum<?> colorEnum = null;

                if (!color.isEmpty()) {
                    colorCode = color.substring(color.length() - 1);
                    String chatColor = ChatColor.getByChar(colorCode).name();

                    if (chatColor.equalsIgnoreCase("MAGIC"))
                        chatColor = "OBFUSCATED";

                    // get color from hex integer 16458759
                    colorEnum = Enum.valueOf(typeEnumChatFormat, chatColor);
//                    colorEnum = (Enum<?>) getByHexValue.invoke(colorEnum, 16755200);
//                    colorEnum = (Enum<?>) getByHexValue.invoke(colorEnum, 16458759);
                    Bukkit.getLogger().info("ColorEnum: " + colorEnum);
                }

                if (colorCode != null)
                    suffix = ChatColor.getByChar(colorCode) + suffix;

                PacketAccessor.TEAM_COLOR.set(packetParams, colorEnum == null ? RESET_COLOR : colorEnum);
                PacketAccessor.DISPLAY_NAME.set(packetParams, Array.get(CraftChatMessage.invoke(null, name), 0));
                PacketAccessor.PREFIX.set(packetParams, Array.get(CraftChatMessage.invoke(null, prefix), 0));
                PacketAccessor.SUFFIX.set(packetParams, Array.get(CraftChatMessage.invoke(null, suffix), 0));


                PacketAccessor.PACK_OPTION.set(packetParams, 1);

                if (PacketAccessor.VISIBILITY != null) {
                    PacketAccessor.VISIBILITY.set(packetParams, visible ? "always" : "never");
                }

                if (param == 0) {
                    ((Collection) PacketAccessor.MEMBERS.get(packet)).addAll(players);
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setupMembers(Collection<?> players) {
        try {
            players = players == null || players.isEmpty() ? new ArrayList<>() : players;
            ((Collection) PacketAccessor.MEMBERS.get(packet)).addAll(players);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    private void setupDefaults(String name, int param) {
        try {
            PacketAccessor.TEAM_NAME.set(packet, name);
            PacketAccessor.PARAM_INT.set(packet, param);

            // 1.17+ These null values are not allowed, this initializes them.
            PacketAccessor.MEMBERS.set(packet, new ArrayList<>());
            PacketAccessor.PUSH.set(packetParams, "");
            PacketAccessor.VISIBILITY.set(packetParams, "");
            PacketAccessor.TEAM_COLOR.set(packetParams, RESET_COLOR);

        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    private void constructPacket() {
        try {
            PacketAccessor.PARAMS.set(packet, Optional.ofNullable(packetParams));
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    public void send() {
        constructPacket();
        PacketAccessor.sendPacket(getOnline(), packet);
    }

    public void send(Player player) {
        constructPacket();
        PacketAccessor.sendPacket(player, packet);
    }

    private List<Player> getOnline() {
        List<Player> list = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            list.addAll(world.getPlayers());
        }

        return Collections.unmodifiableList(list);
    }

}