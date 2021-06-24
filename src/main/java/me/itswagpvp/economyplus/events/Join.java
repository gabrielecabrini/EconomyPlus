package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.updater.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @EventHandler
    public void updateMessage (PlayerJoinEvent event) {

        if (!plugin.getConfig().getBoolean("Updater.Player")) {
            return;
        }

        Player p = event.getPlayer();

        if (!p.hasPermission("economyplus.updater")) {
            return;
        }

        int resourceId = 92975;
        new UpdateChecker(plugin, resourceId).getVersion(version -> {

            if (version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
                return;
            }

            String currentVersion = plugin.getDescription().getVersion();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EconomyPlus.plugin, () -> {

                p.sendMessage("§dEconomyPlus:");
                p.sendMessage("§7There's a new update of the plugin!");
                p.sendMessage("§7You have §cv" + currentVersion + " §7instead of §av" + version);

            }, 0);
        });
    }
}
