package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
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
        return hasAccount(_playerToString(player));
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(_playerToString(player));
    }

    @Override
    public double getBalance(String playerName) {
        return EconomyPlus.getDBType().getToken(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(_playerToString(player));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(_playerToString(player));
    }

    @Override
    public boolean has(String playerName, double amount) {
        double playerMoney = EconomyPlus.getDBType().getToken(playerName);
        return (playerMoney - amount) >= 0;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(_playerToString(player), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(_playerToString(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) - amount;
        EconomyPlus.getDBType().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(_playerToString(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(_playerToString(player), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) + amount;
        EconomyPlus.getDBType().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(_playerToString(player), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(_playerToString(player), amount);
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
        return EconomyPlus.getDBType().createPlayer(_playerToString(player));
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return EconomyPlus.getDBType().createPlayer(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return EconomyPlus.getDBType().createPlayer(_playerToString(player));
    }

    public String _playerToString(OfflinePlayer player) {
        switch (EconomyPlus.getStorageMode()) {
            case NICKNAME:
                return player.getName();
            case UUID:
            default:
                return player.getUniqueId().toString();
        }
    }
}
