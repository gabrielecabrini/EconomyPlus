package me.itswagpvp.economyplus.hooks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Data;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class HolographicDisplays {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    World w = Bukkit.getWorld(plugin.getConfig().getString("Hologram.World"));
    int x = plugin.getConfig().getInt("Hologram.X");
    int y = plugin.getConfig().getInt("Hologram.Y");
    int z = plugin.getConfig().getInt("Hologram.Z");

    Location loc = new Location(w, x, y, z);

    Hologram hologram = HologramsAPI.createHologram(plugin, loc);

    public void createHologram () {

        List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

        EconomyPlus.data = new Data();
        new Data();
        Data data = EconomyPlus.plugin.getData();

        Utils utilities = new Utils();

        int page = 1;

        int start = 0;

        for(String message : header) {
            hologram.appendTextLine(message
                    .replaceAll("%page%", "" + page)
                    .replaceAll("&", "§"));
        }

        for ( int i = start; i < data.getBalTop().size() && i < start + 10; i++ ) {
            Data.PlayerData pData = data.getBalTop().get(i);

            String name = pData.getName();
            double money = pData.getMoney();

            hologram.appendTextLine(plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1))
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + utilities.fixMoney(money)));
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
                hologram.delete();
            }

            new HolographicDisplays().refreshHologram();
        }, 0L, 1200L);
    }

    public void refreshHologram () {

        List<String> header = plugin.getConfig().getStringList("Baltop.Hologram.Header");

        EconomyPlus.data = new Data();
        new Data();
        Data data = EconomyPlus.plugin.getData();

        Utils utilities = new Utils();

        int page = 1;

        int start = 0;

        for(String message : header) {
            hologram.appendTextLine(message
                    .replaceAll("%page%", "" + page)
                    .replaceAll("&", "§"));
        }

        for ( int i = start; i < data.getBalTop().size() && i < start + 10; i++ ) {
            Data.PlayerData pData = data.getBalTop().get(i);

            String name = pData.getName();
            double money = pData.getMoney();

            hologram.appendTextLine(plugin.getConfig().getString("Baltop.Hologram.Player-Format")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1))
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + utilities.fixMoney(money)));
        }
    }

}
