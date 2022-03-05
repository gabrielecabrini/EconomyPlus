package me.itswagpvp.economyplus.hooks.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.BalTopManager;
import me.itswagpvp.economyplus.misc.StorageManager;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class HolographicDisplays {

    StorageManager storageManager = new StorageManager();

    World w = Bukkit.getWorld(storageManager.getStorageConfig().getString("Hologram.BalTop.World"));
    double x = storageManager.getStorageConfig().getDouble("Hologram.BalTop.X");
    double y = storageManager.getStorageConfig().getDouble("Hologram.BalTop.Y");
    double z = storageManager.getStorageConfig().getDouble("Hologram.BalTop.Z");

    Location loc = new Location(w, x, y, z);

    public void createHologram() {

        for (Hologram holo : HologramsAPI.getHolograms(plugin)) {
            holo.delete();
        }

        Hologram hologram = HologramsAPI.createHologram(plugin, loc);

        List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

        EconomyPlus.balTopManager = new BalTopManager();
        BalTopManager balTopManager = plugin.getBalTopManager();

        Utils utilities = new Utils();

        for (String message : header) {
            hologram.appendTextLine(message
                    .replaceAll("%page%", "" + 1)
                    .replaceAll("&", "§"));
        }

        for (int i = 0; i < balTopManager.getBalTop().size() && i < 10; i++) {
            BalTopManager.PlayerData pData = balTopManager.getBalTop().get(i);

            String name = pData.getName();
            double money = pData.getMoney();

            hologram.appendTextLine(plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1))
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + utilities.format(money))
                    .replaceAll("%money_formatted%", "" + utilities.fixMoney(money)));
        }

        long refreshRate = plugin.getConfig().getLong("Baltop.Hologram.Refresh-Rate", 60) * 20L;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> new HolographicDisplays().refreshHologram(), 0L, refreshRate);
    }

    public void refreshHologram() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            EconomyPlus.balTopManager = new BalTopManager();
            BalTopManager balTopManager = plugin.getBalTopManager();

            Utils utilities = new Utils();

            List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

            for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {

                Bukkit.getScheduler().runTask(plugin, hologram::clearLines);

                for (String message : header) {
                    Bukkit.getScheduler().runTask(plugin, () -> hologram.appendTextLine(message
                            .replaceAll("%page%", "" + 1)
                            .replaceAll("&", "§")));
                }

                for (int i = 0; i < balTopManager.getBalTop().size() && i < 10; i++) {
                    BalTopManager.PlayerData pData = balTopManager.getBalTop().get(i);

                    String name = pData.getName();
                    double money = pData.getMoney();

                    int finalI = i;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        int line = finalI + header.size();
                        hologram.insertTextLine(line, plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                                .replaceAll("&", "§")
                                .replaceAll("%number%", "" + (finalI + 1))
                                .replaceAll("%player%", "" + name)
                                .replaceAll("%money%", "" + utilities.format(money))
                                .replaceAll("%money_formatted%", "" + utilities.fixMoney(money)));
                    });
                }
            }
        });
    }
}
