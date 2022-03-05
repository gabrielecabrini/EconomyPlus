package me.itswagpvp.economyplus.database.cache;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class CacheManager {

    private static final HashMap<String, Double> cachedPlayersMoneys = new HashMap<>();
    private static final HashMap<String, Double> cachedPlayersBanks = new HashMap<>();

    /**
     * @param selector (1 = moneys, 2 = bank)
     * @return HashMap for moneys and banks
     * @throws IllegalArgumentException when the selector is different from 1 or 2
     **/
    public static HashMap<String, Double> getCache(int selector) throws IllegalArgumentException {
        if (selector == 1) {
            return cachedPlayersMoneys;
        } else if (selector == 2) {
            return cachedPlayersBanks;
        } else {
            throw new IllegalArgumentException("Invalid cache selector (1 = moneys, 2 = bank)");
        }
    }

    // Stores the db (YAML, H2) into the cache
    public void cacheLocalDatabase() {
        AtomicInteger num = new AtomicInteger();
        AtomicBoolean finished = new AtomicBoolean(false);
        Thread t = new Thread(() -> {
            Thread.currentThread().setName("cacheLocalDatabase-economyplus");
            for (String player : EconomyPlus.getDBType().getList()) {
                try {
                    num.getAndIncrement();
                    cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                    cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
                } catch (Exception e) {
                    EconomyPlus.getDBType().removePlayer(player);
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Encountered an error while refreshing local database: " + e.getMessage());
                }
            }

            finished.set(true);

            if (EconomyPlus.debugMode) {
                Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Finished the cache thread for " + num.get() + " accounts...");
            }
        });

        t.start();

        while (finished.get()) {
            t.stop();
            return;
        }

    }

    // Stores the db (MySQL) into the cache
    public void cacheOnlineDatabase() {
        AtomicInteger num = new AtomicInteger();
        AtomicBoolean finished = new AtomicBoolean(false);
        Thread t = new Thread(() -> {
            Thread.currentThread().setName("cacheOnlineDatabase-economyplus");
                for (String player : EconomyPlus.getDBType().getList()) {
                    try {
                        num.getAndIncrement();
                        cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                        cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
                    } catch (Exception e) {
                        EconomyPlus.getDBType().removePlayer(player);
                        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Encountered an error while refreshing MySQL: " + e.getMessage());
                    }
                }

                finished.set(true);

                if (EconomyPlus.debugMode) {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Finished the cache thread for " + num.get() + " accounts...");
                }

        });
        t.start();

        while (finished.get()) {
            t.stop();
            return;
        }
    }

    // Started only with H2 and YAML
    public void startAutoSave() {
        long refreshRate = plugin.getConfig().getLong("Database.Cache.Auto-Save", 300) * 20L;

        if (EconomyPlus.getDBType() != DatabaseType.MySQL) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (EconomyPlus.debugMode) Bukkit.getConsoleSender().sendMessage(
                        "[EconomyPlus-Debug] Caching accounts...");
                cacheLocalDatabase();
            }, 300L, refreshRate);
        }
    }

}
