/*
 * BlocksConfig
 * Core
 *
 * Created by leobaehre on 9/1/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.depthscape.core.config;

import net.depthscape.core.CorePlugin;
import net.depthscape.core.config.model.CustomBlock;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlocksConfig {

    private final String path = "blocks.yml";

    private final YamlConfiguration config;

    private List<CustomBlock> customBlocks = new ArrayList<>();

    public BlocksConfig(CorePlugin plugin) {
        File file = new File(plugin.getDataFolder(), path);

        if (!file.exists()) {
            plugin.saveResource(path, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        for (String key : config.getKeys(false)) {
            ConfigurationSection blockSection = config.getConfigurationSection(key);
            String itemDisplayName = blockSection.getString("Item_Display_Name");
            Material itemMaterial = Material.valueOf(blockSection.getString("Item_Material"));
            int customModelData = blockSection.getInt("Custom_Model_Data");
            int blockNote = blockSection.getInt("Block_Note");
            String blockInstrument = blockSection.getString("Block_Instrument");

            Instrument instrument = Instrument.valueOf(blockInstrument);

            CustomBlock customBlock = new CustomBlock(itemDisplayName, itemMaterial, customModelData, blockNote, instrument);
            customBlocks.add(customBlock);
        }
    }
}
