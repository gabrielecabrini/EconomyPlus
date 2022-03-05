package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import org.bukkit.Bukkit;

import java.util.*;

public class BalTopManager {

    public List<PlayerData> balTop;
    public Map<String,PlayerData> balTopName;

    public BalTopManager() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        getBalTop().clear();

        for ( String playerName : EconomyPlus.getDBType().getList()) {

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
            getBalTop().add( pData );
            getBalTopName().put( pData.getName(), pData );

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
        public int compare( PlayerData arg0, PlayerData arg1 )
        {
            int results = Double.compare( arg1.getMoney(), arg0.getMoney() );
            if ( results == 0 ) {
                results = arg0.getName().compareToIgnoreCase( arg1.getName() );
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
            if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
                return name;
            } else if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
                if (Bukkit.getServer().getOfflinePlayer(UUID.fromString(name)).getName() == null) return "Invalid User";
                return Bukkit.getServer().getOfflinePlayer(UUID.fromString(name)).getName();
            }
            return "Invalid user";
        }

        public double getMoney() {
            return money;
        }

    }
}
