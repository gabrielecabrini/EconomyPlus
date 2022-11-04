package me.itswagpvp.economyplus.hooks;

import org.bukkit.Bukkit;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class PlaceholderLoader {

    // Load PlaceholderAPI
    public void loadPlaceholderAPI() {

        if (!plugin.getConfig().getBoolean("Hooks.PlaceholderAPI")) {
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("   - §fPlaceholderAPI: §cCan't find the jar!");
            return;
        }

        try {
            new PlaceholderAPI(plugin).canRegister();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fPlaceholderAPI: §cError!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        } finally {
            Bukkit.getConsoleSender().sendMessage("   - §fPlaceholderAPI: §aDone!");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    new PlaceholderAPI(plugin).register();
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(e.getMessage());
                }
            }
        }, 1L);


    }
}
