/*
 * Whitelist
 * Core
 *
 * Created by leobaehre on 8/31/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.config.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Whitelist {

    private boolean enabled;
    private String message;
    private List<String> whitelistedRanks;
}
