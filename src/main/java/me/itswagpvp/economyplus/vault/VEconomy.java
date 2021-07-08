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
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
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
        return "$";
    }

    @Override
    public String currencyNameSingular() {
        return "$";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return EconomyPlus.getInstance().getRDatabase().getList().contains(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player.getName());
    }

    @Override
    public double getBalance(String playerName) {
        return EconomyPlus.getInstance().getRDatabase().getTokens(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player.getName());
    }

    @Override
    public boolean has(String playerName, double amount) {

        double playerMoney = EconomyPlus.getInstance().getRDatabase().getTokens(playerName);
        double detracting = amount;

        return (playerMoney - detracting) >= 0;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) - amount;

        EconomyPlus.getInstance().getRDatabase().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        double tokens = getBalance(player) - amount;

        EconomyPlus.getInstance().getRDatabase().setTokens(player.getName(), tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {

        double tokens = getBalance(playerName) - amount;

        EconomyPlus.getInstance().getRDatabase().setTokens(playerName, tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        double tokens = getBalance(player) - amount;

        EconomyPlus.getInstance().getRDatabase().setTokens(player.getName(), tokens);
        return new EconomyResponse(amount, tokens, EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double tokens = getBalance(playerName) + amount;
        EconomyPlus.getInstance().getRDatabase().setTokens(playerName, tokens);

        return new EconomyResponse(amount, tokens,EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double tokens = getBalance(player) + amount;
        EconomyPlus.getInstance().getRDatabase().setTokens(player.getName(), tokens);

        return new EconomyResponse(amount, tokens,EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {

        double tokens = getBalance(playerName) + amount;
        EconomyPlus.getInstance().getRDatabase().setTokens(playerName, tokens);

        return new EconomyResponse(amount, tokens,EconomyResponse.ResponseType.SUCCESS, "Done");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {

        double tokens = getBalance(player) + amount;
        EconomyPlus.getInstance().getRDatabase().setTokens(player.getName(), tokens);

        return new EconomyResponse(amount, tokens,EconomyResponse.ResponseType.SUCCESS, "Done");
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
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
