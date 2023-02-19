package me.itswagpvp.economyplus.database;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;

import java.util.HashMap;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;
import static me.itswagpvp.economyplus.EconomyPlus.purgeInvalid;

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

    // Cache database
    public void cacheDatabase() {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            int num = 0;
            int purged = 0;

            for (String player : EconomyPlus.getDBType().getList()) {

                try {
                    if (purgeInvalid) {
                        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
                            if (PlayerHandler.getName(player, true).equalsIgnoreCase(player)) {
                                EconomyPlus.getDBType().removePlayer(player);
                                purged++;
                            }
                        }
                    }
                    cachedPlayersMoneys.put(player, EconomyPlus.getDBType().getToken(player));
                    if (EconomyPlus.bankEnabled) {
                        cachedPlayersBanks.put(player, EconomyPlus.getDBType().getBank(player));
                    }
                    num++;
                } catch (Exception e) {
                    EconomyPlus.getDBType().removePlayer(player);
                    if (EconomyPlus.getDBType() == DatabaseType.MySQL) {
                        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Encountered an error while refreshing MySQL: " + e.getMessage());
                    } else {
                        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Encountered an error while refreshing local database: " + e.getMessage());
                    }
                }

            }

            if (EconomyPlus.debugMode) {
                Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Finished the cache thread for " + num + " accounts...");
                if (purged != 0) {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Purged " + purged + " invalid accounts!");
                }
            }

        });

    }

    // Started only with H2 and YAML
    public void startAutoSave() {

        long refreshRate = plugin.getConfig().getLong("Database.Cache.Auto-Save", 300) * 20L;

        if (EconomyPlus.getDBType() != DatabaseType.MySQL) {

            Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {

                if (EconomyPlus.debugMode) {
                    if (EconomyPlus.purgeInvalid) {
                        Bukkit.getConsoleSender().sendMessage("[EconomyPlus-Debug] Caching and removing invalid accounts...");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("[EconomyPlus-Debug] Caching accounts...");
                    }
                }

                cacheDatabase();

            }, 300L, refreshRate);

        }
    }

}