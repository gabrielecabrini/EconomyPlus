package me.itswagpvp.economyplus.listener;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import me.itswagpvp.economyplus.misc.Updater;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        FileConfiguration config = EconomyPlus.plugin.getConfig();
        if(config.get("Updater-Notifications") == null || config.getBoolean("Updater-Notifications", true)) {
            if(event.getPlayer().hasPermission("economyplus.update") || event.getPlayer().isOp()) {
                Updater.checkForPlayerUpdate(event.getPlayer());
            }
        }

        String playerName = Selector.playerToString(event.getPlayer());

        if (!EconomyPlus.getDBType().getList().contains(playerName)) {
            Economy eco = new Economy(event.getPlayer());
            eco.setBalance(EconomyPlus.plugin.getConfig().getDouble("Starting-Balance", 0.00D));
            eco.setBank(EconomyPlus.plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00D));
        }

    }
}
