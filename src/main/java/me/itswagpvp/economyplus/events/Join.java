package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void setStartBalance (PlayerJoinEvent event) {

        if (event.getPlayer().hasPlayedBefore()) return;
        String playerName = event.getPlayer().getName();

        EconomyPlus.getDBType().setTokens(playerName, EconomyPlus.plugin.getConfig().getDouble("Starting-Balance"));
        EconomyPlus.getDBType().setBank(playerName, EconomyPlus.plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00));

    }
}
