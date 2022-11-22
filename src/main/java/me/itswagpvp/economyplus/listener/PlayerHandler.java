package me.itswagpvp.economyplus.listener;

import me.itswagpvp.economyplus.misc.Updater;
import me.itswagpvp.economyplus.vault.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class PlayerHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        FileConfiguration config = plugin.getConfig();
        if(config.get("Updater-Notifications") == null || config.getBoolean("Updater-Notifications", true)) {
            if(p.hasPermission("economyplus.update") || p.isOp()) {
                Updater.checkForPlayerUpdate(p);
            }
        }

        Economy eco = new Economy(p);
        if(!(eco.hasAccount(p))) { //player doesn't have an account
            eco.setBalance(plugin.getConfig().getDouble("Starting-Balance", 0.00D));
            eco.setBank(plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00D));
        }

    }

}