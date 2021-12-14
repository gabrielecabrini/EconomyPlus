package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VEconomy implements Economy {

    public EconomyPlus plugin;

    public VEconomy (EconomyPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Dollar";
    }

    @Override
    public String currencyNameSingular() {
        return "Dollars";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return EconomyPlus.getDBType().contains(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return hasAccount(player.getName());
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return hasAccount(String.valueOf(player.getUniqueId()));
        }

        return false;

    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return hasAccount(player.getName());
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return hasAccount(String.valueOf(player.getUniqueId()));
        }

        return false;
    }

    @Override
    public double getBalance(String playerName) {
        return EconomyPlus.getDBType().getToken(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return getBalance(player.getName());
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return getBalance(String.valueOf(player.getUniqueId()));
        }

        return 0;
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return getBalance(player.getName());
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return getBalance(String.valueOf(player.getUniqueId()));
        }

        return 0;
    }

    @Override
    public boolean has(String playerName, double amount) {
        double playerMoney = EconomyPlus.getDBType().getToken(playerName);
        return (playerMoney - amount) >= 0;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return has(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return has(String.valueOf(player.getUniqueId()), amount);
        }

        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return has(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return has(String.valueOf(player.getUniqueId()), amount);
        }

        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) - amount;
        EconomyPlus.getDBType().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return withdrawPlayer(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return withdrawPlayer(String.valueOf(player.getUniqueId()), amount);
        }

        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return withdrawPlayer(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return withdrawPlayer(String.valueOf(player.getUniqueId()), amount);
        }

        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) + amount;
        EconomyPlus.getDBType().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens,EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return depositPlayer(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return depositPlayer(String.valueOf(player.getUniqueId()), amount);
        }

        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return depositPlayer(player.getName(), amount);
        }

        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            return depositPlayer(String.valueOf(player.getUniqueId()), amount);
        }

        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return EconomyPlus.getDBType().createPlayer(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return EconomyPlus.getDBType().createPlayer(player.getName());
        } else {
            return EconomyPlus.getDBType().createPlayer(player.getUniqueId().toString());
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return EconomyPlus.getDBType().createPlayer(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            return EconomyPlus.getDBType().createPlayer(player.getName());
        } else {
            return EconomyPlus.getDBType().createPlayer(player.getUniqueId().toString());
        }
    }
}
