package net.depthscape.core.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum UnicodeSpace {
    MINUS_1024('\uF80F', -1024),
    MINUS_512('\uF80E', -512),
    MINUS_256('\uF80D', -256),
    MINUS_128('\uF80C', -128),
    MINUS_64('\uF80B', -64),
    MINUS_32('\uF80A', -32),
    MINUS_16('\uF809', -16),
    MINUS_9('\uF808', -9),
    MINUS_8('\uF807', -8),
    MINUS_6('\uF806', -6),
    MINUS_5('\uF805', -5),
    MINUS_4('\uF804', -4),
    MINUS_3('\uF803', -3),
    MINUS_2('\uF802', -2),
    MINUS_1('\uF801', -1),
    PLUS_1024('\uF82F', 1024),
    PLUS_512('\uF82E', 512),
    PLUS_256('\uF82D', 256),
    PLUS_128('\uF82C', 128),
    PLUS_64('\uF82B', 64),
    PLUS_32('\uF82A', 32),
    PLUS_16('\uF829', 16),
    PLUS_9('\uF828', 9),
    PLUS_8('\uF827', 8),
    PLUS_6('\uF826', 6),
    PLUS_5('\uF825', 5),
    PLUS_4('\uF824', 4),
    PLUS_3('\uF823', 3),
    PLUS_2('\uF822', 2),
    PLUS_1('\uF821', 1);

    private final char unicode;
    private final int spaces;

    UnicodeSpace(char unicode, int spaces) {
        this.unicode = unicode;
        this.spaces = spaces;
    }

    public static String find(int target) {
        if (target == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<UnicodeSpace> values;
        boolean negative = target < 0;
        if (negative) {
            values = Arrays.stream(values()).filter(unicodeSpace -> unicodeSpace.getSpaces() < 0).toList();
        } else {
            values = Arrays.stream(values()).filter(unicodeSpace -> unicodeSpace.getSpaces() > 0).toList();
        }

        int remainingPixels = target;

        while (remainingPixels != 0) {
            UnicodeSpace bestFit = bestFit(remainingPixels, values, negative);
            if (bestFit == null) {
                break;
            }
            remainingPixels -= bestFit.getSpaces();
            stringBuilder.append(bestFit.getUnicode());
        }


        return stringBuilder.toString();
    }

    private static UnicodeSpace bestFit(int target, List<UnicodeSpace> values, boolean negative) {
        for (UnicodeSpace value : values) {
            if (negative) {
                if (value.getSpaces() >= target) {
                    return value;
                }
            } else {
                if (value.getSpaces() <= target) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(unicode);
    }
}