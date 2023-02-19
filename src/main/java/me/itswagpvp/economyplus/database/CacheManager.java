package me.itswagpvp.economyplus.database;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class CacheManager {

    private static final HashMap<String, Double> cachedPlayersMoneys = new HashMap<>();
    private static final HashMap<String, Double> cachedPlayersBanks = new HashMap<>();
    public static HashMap<String, String> usernames = new HashMap<>();

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

        int num = 0;
        int purged = 0;

        for (String uuid : EconomyPlus.getDBType().getList()) {

            try {
                String name;
                if (EconomyPlus.getStorageMode() == StorageMode.UUID) {

                    name = PlayerHandler.getName(uuid, true);
                    if (plugin.purgeInvalid) {
                        if (name.equalsIgnoreCase(uuid)) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + name);
                            EconomyPlus.getDBType().removePlayer(uuid);
                            CacheManager.getCache(1).remove(uuid);
                            purged++;
                            continue;
                        }
                    }

                    // add username
                    if (!(usernames.containsKey(name))) {
                        usernames.put(name, uuid);
                    }

                } else { // nickname mode
                    name = uuid;
                    if (plugin.purgeInvalid) {
                        if (uuid.length() >= 36) { // is a uuid
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + name);
                            EconomyPlus.getDBType().removePlayer(name);
                            CacheManager.getCache(1).remove(name);
                            purged++;
                            continue;
                        }
                    }

                    // add username
                    if (!(usernames.containsKey(name))) {
                        usernames.put(name, "");
                    }

                }

                cachedPlayersMoneys.put(uuid, EconomyPlus.getDBType().getToken(uuid));
                if (EconomyPlus.bankEnabled) {
                    cachedPlayersBanks.put(uuid, EconomyPlus.getDBType().getBank(uuid));
                }

                num++;

            } catch (Exception e) {
                EconomyPlus.getDBType().removePlayer(uuid);
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

    }

    // Started only with H2 and YAML
    //
    public void startAutoSave() {

        long refreshRate = plugin.getConfig().getLong("Database.Cache.Auto-Save", 300) * 20L;
        if (EconomyPlus.getDBType() == DatabaseType.MySQL) {
            refreshRate = plugin.getConfig().getLong("Database.Cache.MySQL", 10) * 20;
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {

            if (EconomyPlus.debugMode) {
                if (plugin.purgeInvalid) {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus-Debug] Caching and removing invalid accounts...");
                } else {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus-Debug] Caching accounts...");
                }
            }

            cacheDatabase();

        }, refreshRate, refreshRate);

    }

}