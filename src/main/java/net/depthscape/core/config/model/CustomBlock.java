/*
 * CustomBlock
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Instrument;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public class CustomBlock {

    String itemDisplayName;
    Material ItemMaterial;
    int customModelData;

    int blockNote;
    Instrument blockInstrument;

}
