/*
 * RankManager
 * Core
 *
 * Created by lobae on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.rank;

import lombok.Getter;
import net.depthscape.core.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {

//    private static PermissionsSettings permissionsSettings;

    @Getter
    private static final List<Rank> ranks = new ArrayList<>();

    public static void loadRanks() {

        Map<Rank, String> inheritanceStrings = new HashMap<>();

        DatabaseUtils.executeQuerySync("SELECT * FROM ranks", resultSet -> {
            while (resultSet.next()) {

                Rank rank = new Rank(
                        resultSet.getString("name"),
                        resultSet.getString("chat_prefix"),
                        resultSet.getString("tab_prefix"),
                        resultSet.getInt("weight"),
                        resultSet.getBoolean("staff")
                );

                ranks.add(rank);
//                inheritanceStrings.put(rank, resultSet.getString("inheritance"));
            }


//            if (permissionsSettings == null) {
//                permissionsSettings = new PermissionsSettings();
//            }

            for (Map.Entry<Rank, String> entry : inheritanceStrings.entrySet()) {
                Rank rank = entry.getKey();
                String inheritanceString = entry.getValue();
                if (inheritanceString != null) {
                    Rank inheritance = getRank(inheritanceString);
                    if (inheritance != null) {
                        rank.setInheritance(inheritance);
                    }
                }
            }
        });
    }

    public static Rank getRank(String name) {
        if (name == null || name.isEmpty()) return null;

        for (Rank rank : ranks) {
            if (rank.getName().equals(name)) {
                return rank;
            }
        }

        return null;
    }

    public static void reload() {
        ranks.clear();
        loadRanks();
//        permissionsSettings.reload();
    }

    public static Rank getDefaultRank() {
        return getRank("Player");
    }
}