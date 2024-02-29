package net.depthscape.core.utils;

import lombok.Getter;

@Getter
public enum CustomFontCharacter {

    DS_LOGO_1('\u4E5E', 5),          // Character: 乞
    DS_LOGO_2('\u4EA1', 5),          // Character: 亡
    DS_LOGO_WIDE_1('\u5DDD', 5),     // Character: 川
    DS_LOGO_WIDE_2('\u4EBF', 5),     // Character: 亿
    PAUSE_MENU('\u4E59', 5),         // Character: 乙
    UI_TUTORIAL('\u4E8C', 5),        // Character: 二
    BLACK_SCREEN('\u4E01', 5),       // Character: 丁
    COIN_NUMBERS('\u4E03', 5),       // Character: 七
    COIN_ICON('\u5382', 5),          // Character: 厂
    SCROLLABLE_MIDDLE('\u5C0F', 5),  // Character: 小
    SCROLLABLE_LEFT('\u53E3', 5),    // Character: 口
    SCROLLABLE_RIGHT('\u5C71', 5),   // Character: 山
    SCROLLABLE_LOCKED('\u5DFE', 5),  // Character: 巾
    DOUBLE_CHEST('\u5341', 5),       // Character: 十
    SMALL_CHEST('\u4E0B', 5),        // Character: 下
    MENU_TITLES_1('\u58EB', 5),      // Character: 士
    MENU_TITLES_2('\u624D', 5),      // Character: 才
    MENU_TITLES_3('\u5BF8', 5),      // Character: 寸
    MENU_TITLES_4('\u4E0A', 5),      // Character: 上
    SCRAP_METAL_ICON('\u4E07', 5),   // Character: 万
    WARNING_ICON('\u5343', 5),       // Character: 千
    INFO_ICON('\u4E2A', 5),          // Character: 个
    AFK_ICON('\u4E0E', 5),           // Character: 与
    DISCORD_ICON('\u52FA' , 5),      // Character: 勺
    HELPER_VANISH('\u4E08', 5),      // Character: 丈
    MODERATOR_VANISH('\u5927', 5),   // Character: 大
    ADMIN_VANISH('\u5DE5', 5),       // Character: 工
    SURVIVOR('\u4E8F', 5),           // Character: 荏
    BUILDER('\u4E43', 5),            // Character: 䐃
    HELPER('\u529B', 5),             // Character: 力
    MODERATOR('\u5200', 5),          // Character: 刀
    ADMIN('\u4E86', 5),              // Character: 了
    VANISHED('\u5915', 10),          // Character: 夕
    CLOCK('\u4E45', 10),             // Character: 久
    VANISH_ON('\u4E8C', 126),        // Character: 之
    VANISH_OFF('\u4E48', 126);       // Character: 么

    private final char character;
    private final int length;

    CustomFontCharacter(char character, int length) {
        this.character = character;
        this.length = length;
    }

    public static CustomFontCharacter getCharacter(char c) {
    	for (CustomFontCharacter dFI : CustomFontCharacter.values()) {
            if (dFI.getCharacter() == c) return dFI;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }
}