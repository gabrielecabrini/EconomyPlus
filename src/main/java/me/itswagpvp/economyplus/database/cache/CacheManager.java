package me.itswagpvp.economyplus.database.cache;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class CacheManager {

    private static ConcurrentHashMap<String, Double> cachedPlayersMoneys = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Double> cachedPlayersBanks = new ConcurrentHashMap<>();

    /**
     * @return ConcurrentHashMap for moneys and banks
     *
     * @param selector (1 = moneys, 2 = bank)
     **/
    public static ConcurrentHashMap<String, Double> getCache(int selector) {
        if (selector == 1) {
            return cachedPlayersMoneys;
        } else if (selector == 2) {
            return cachedPlayersBanks;
        }

        return new ConcurrentHashMap<>();
    }

    public int cacheDatabase() {
        AtomicInteger i = new AtomicInteger();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                i.getAndIncrement();
            }

            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
            }
        }, 0, 40);

        return i.get();
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
