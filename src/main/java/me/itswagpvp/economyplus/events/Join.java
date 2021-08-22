package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.storage.mysql.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @EventHandler
    public void setStartBalance (PlayerJoinEvent event) {

        if (event.getPlayer().hasPlayedBefore()) {
            return;
        }

        String type = plugin.getConfig().getString("Database.Type");

        if (type.equalsIgnoreCase("H2")) {
            plugin.getRDatabase().setTokens(event.getPlayer().getName(), plugin.getConfig().getDouble("Starting-Balance"));
        }

        else if (type.equalsIgnoreCase("MySQL")) {
            new MySQL().setTokens(event.getPlayer().getName(), plugin.getConfig().getDouble("Starting-Balance"));
        }

    }
}
