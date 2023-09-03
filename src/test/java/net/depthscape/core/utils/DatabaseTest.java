/*
 * DatabaseTest
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    public void testDatabaseConnection() {

        DatabaseUtils.connect("node.depthscape.net", 3306, "s3_DEVDB", "u3_LaKkc58NWv", "!^svUx.qorqfn8t.ip7Qe07R");

        assertTrue(DatabaseUtils.isConnected());
    }

}