/*
 * OpenMenuAction
 * Core
 *
 * Created by leobaehre on 9/3/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.utils.menu.action.actions;

import lombok.AllArgsConstructor;
import net.depthscape.core.user.User;
import net.depthscape.core.utils.menu.Menu;
import net.depthscape.core.utils.menu.action.ButtonAction;

@AllArgsConstructor
public class OpenMenuAction extends ButtonAction {

    private Menu otherMenu;

    @Override
    public void execute(User user) {
        if (user.getOpenMenu() != null) {
            user.openMenu(otherMenu);
        }
    }
}
