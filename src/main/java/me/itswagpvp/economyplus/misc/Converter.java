package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;

import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Converter {

    public int NameToUUID() {

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) return -1;

        int accounts = 0;

        plugin.setStorageMode("UUID");

        switch (EconomyPlus.getDBType()) {
            case YAML:
                for (String user : plugin.getYMLData().getConfigurationSection("Data").getKeys(false)) {

                    double money = plugin.getYMLData().getDouble("Data." + user + ".tokens");
                    double bank = plugin.getYMLData().getDouble("Data." + user + ".bank");

                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);

                    plugin.getYMLData().set("Data." + user, null);

                    plugin.getYMLData().set("Data." + p.getUniqueId() + ".tokens", money);
                    plugin.getYMLData().set("Data." + p.getUniqueId() + ".bank", bank);
                    plugin.saveYMLConfig();

                    PlayerHandler.saveName(p.getUniqueId(), user);

                    accounts++;

                }
                break;
            case MySQL:
                for (String user : new MySQL().getList()) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);
                    new MySQL().changeUser(p, "UUID");
                    PlayerHandler.saveName(p.getUniqueId(), user);
                    accounts++;
                }
                break;
            case H2:
                for (String user : new SQLite().getList()) {

                    double money = new SQLite().getTokens(user);
                    double bank = new SQLite().getBank(user);

                    OfflinePlayer p = Bukkit.getOfflinePlayer(user);
                    new SQLite().removeUser(user);
                    new SQLite().setTokens(String.valueOf(p.getUniqueId()), money);
                    new SQLite().setBank(String.valueOf(p.getUniqueId()), bank);

                    PlayerHandler.saveName(p.getUniqueId(), user);
                    accounts++;

                }
                break;


        }

        return accounts;
    }

    public int UUIDToName() {

        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) return -1;
        int accounts = 0;

        plugin.setStorageMode("NICKNAME");

        switch (EconomyPlus.getDBType()) {
            case YAML:
                for (String user : plugin.getYMLData().getConfigurationSection("Data").getKeys(false)) {

                    double money = plugin.getYMLData().getDouble("Data." + user + ".tokens");
                    double bank = plugin.getYMLData().getDouble("Data." + user + ".bank");

                    plugin.getYMLData().set("Data." + user, null);

                    String name = PlayerHandler.getName(user, true);
                    if (name.equalsIgnoreCase(user)) { // invalid user while converting
                        continue;
                    }

                    plugin.getYMLData().set("Data." + name + ".tokens", money);
                    plugin.getYMLData().set("Data." + name + ".bank", bank);
                    plugin.saveYMLConfig();

                    accounts++;
                }
                break;
            case H2:
                for (String user : new SQLite().getList()) {

                    double money = new SQLite().getTokens(user);
                    double bank = new SQLite().getBank(user);

                    String name = PlayerHandler.getName(user, true);
                    if (name.contains("-")) {
                        name = Bukkit.getOfflinePlayer(UUID.fromString(user)).getName();
                        if (name == null) {
                            break;
                        }
                    }

                    new SQLite().removeUser(user);
                    new SQLite().setTokens(name, money);
                    new SQLite().setBank(name, bank);
                    accounts++;

                }
                break;
            case MySQL:

                for (String user : new MySQL().getList()) {
                    new MySQL().changeUser(Bukkit.getOfflinePlayer(user), "NICKNAME");
                    accounts++;
                }

                break;
        }

        return accounts;
    }

}
