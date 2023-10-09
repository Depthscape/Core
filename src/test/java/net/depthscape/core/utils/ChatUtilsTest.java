/*
 * ChatUtilsTest
 * Core
 *
 * Created by leobaehre on 9/2/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

class ChatUtilsTest {


   @Test
   void colorizeTest() {

       String text = "rartatioai";

        int padding = 3;

        int textWidth = DefaultFontInfo.getStringLength(text, false);
            System.out.println("textWidth: " + textWidth);

        List<BossBarCharacter> closestBossbarCenters = BossBarCharacter.getCenters(textWidth + padding * 2);
        // + padding * 2
        int centerWidth = closestBossbarCenters.stream().mapToInt(BossBarCharacter::getWidth).sum();
            System.out.println("centerWidth: " + centerWidth);
            System.out.println("closestBossbarCenters: " + closestBossbarCenters.stream().map(BossBarCharacter::getWidth).toList());

        // end + -1 + center + -1 + end + x + text
        //x = textWidth / 2 + centerWidth / 2

        int x = -(textWidth / 2 + centerWidth / 2) - 2;

       // (closestBossbarCenters.size() > 1 ? closestBossbarCenters.size() : 0) / 2)

            System.out.println("x: " + x);

        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("&#4e5c24").append(BossBarCharacter.BOSSBAR_END).append(UnicodeSpace.MINUS_1);
            for (BossBarCharacter customFontCharacter : closestBossbarCenters) {
            stringBuilder.append(customFontCharacter.getCharacter())
                    .append(UnicodeSpace.MINUS_1);

        }
            stringBuilder.append(BossBarCharacter.BOSSBAR_END)
                    .append(UnicodeSpace.findBestCombination(x))
                    .append("&r")
                    .append(text);

       System.out.println(stringBuilder.toString());

       //assertEquals("a", stringBuilder.toString());
   }
//
//    @Test
//    void combinationTest() {
//        int target = 44;
//        List<BossBarCharacter> efficientCombination = BossBarCharacter.getCenters(target);
//
//        List<Character> actual = efficientCombination.stream().map(BossBarCharacter::getCharacter).toList();
//
//        assertEquals(List.of('ꈇ', 'ꈅ', 'ꈄ'), actual);
//
//        int target2 = -45;
//        //List<UnicodeSpace> bestCombination = UnicodeSpace.findBestCombination(target2);
//        //assertEquals(List.of(UnicodeSpace.MINUS_32.getUnicode(), UnicodeSpace.MINUS_9.getUnicode(), UnicodeSpace.MINUS_4.getUnicode()), bestCombination);
//    }

}