package net.depthscape.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TabUtil {

    public static List<String> complete(String partialName, Iterable<String> all) {
        final ArrayList<String> tab = new ArrayList<>();

        for (final String s : all)
            tab.add(s);

        partialName = partialName.toLowerCase();

        for (final Iterator<String> iterator = tab.iterator(); iterator.hasNext();) {
            final String val = iterator.next();

            if (!val.toLowerCase().startsWith(partialName))
                iterator.remove();
        }

        Collections.sort(tab);

        return tab;
    }

    /**
     * This is a special method to sort nametags in
     * the tablist. It takes a priority and converts
     * it to an alphabetic representation to force a
     * specific sort.
     *
     * @param input the sort priority
     * @return the team name
     */
    public static String getNameFromInput(int input) {
        if (input < 0) return "Z";
        char letter = (char) ((input / 5) + 65);
        int repeat = input % 5 + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            builder.append(letter);
        }
        return builder.toString();
    }


}
