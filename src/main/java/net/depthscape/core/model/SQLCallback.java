/*
 * SQLCallback
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLCallback {

    void call(ResultSet result) throws SQLException;
}
