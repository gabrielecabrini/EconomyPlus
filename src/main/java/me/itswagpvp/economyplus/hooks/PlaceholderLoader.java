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
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §cNot found!");
            return;
        }

        try {
            new PlaceholderAPI(plugin).register();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §cError!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §aDone!");
        }
    }
}
