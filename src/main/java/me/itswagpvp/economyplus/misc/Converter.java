package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Converter {

    //Select which converter the plugin needs to use
    public int convert() {
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) return UUIDToName();
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) return NameToUUID();
        return 0;
    }

    private int NameToUUID() {
        // YAML
        AtomicInteger updatedAccounts = new AtomicInteger();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.setStorageMode("UUID");

            if (EconomyPlus.getDBType() == DatabaseType.YAML) {
                for (String user : plugin.getYMLData().getConfigurationSection("Data").getKeys(false)) {
                    double money = plugin.getYMLData().getDouble("Data." + user + ".tokens");
                    double bank = plugin.getYMLData().getDouble("Data." + user + ".bank");

                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);

                    plugin.getYMLData().set("Data." + user, null);

                    plugin.getYMLData().set("Data." + p.getUniqueId() + ".tokens", money);
                    plugin.getYMLData().set("Data." + p.getUniqueId() + ".bank", bank);
                    plugin.saveYMLConfig();
                    updatedAccounts.getAndIncrement();
                }

            }

            // SQLite
            if (EconomyPlus.getDBType() == DatabaseType.H2) {
                for (String user : new SQLite().getList()) {
                    double money = new SQLite().getTokens(user);
                    double bank = new SQLite().getBank(user);

                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);
                    new SQLite().removeUser(user);
                    new SQLite().setTokens(String.valueOf(p.getUniqueId()), money);
                    new SQLite().setBank(String.valueOf(p.getUniqueId()), bank);
                    updatedAccounts.getAndIncrement();
                }
            }

            // MySQL
            if (EconomyPlus.getDBType() == DatabaseType.MySQL) {
                for (String user : new MySQL().getList()) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);
                    new MySQL().changeUser(p, "UUID");
                    updatedAccounts.getAndIncrement();
                }
            }
        });

        return updatedAccounts.get();
    }

    private int UUIDToName() {

        AtomicInteger updatedAccounts = new AtomicInteger();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.setStorageMode("NICKNAME");
            // YAML
            if (EconomyPlus.getDBType() == DatabaseType.YAML) {
                for (String user : plugin.getYMLData().getConfigurationSection("Data").getKeys(false)) {
                    double money = plugin.getYMLData().getDouble("Data." + user + ".tokens");
                    double bank = plugin.getYMLData().getDouble("Data." + user + ".bank");

                    plugin.getYMLData().set("Data." + user, null);

                    OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(user));
                    plugin.getYMLData().set("Data." + p.getName() + ".tokens", money);
                    plugin.getYMLData().set("Data." + p.getName() + ".bank", bank);
                    plugin.saveYMLConfig();
                    updatedAccounts.getAndIncrement();
                }

            }

            // SQLite
            if (EconomyPlus.getDBType() == DatabaseType.H2) {
                for (String user : new SQLite().getList()) {
                    double money = new SQLite().getTokens(user);
                    double bank = new SQLite().getBank(user);

                    OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(user));
                    new SQLite().removeUser(user);
                    new SQLite().setTokens(String.valueOf(p.getName()), money);
                    new SQLite().setBank(String.valueOf(p.getName()), bank);
                    updatedAccounts.getAndIncrement();
                }
            }

            // MySQL
            if (EconomyPlus.getDBType() == DatabaseType.MySQL) {
                for (String user : new MySQL().getList()) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(user));
                    new MySQL().changeUser(p, "NICKNAME");
                    updatedAccounts.getAndIncrement();
                }
            }
        });

        return updatedAccounts.get();
    }

}
