/*
 * Colors
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import org.bukkit.ChatColor;

public class Colors {
  public static String getColored(String msg) {
    if (msg == null)
      return ""; 
    if (Pattern.compile("\\{#[0-9A-Fa-f]{6}}").matcher(msg).find()) {
      Matcher m = Pattern.compile("\\{#[0-9A-Fa-f]{6}}").matcher(msg);
      while(m.find()) {
        String s = m.group();
        String sNew = "§x" + Arrays.stream(s.split("")).map((s2) -> "§" + s2)
                .collect(Collectors.joining()).replace("§#", "");
        msg = msg.replace(s, sNew.replace("§{", "").replace("§}", ""));
      }
      return ChatColor.translateAlternateColorCodes('&', msg);
    } 
    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  @Getter
  public enum DefaultColor {
    DARK_RED("dark_red", "4", "#AA0000"),
    RED("red", "c", "#FF5555"),
    GOLD("gold", "6", "#FFAA00"),
    YELLOW("yellow", "e", "#FFFF55"),
    DARK_GREEN("dark_green", "2", "#00AA00"),
    GREEN("green", "a", "#55FF55"),
    AQUA("aqua", "b", "#55FFFF"),
    DARK_AQUA("dark_aqua", "3", "#00AAAA"),
    DARK_BLUE("dark_blue", "1", "#0000AA"),
    BLUE("blue", "9", "#5555FF"),
    LIGHT_PURPLE("light_purple", "d", "#FF55FF"),
    DARK_PURPLE("dark_purple", "5", "#AA00AA"),
    WHITE("white", "f", "#FFFFFF"),
    GRAY("gray", "7", "#AAAAAA"),
    DARK_GRAY("dark_gray", "8", "#555555"),
    BLACK("black", "0", "#000000");


    private final String name;
    private final String code;
    private final String hex;
    private final int r;
    
    private final int g;
    
    private final int b;
    
    DefaultColor(String name, String code, String hex) {
      this.name = name;
      this.code = code;
      this.hex = hex;
      Color color = hex2Rgb(hex);
      this.r = color.getRed();
      this.g = color.getGreen();
      this.b = color.getBlue();
    }
    
    public Color getColor() {
      return new Color(this.r, this.g, this.b);
    }
    
    public static String getClosestFromHex(String hex) {
      Color color2 = hex2Rgb(hex);
      int closestInt = -1;
      DefaultColor closestColor = null;
      for (DefaultColor color1 : values()) {
        int result = distance(color1, color2);
        if (closestInt > result || closestInt == -1) {
          closestInt = result;
          closestColor = color1;
        } 
      } 
      if (closestColor == null)
        return "f"; 
      return closestColor.getCode();
    }
    
    private static int distance(DefaultColor color1, Color color2) {
      return Math.abs(color1.r - color2.getRed()) + Math.abs(color1.g - color2.getGreen()) + Math.abs(color1.b - color2.getBlue());
    }
    
    public static Color hex2Rgb(String colorStr) {
      return new Color(
              Integer.valueOf(colorStr.substring(1, 3), 16),
              Integer.valueOf(colorStr.substring(3, 5), 16),
              Integer.valueOf(colorStr.substring(5, 7), 16));
    }
  }
}