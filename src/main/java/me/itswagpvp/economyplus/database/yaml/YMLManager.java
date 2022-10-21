package me.itswagpvp.economyplus.database.yaml;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class YMLManager {

    public boolean contains(String playerName) {
        return plugin.getYMLData().contains("Data." + playerName);
    }

    public double getTokens(String name) {
        return plugin.getYMLData().getDouble("Data." + name + ".tokens", 0D);
    }

    public void setTokens(String name, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getYMLData().set("Data." + name + ".tokens", value);
            plugin.saveYMLConfig();
        });
    }

    public double getBank(String name) {
        return plugin.getYMLData().getDouble("Data." + name + ".bank");
    }

    public void setBank(String name, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getYMLData().set("Data." + name + ".bank", value);
            plugin.saveYMLConfig();
        });
    }

    public boolean createPlayer(String player) {
        setTokens(player, plugin.getConfig().getDouble("Starting-Balance"));
        setBank(player, plugin.getConfig().getDouble("Starting-Bank-Balance"));
        return true;
    }

    public void removePlayer(String player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getYMLData().set("Data." + player, null);
            plugin.saveYMLConfig();
        });
    }

    public List<String> getList() {
        return new ArrayList<>(plugin.getYMLData().getConfigurationSection("Data").getKeys(false));
    }

}