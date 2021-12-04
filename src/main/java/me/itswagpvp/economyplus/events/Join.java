package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.misc.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerJoin (PlayerJoinEvent event) {

        new Updater(EconomyPlus.plugin).checkForPlayerUpdate(event.getPlayer());

        if (!event.getPlayer().hasPlayedBefore()) {
            String playerName;

            if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
                playerName = String.valueOf(event.getPlayer().getUniqueId());
            } else if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
                playerName = event.getPlayer().getName();
            } else return;

            EconomyPlus.getDBType().setTokens(playerName, EconomyPlus.plugin.getConfig().getDouble("Starting-Balance", 0.00D));
            EconomyPlus.getDBType().setBank(playerName, EconomyPlus.plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00D));
        }
    }
}
