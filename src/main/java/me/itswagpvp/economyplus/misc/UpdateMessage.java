package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

public class UpdateMessage {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    public void updater(int resourceId) {

        long before = System.currentTimeMillis();

        if (!plugin.getConfig().getBoolean("Updater.Console")) {
            return;
        }

        /*if (!Bukkit.getVersion().contains("1.12")
                || !Bukkit.getVersion().contains("1.13")
                || !Bukkit.getVersion().contains("1.14")
                || !Bukkit.getVersion().contains("1.15")
                || !Bukkit.getVersion().contains("1.16")) {
            return;
        }*/

        new UpdateChecker(plugin, resourceId).getVersion(version -> {

            if (version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
                return;
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EconomyPlus.plugin, () -> {
                Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                Bukkit.getConsoleSender().sendMessage("            §dEconomyPlus");
                Bukkit.getConsoleSender().sendMessage("              §eUpdater");
                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§f-> New version available! §av" + version);
                Bukkit.getConsoleSender().sendMessage("§f-> You have §cv" + plugin.getDescription().getVersion());
                Bukkit.getConsoleSender().sendMessage("§f-> §eDownload it at https://www.spigotmc.org/resources/" + resourceId);
                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before - 4000) + "ms §8]-------------+");
            }, 40);
        });
    }
}
