package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.StorageMode;

import me.itswagpvp.economyplus.listener.PlayerHandler;
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

        Bukkit.broadcastMessage("called baltop");

        getBalTop().clear();

        FileConfiguration config = new StorageManager().getStorageConfig();

        boolean useUUID = false;
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            useUUID = true;
        }

        for (Map.Entry<String, Double> value: CacheManager.getCache(1).entrySet()) {

            String name = value.getKey();
            Bukkit.broadcastMessage("Value: " + name);

            // purge check
            if (useUUID && name.equalsIgnoreCase("Invalid User")) {
                if (plugin.purgeInvalid) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + value.getKey());
                    EconomyPlus.getDBType().removePlayer(value.getKey());
                    CacheManager.getCache(1).remove(value.getKey());
                    continue;
                }
            }

            // exclude check
            if (config.getBoolean("BalTop.Exclude." + value.getKey())) {
                continue;
            } else if (config.getBoolean("BalTop.Exclude." + name)) {
                continue;
            }

            // add to baltop sorting
            PlayerData pData = new PlayerData(name, value.getValue());
            getBalTop().add(pData);
            getBalTopName().put(pData.getName(), pData);

        }

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
