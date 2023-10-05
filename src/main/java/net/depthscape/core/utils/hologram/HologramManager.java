package net.depthscape.core.utils.hologram;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    protected static List<Hologram> holograms = new ArrayList<>();

    public void registerHologram(final Hologram hologram) {
        if (!holograms.contains(hologram)) {
            holograms.add(hologram);
        }
    }

    public Hologram getHologram(final String id) {
        for (final Hologram hologram : holograms) {
            if (hologram.getId().equals(id)) return hologram;
        }
        return null;
    }

    public void registerAllHolograms() {
        //holograms.addAll(LobbyPlugin.getHologramSettings().getHolograms());
    }

    public void showAllHolos(final Player player) {
        for (final Hologram hologram : holograms) {
            hologram.spawn(player);
        }
    }

    public void refreshHolos() {
        holograms.forEach(hologram -> Bukkit.getOnlinePlayers().forEach(hologram::refresh));
    }

    public void reloadHolos() {

    }
}
