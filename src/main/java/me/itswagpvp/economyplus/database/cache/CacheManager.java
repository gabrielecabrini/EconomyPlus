package me.itswagpvp.economyplus.database.cache;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

import java.util.HashMap;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class CacheManager {

    public static HashMap<String, Double> cachedPlayersMoneys = new HashMap<>();
    public static HashMap<String, Double> cachedPlayersBanks = new HashMap<>();

    public int cacheDatabase() {
        int i = 0;
        for (String player : EconomyPlus.getDBType().getList()) {
            cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
            i++;
        }

        for (String player : EconomyPlus.getDBType().getList()) {
            cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
        }

        return i;
    }

    public void startAutoSave() {
        long refreshRate = plugin.getConfig().getLong("Database.Cache.Auto-Save", 300) * 20L;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            int savedAccounts = cacheDatabase();
            if (EconomyPlus.debugMode) Bukkit.getConsoleSender().sendMessage(
                    "[EconomyPlus-Debug] Cached §6%accounts% §7accounts..."
                            .replace("%accounts%", "" + savedAccounts));

        }, 0L, refreshRate);
    }

}
