package me.itswagpvp.economyplus.bank.other;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class InterestsManager {
    public void startBankInterests() {
        long time = plugin.getConfig().getLong("Bank.Interests.Time", 300) * 20L;
        int interest = plugin.getConfig().getInt("Bank.Interests.Percentage", 10);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (String player : EconomyPlus.getDBType().getList()) {

                Player p;
                if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
                    p = Bukkit.getPlayer(UUID.fromString(player));
                } else {
                    p = Bukkit.getPlayer(player);
                }

                if (plugin.getConfig().getBoolean("Bank.Interests.Online-Player", false)) {
                    if (p == null) {
                        continue;
                    }
                }

                double bankValue = CacheManager.getCache(2).get(player);
                bankValue = (bankValue * (100 + interest) / 100);

                // Save the new bank in the cache and then in the db
                CacheManager.getCache(2).put(player, bankValue);
                EconomyPlus.getDBType().setBank(player, bankValue);

                if (p != null) {
                    p.sendMessage(plugin.getMessage("Bank.Interests").replaceAll("%percentage%", "" + interest));
                    Utils.playSuccessSound(p);
                }

            }
        }, time, time);
    }
}
