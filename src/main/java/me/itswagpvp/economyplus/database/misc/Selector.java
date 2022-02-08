package me.itswagpvp.economyplus.database.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Selector {
    public static String playerToString(OfflinePlayer player) {
        switch (EconomyPlus.getStorageMode()) {
            case NICKNAME:
                return player.getName();
            case UUID:
            default:
                return player.getUniqueId().toString();
        }
    }

    /**
     * @param player name or uuid
     * @return the player class
     * @since 3.6
     **/
    public static Player stringToPlayer(String player) {
        Player p;
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            p = Bukkit.getPlayer(UUID.fromString(player));
        } else {
            p = Bukkit.getPlayer(player);
        }

        return p;
    }

    @Deprecated
    public static OfflinePlayer stringToOfflinePlayer(String player) {
        OfflinePlayer p;
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            p = Bukkit.getOfflinePlayer(UUID.fromString(player));
        } else {
            p = Bukkit.getOfflinePlayer(player);
        }

        return p;
    }
}
