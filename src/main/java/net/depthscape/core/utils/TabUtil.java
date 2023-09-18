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
}
