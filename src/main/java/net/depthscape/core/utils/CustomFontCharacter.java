package net.depthscape.core.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CustomFontCharacter {

    DS_LOGO_1('\u4E5E'),   // Character: 乞
    DS_LOGO_2('\u4EA1'),   // Character: 亡
    DS_LOGO_WIDE_1('\u5DDD'), // Character: 川
    DS_LOGO_WIDE_2('\u4EBF'), // Character: 亿
    PAUSE_MENU('\u4E59'),     // Character: 乙
    UI_TUTORIAL('\u4E8C'),    // Character: 二
    BLACK_SCREEN('\u4E01'),   // Character: 丁
    COIN_NUMBERS('\u4E03'),   // Character: 七
    COIN_ICON('\u5382'),       // Character: 厂
    SCROLLABLE_MIDDLE('\u5C0F'),  // Character: 小
    SCROLLABLE_LEFT('\u53E3'),    // Character: 口
    SCROLLABLE_RIGHT('\u5C71'),   // Character: 山
    SCROLLABLE_LOCKED('\u5DFE'),  // Character: 巾
    DOUBLE_CHEST('\u5341'),       // Character: 十
    SMALL_CHEST('\u4E0B'),       // Character: 下
    MENU_TITLES_1('\u58EB'),     // Character: 士
    MENU_TITLES_2('\u624D'),     // Character: 才
    MENU_TITLES_3('\u5BF8'),     // Character: 寸
    MENU_TITLES_4('\u4E0A'),     // Character: 上
    SCRAP_METAL_ICON('\u4E07'),   // Character: 万
    WARNING_ICON('\u5343'),      // Character: 千
    INFO_ICON('\u4E2A'),         // Character: 个
    AFK_ICON('\u4E0E'),          // Character: 与
    HELPER_VANISH('\u4E08'),     // Character: 丈
    MODERATOR_VANISH('\u5927'),  // Character: 大
    ADMIN_VANISH('\u5DE5'),      // Character: 工
    SURVIVOR('\u4E8F'),          // Character: 荏
    BUILDER('\u4E43'),           // Character: 䐃
    HELPER('\u529B'),            // Character: 力
    MODERATOR('\u5200'),         // Character: 刀
    ADMIN('\u4E86'),             // Character: 了
    BOSSBAR_CENTER_1('ꈂ'),      // Character: ꈂ
    BOSSBAR_CENTER_2('ꈃ'),      // Character: ꈃ
    BOSSBAR_CENTER_4('ꈄ'),      // Character: ꈄ
    BOSSBAR_CENTER_8('ꈅ'),      // Character: ꈅ
    BOSSBAR_CENTER_16('ꈆ'),     // Character: ꈆ
    BOSSBAR_CENTER_32('ꈇ'),     // Character: ꈇ
    BOSSBAR_CENTER_64('ꈈ'),     // Character: ꈈ
    BOSSBAR_CENTER_128('ꈉ'),    // Character: ꈉ
    BOSSBAR_END('ꈁ');          // Character: ꈁ

    private final char character;

    CustomFontCharacter(char character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }

    public static List<CustomFontCharacter> getClosestBossbarCenters(int pixels) {
        List<CustomFontCharacter> closestCenters = new ArrayList<>();
        int closestDifference = Integer.MAX_VALUE;

        for (CustomFontCharacter character : CustomFontCharacter.values()) {
            if (character.name().startsWith("BOSSBAR_CENTER_")) {
                int centerPixels = Integer.parseInt(character.name().substring("BOSSBAR_CENTER_".length()));
                int difference = Math.abs(centerPixels - pixels);

                if (difference < closestDifference) {
                    closestCenters.clear(); // Clear the previous closest values
                    closestCenters.add(character);
                    closestDifference = difference;
                } else if (difference == closestDifference) {
                    closestCenters.add(character);
                }
            }
        }

        return closestCenters;
    }
}