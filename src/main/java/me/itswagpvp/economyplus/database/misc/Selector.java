package me.itswagpvp.economyplus.database.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.OfflinePlayer;

public class Selector {
    public String playerToString(OfflinePlayer player) {
        switch (EconomyPlus.getStorageMode()) {
            case NICKNAME:
                return player.getName();
            case UUID:
            default:
                return player.getUniqueId().toString();
        }
    }
}
