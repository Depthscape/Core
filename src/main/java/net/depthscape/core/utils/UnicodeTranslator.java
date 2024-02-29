package net.depthscape.core.utils;

import java.util.HashMap;

public class UnicodeTranslator {
    
    private static final HashMap<Character, String> unicodeMap = new HashMap<>();
    
    static {
        unicodeMap.put('A', "\u827A");
        unicodeMap.put('B', "\u4E38");
        unicodeMap.put('C', "\u53CA");
        unicodeMap.put('D', "\u5E7F");
        unicodeMap.put('E', "\u6728");
        unicodeMap.put('F', "\u95E8");
        unicodeMap.put('G', "\u4E2B");
        unicodeMap.put('H', "\u4E49");
        unicodeMap.put('I', "\u4E4B");
        unicodeMap.put('J', "\u5C38");
        unicodeMap.put('K', "\u5DF1");
        unicodeMap.put('L', "\u5DF2");
        unicodeMap.put('M', "\u5DF3");
        unicodeMap.put('N', "\u5F13");
        unicodeMap.put('O', "\u5B50");
        unicodeMap.put('P', "\u536B");
        unicodeMap.put('Q', "\u4E5F");
        unicodeMap.put('R', "\u5973");
        unicodeMap.put('S', "\u5203");
        unicodeMap.put('T', "\u98DE");
        unicodeMap.put('U', "\u4E60");
        unicodeMap.put('V', "\u53C9");
        unicodeMap.put('W', "\u9A6C");
        unicodeMap.put('X', "\u4E61");
        unicodeMap.put('Y', "\u4E30");
        unicodeMap.put('Z', "\u738B");
        unicodeMap.put('_', "\u5F00");
        unicodeMap.put('0', "\u624E");
        unicodeMap.put('1', "\u4E95");
        unicodeMap.put('2', "\u5929");
        unicodeMap.put('3', "\u592B");
        unicodeMap.put('4', "\u5143");
        unicodeMap.put('5', "\u65E0");
        unicodeMap.put('6', "\u4E91");
        unicodeMap.put('7', "\u4E13");
        unicodeMap.put('8', "\u4E10");
        unicodeMap.put('9', "\u4E94");
        unicodeMap.put('\'', "\u652F");
    }
    
    public static String translateToUnicode(String normalString) {
        StringBuilder sb = new StringBuilder();
        for (char c : normalString.toUpperCase().toCharArray()) {
            if (unicodeMap.containsKey(c)) {
                sb.append(unicodeMap.get(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String normalString = "Hoi ik ben dom";
        System.out.println("Normal string: " + normalString);
        System.out.println("Translated string: " + translateToUnicode(normalString));
    }
}