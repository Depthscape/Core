package net.depthscape.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@AllArgsConstructor
@Getter
public enum BossBarCharacter {
    BOSSBAR_CENTER_1('ꈂ', 1),      // Character: ꈂ
    BOSSBAR_CENTER_2('ꈃ', 2),      // Character: ꈃ
    BOSSBAR_CENTER_4('ꈄ', 4),      // Character: ꈄ
    BOSSBAR_CENTER_8('ꈅ', 8),      // Character: ꈅ
    BOSSBAR_CENTER_16('ꈆ', 16),     // Character: ꈆ
    BOSSBAR_CENTER_32('ꈇ', 32),     // Character: ꈇ
    BOSSBAR_CENTER_64('ꈈ', 64),     // Character: ꈈ
    BOSSBAR_CENTER_128('ꈉ', 128),    // Character: ꈉ
    BOSSBAR_END('ꈁ', -1);          // Character: ꈁ

    private final char character;
    private final int width;

    public static List<BossBarCharacter> getCenters(int pixels) {
        List<BossBarCharacter> efficientCombination = new ArrayList<>();

        int remainingPixels = pixels;

        int[] values = Arrays.stream(values()).map(BossBarCharacter::getWidth).mapToInt(Integer::intValue).toArray();
        for (int i = values.length - 1; i >= 0; i--) {
            int value = values[i];
            if (value == -1) {
                continue;
            }
            if (remainingPixels >= value) {
                remainingPixels -= value;
                efficientCombination.add(values()[i]);
            }
        }

        return efficientCombination;
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }
}
