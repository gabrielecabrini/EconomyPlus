package me.itswagpvp.economyplus.hooks.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.BalTopManager;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.misc.StorageManager;
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

    public void createHologram () {

        Hologram hologram = HologramsAPI.createHologram(plugin, loc);

        List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

        EconomyPlus.balTopManager = new BalTopManager();
        new BalTopManager();
        BalTopManager balTopManager = plugin.getBalTopManager();

        Utils utilities = new Utils();

        int page = 1;

        int start = 0;

        for(String message : header) {
            hologram.appendTextLine(message
                    .replaceAll("%page%", "" + page)
                    .replaceAll("&", "§"));
        }

        for (int i = start; i < balTopManager.getBalTop().size() && i < start + 10; i++ ) {
            BalTopManager.PlayerData pData = balTopManager.getBalTop().get(i);

            String name = pData.getName();
            double money = pData.getMoney();

            hologram.appendTextLine(plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1))
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + utilities.format(money))
                    .replaceAll("%money_formatted%","" + utilities.fixMoney(money)));
        }

        long refreshRate = plugin.getConfig().getLong("Baltop.Hologram.Refresh-Rate", 60) * 20L;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            for (Hologram holograms : HologramsAPI.getHolograms(plugin)) {
                holograms.delete();
            }

            new HolographicDisplays().refreshHologram();
        }, 0L, refreshRate);
    }

    public void refreshHologram () {

        Hologram hologram = HologramsAPI.createHologram(plugin, loc);

        List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

        EconomyPlus.balTopManager = new BalTopManager();
        new BalTopManager();
        BalTopManager balTopManager = plugin.getBalTopManager();

        Utils utilities = new Utils();

        int page = 1;

        int start = 0;

        for(String message : header) {
            hologram.appendTextLine(message
                    .replaceAll("%page%", "" + page)
                    .replaceAll("&", "§"));
        }

        for (int i = start; i < balTopManager.getBalTop().size() && i < start + 10; i++ ) {
            BalTopManager.PlayerData pData = balTopManager.getBalTop().get(i);

            String name = pData.getName();
            double money = pData.getMoney();

            hologram.appendTextLine(plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1))
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + utilities.format(money))
                    .replaceAll("%money_formatted%","" + utilities.fixMoney(money)));
        }
    }
}
