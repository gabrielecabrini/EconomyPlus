package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;

import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static me.itswagpvp.economyplus.EconomyPlus.*;

public class BalTopManager {

    public List<PlayerData> balTop;
    public Map<String, PlayerData> balTopName;

    public BalTopManager() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();
        loadFromDatabase();
    }

    public static int getPages() {

        double size = CacheManager.getCache(1).size();
        size = size / 10; // gets total amount of pages balance top uses

        int pages = Integer.parseInt(String.valueOf(size).split("\\.")[0]); // gets the int of size

        if (size > pages) { // if total size is greater than the amount of pages (decimal value)
            pages = pages + 1; // add one to pages
        }

        return pages;
    }

    private void loadFromDatabase() {

        getBalTop().clear();

        FileConfiguration config = new StorageManager().getStorageConfig();

        boolean useUUID = false;
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            useUUID = true;
        }

        // sort mysql db in order
        // sort db lite in order
        if (EconomyPlus.getDBType() == DatabaseType.MySQL) {

            int i = 1;
            for (Map.Entry<String, Double> user : MySQL.getOrderedList().entrySet()) {

                String name = user.getKey();
                if (useUUID) {
                    name = PlayerHandler.getName(name, false);
                    // purge check
                    if (plugin.purgeInvalid) {
                        if (name.equalsIgnoreCase("Invalid User")) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + name);
                            EconomyPlus.getDBType().removePlayer(name);
                            CacheManager.getCache(1).remove(name);
                        }
                    }
                }

                // exclude check
                if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                } else if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                }

                // add to baltop
                PlayerData pData = new PlayerData(user.getKey(), user.getValue());
                getBalTop().add(pData);
                getBalTopName().put(pData.getName(), pData);

            }

        } else if (EconomyPlus.getDBType() == DatabaseType.H2) {

            for (Map.Entry<String, Double> user : SQLite.getOrderedList().entrySet()) {

                String name = user.getKey();
                if (useUUID) {
                    name = PlayerHandler.getName(name, false);
                    // purge check
                    if (plugin.purgeInvalid) {
                        if (name.equalsIgnoreCase("Invalid User")) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + name);
                            EconomyPlus.getDBType().removePlayer(name);
                            CacheManager.getCache(1).remove(name);
                        }
                    }
                }

                // exclude check
                if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                } else if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                }

                // add to baltop
                PlayerData pData = new PlayerData(user.getKey(), user.getValue());
                getBalTop().add(pData);
                getBalTopName().put(pData.getName(), pData);

            }

        } else {

            for (Map.Entry<String, Double> value : CacheManager.getCache(1).entrySet()) { // loop through cache manager if db type is not mysql

                String name = value.getKey();

                // purge check
                if (useUUID && name.equalsIgnoreCase("Invalid User")) {
                    if (plugin.purgeInvalid) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + name);
                        EconomyPlus.getDBType().removePlayer(name);
                        CacheManager.getCache(1).remove(name);
                        continue;
                    }
                }

                // exclude check
                if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                } else if (config.getBoolean("BalTop.Exclude." + name)) {
                    continue;
                }

                // add to baltop
                PlayerData pData = new PlayerData(name, value.getValue());
                getBalTop().add(pData);
                getBalTopName().put(pData.getName(), pData);

                // sort baltop
                getBalTop().sort(new PlayerComparator());

            }

        }

    }

    public List<PlayerData> getBalTop() {
        return balTop;
    }

    public Map<String, PlayerData> getBalTopName() {
        return balTopName;
    }

    public static class PlayerComparator
            implements Comparator<PlayerData> {

        @Override
        public int compare(PlayerData arg0, PlayerData arg1) {
            int results = Double.compare(arg1.getMoney(), arg0.getMoney());
            if (results == 0) {
                results = arg0.getName().compareToIgnoreCase(arg1.getName());
            }
            return results;
        }
    }

    public static class PlayerData {

        private final String name;
        private final double money;

        public PlayerData(String name, double money) {
            super();

            this.name = name;
            this.money = money;

        }

        public String getName() {
            if (EconomyPlus.getStorageMode() != StorageMode.NICKNAME) {
                return PlayerHandler.getName(name, false);
            }
            return name;
        }

        public double getMoney() {
            return money;
        }

    }
}
