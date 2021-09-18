package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @EventHandler
    public void setStartBalance (PlayerJoinEvent event) {

        if (event.getPlayer().hasPlayedBefore()) return;

        EconomyPlus.getDBType().setTokens(event.getPlayer().getName(), plugin.getConfig().getDouble("Starting-Balance"));
        EconomyPlus.getDBType().setBank(event.getPlayer().getName(), plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00));

    }
}
