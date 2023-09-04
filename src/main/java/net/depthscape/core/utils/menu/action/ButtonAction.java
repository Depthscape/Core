/*
 * ButtonAction
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu.action;

import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.Menu;

public abstract class ButtonAction {

    abstract public void execute(User user);
}
