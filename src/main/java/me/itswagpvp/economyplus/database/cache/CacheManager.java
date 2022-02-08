package me.itswagpvp.economyplus.database.cache;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class CacheManager {

    private static final ConcurrentHashMap<String, Double> cachedPlayersMoneys = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Double> cachedPlayersBanks = new ConcurrentHashMap<>();

    /**
     * @param selector (1 = moneys, 2 = bank)
     * @return HashMap for moneys and banks
     **/
    public static ConcurrentHashMap<String, Double> getCache(int selector) {
        if (selector == 1) {
            return cachedPlayersMoneys;
        } else if (selector == 2) {
            return cachedPlayersBanks;
        } else {
            throw new IllegalArgumentException("Invalid cache selector (1 = moneys, 2 = bank)");
        }
    }

    public int cacheLocalDatabase() {
        AtomicInteger i = new AtomicInteger();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                i.getAndIncrement();
            }

            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
            }
        });
        return i.get();
    }

    public int cacheOnlineDatabase() {
        AtomicInteger i = new AtomicInteger();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                i.getAndIncrement();
            }

            for (String player : EconomyPlus.getDBType().getList()) {
                cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
            }
        });
        return i.get();
    }

    public void startAutoSave() {
        long refreshRate = plugin.getConfig().getLong("Database.Cache.Auto-Save", 300) * 20L;

        if (EconomyPlus.getDBType() != DatabaseType.MySQL) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                int savedAccounts = cacheLocalDatabase();
                if (EconomyPlus.debugMode) Bukkit.getConsoleSender().sendMessage(
                        "[EconomyPlus-Debug] Cached §6%accounts% §7accounts..."
                                .replace("%accounts%", "" + savedAccounts));
            }, 0L, refreshRate);
        }
    }

}
