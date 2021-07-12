package me.itswagpvp.economyplus.misc.updater;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

public class UpdateMessage {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    public void updater(int resourceId) {

        if (!plugin.getConfig().getBoolean("Updater.Console")) {
            return;
        }



        long before = System.currentTimeMillis();

        new UpdateChecker(plugin, resourceId).getVersion(version -> {

            if (version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
                return;
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EconomyPlus.plugin, () -> {
                Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                Bukkit.getConsoleSender().sendMessage("            §dEconomy§5Plus");
                Bukkit.getConsoleSender().sendMessage("              §eUpdater");
                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§f-> New version available! §av" + version);
                Bukkit.getConsoleSender().sendMessage("§f-> You have §cv" + plugin.getDescription().getVersion());
                Bukkit.getConsoleSender().sendMessage("§f-> §eDownload it at https://www.spigotmc.org/resources/" + resourceId);
                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");
            }, 40);
        });
    }
}
