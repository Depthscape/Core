/*
 * ChatUtilsTest
 * Core
 *
 * Created by leobaehre on 9/2/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ChatUtilsTest {

    public static final char COLOR_CHAR = '\u00A7';

    private static String getHexColor(String input, int index) {
        // Check for hex color with the format '§x§1§2§3§4§5§6'
        // Our index is currently on the last '§' which means to have a potential hex color
        // The index - 11 must be an 'x' and index - 12 must be a '§'
        // But first check if the string is long enough
        if (index < 12) {
            return null;
        }

        if (input.charAt(index - 11) != 'x' || input.charAt(index - 12) != COLOR_CHAR) {
            return null;
        }

        // We got a potential hex color
        // Now check if every the chars switches between '§' and a hex number
        // First check '§'
        for (int i = index - 10; i <= index; i += 2) {
            if (input.charAt(i) != COLOR_CHAR) {
                return null;
            }
        }

        for (int i = index - 9; i <= (index + 1); i += 2) {
            char toCheck = input.charAt(i);
            if (toCheck < '0' || toCheck > 'f') {
                return null;
            }

            if (toCheck > '9' && toCheck < 'A') {
                return null;
            }

            if (toCheck > 'F' && toCheck < 'a') {
                return null;
            }
        }

        // We got a hex color return it
        return input.substring(index - 12, index + 2);
    }

    @Test
    void colorizeTest() {
        assertEquals("§x§f§b§2§4§0§7", ChatColor.getLastColors("ararea§x§f§b§2§4§0§7"));
        assertEquals("§x§1§2§3§4§5§6", getHexColor("§x§1§2§3§4§5§6", 12));

        String color = ChatColor.getLastColors("ararea§x§f§b§2§4§0§7");

        //assertEquals("a", color.substring(color.length() - 1));

    }

}