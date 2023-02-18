package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.StorageMode;

import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static me.itswagpvp.economyplus.EconomyPlus.balTopManager;
import static me.itswagpvp.economyplus.EconomyPlus.plugin;

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

        CacheManager.getCache(1).forEach((player, money) -> {

            //if (config.getBoolean("BalTop.Exclude." + player)) {
            //    continue;
            //}

            PlayerData pData = new PlayerData(player, money);
            getBalTop().add(pData);
            getBalTopName().put(pData.getName(), pData);

        });

        /*

        for (String playerName : EconomyPlus.getDBType().getList()) {

            if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
                String convertedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerName)).getName();
                if (new StorageManager().getStorageConfig().getBoolean("BalTop.Exclude." + convertedPlayer)) {
                    continue;
                }
            } else {
                if (new StorageManager().getStorageConfig().getBoolean("BalTop.Exclude." + playerName)) {
                    continue;
                }
            }

            double money;
            if (CacheManager.getCache(1).get(playerName) == null) {
                money = 0;
            } else {
                money = CacheManager.getCache(1).get(playerName);
            }

            PlayerData pData = new PlayerData(playerName, money);
            getBalTop().add(pData);
            getBalTopName().put(pData.getName(), pData);

        }

         */

        getBalTop().sort(new PlayerComparator());

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
