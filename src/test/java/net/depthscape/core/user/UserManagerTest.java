/*
 * UserCacheTest
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.user;

import net.depthscape.core.rank.Rank;
import net.depthscape.core.rank.RankManager;
import net.depthscape.core.utils.ChatUtils;
import net.depthscape.core.utils.DatabaseUtils;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    @Test
    public void testValidateUser() {

//        UUID uuid = UUID.fromString("f8462f04-9b6b-407d-91d8-1017d2d0e4b1");
//        DatabaseUtils.connect("node.depthscape.net", 3306, "s3_DEVDB", "u3_LaKkc58NWv", "!^svUx.qorqfn8t.ip7Qe07R");
//        assertTrue(DatabaseUtils.isConnected());
//
//        OfflineUser user = UserManager.getOfflineUserSync(uuid);
//        if (user == null) {
//            UserManager.createNewUserSync(uuid);
//            user = UserManager.getOfflineUserSync(uuid);
//        }
//        assertNotNull(user);
//
//        assertEquals(0, user.getCoins());
    }

}