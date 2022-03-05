package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.Collections;
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
        return hasAccount(Selector.playerToString(player));
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(Selector.playerToString(player));
    }

    @Override
    public double getBalance(String playerName) {
        if (CacheManager.getCache(1).get(playerName) == null) {
            return 0D;
        }
        return CacheManager.getCache(1).get(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(Selector.playerToString(player));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(Selector.playerToString(player));
    }

    @Override
    public boolean has(String playerName, double amount) {
        double playerMoney = EconomyPlus.getDBType().getToken(playerName);
        return (playerMoney - amount) >= 0;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(Selector.playerToString(player), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(Selector.playerToString(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double tokens = 0D;
        try {
            tokens = getBalance(playerName) - amount;
            if (tokens >= 0) {
                CacheManager.getCache(1).put(playerName, tokens);
                EconomyPlus.getDBType().setTokens(playerName, tokens);
            } else {
                return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.FAILURE, "Not enough moneys!");
            }
        } catch (Exception e) {
            return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.FAILURE, "Error while removing moneys from the player " + playerName);
        }

        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(Selector.playerToString(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(Selector.playerToString(player), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double tokens = 0D;
        try {
            tokens = getBalance(playerName) + amount;
            CacheManager.getCache(1).put(playerName, tokens);
            EconomyPlus.getDBType().setTokens(playerName, tokens);
        } catch (Exception e) {
            return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.FAILURE, "Can't add moneys to the player " + playerName);
        }

        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Action done");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(Selector.playerToString(player), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(Selector.playerToString(player), amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#createBank() is not implemented");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#createBank() is not implemented");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#deleteBank() is not implemented");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#bankBalance() is not implemented");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#bankHas() is not implemented");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#bankWithdraw() is not implemented");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#bankDeposit() is not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#isBankOwner() is not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#isBankOwner() is not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#isBankMember() is not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy#isBankMember() is not implemented");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return EconomyPlus.getDBType().createPlayer(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return EconomyPlus.getDBType().createPlayer(Selector.playerToString(player));
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return EconomyPlus.getDBType().createPlayer(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return EconomyPlus.getDBType().createPlayer(Selector.playerToString(player));
    }
}