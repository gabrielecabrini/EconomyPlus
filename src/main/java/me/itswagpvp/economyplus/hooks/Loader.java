package me.itswagpvp.economyplus.hooks;

import org.bukkit.Bukkit;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;
import static org.bukkit.Bukkit.getServer;

public class Loader {

    public void loadHolograms() {

    }

    public static String placeholder = "   - §fPlaceholderAPI: §cCan't find the jar!";
    public void loadPlaceholderAPI() {

        if (!plugin.getConfig().getBoolean("Hooks.PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("boolean toggled");
            return;
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }

        try {
            new PlaceholderAPI(plugin).register();
        } catch (Exception e) {
            placeholder = "   - §fPlaceholderAPI: §cError!";
            Bukkit.getConsoleSender().sendMessage("§c[EconomyPlus] Error hooking into PlaceholderAPI:");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        } finally {
            placeholder = "   - §fPlaceholderAPI: §aHooked!";
        }

    }

    public boolean getPlaceholderAPI() {
        return new PlaceholderAPI(plugin).isRegistered();
    }

}
