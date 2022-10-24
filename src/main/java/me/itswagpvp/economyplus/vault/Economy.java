package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import me.itswagpvp.economyplus.hooks.events.PlayerBalanceChangeEvent;
import me.itswagpvp.economyplus.hooks.events.PlayerBankChangeEvent;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import static me.itswagpvp.economyplus.EconomyPlus.ymlConfig;

public class Economy extends VEconomy {

    private final OfflinePlayer player;

    public Economy(OfflinePlayer player) {
        super(EconomyPlus.plugin);
        this.player = player;
    }

    // Returns the money of a player
    public double getBalance() {
        return round(super.getBalance(Selector.playerToString(player)));
    }

    // Set the money of a player
    public void setBalance(double money) {

        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), round(money));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        CacheManager.getCache(1).put(Selector.playerToString(player), round(money));
        EconomyPlus.getDBType().setTokens(Selector.playerToString(player), round(money));
    }

    // Add moneys to a player account
    public void addBalance(double money) {

        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), getBalance() + round(money));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        super.depositPlayer(Selector.playerToString(player), round(money));
    }

    // Remove moneys from a player's account
    public void takeBalance(double money) {

        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), getBalance() - round(money));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        super.withdrawPlayer(Selector.playerToString(player), round(money));
    }

    // Set player's bank to the constructor value
    public void setBank(double money) {

        PlayerBankChangeEvent event = new PlayerBankChangeEvent(Selector.playerToString(player), round(money));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        CacheManager.getCache(2).put(Selector.playerToString(player), round(money));
        EconomyPlus.getDBType().setBank(Selector.playerToString(player), round(money));
    }

    // Returns the player bank
    public double getBank() {
        if (CacheManager.getCache(2).get(Selector.playerToString(player)) == null) {
            return 0D;
        }
        return round(CacheManager.getCache(2).get(Selector.playerToString(player)));
    }

    // Controls if the player has enough moneys
    public boolean detractable(double money) {
        return has(player, money);
    }

    //method that rounds to 2 dec places
    public static Double round(double val) { //known issue with trying to get data config

        //if(ymlConfig.get("Round") == null || !ymlConfig.getBoolean("Round")) {
            //return val;
        //}

        //int round = 2;
        //if(!(ymlConfig.get("Rounding-Value") == null)) {
            //round = ymlConfig.getInt("Rounding-Value");
        //}

        return Double.parseDouble(String.format("%." + 2 + "f", val)); //replace 2 with round when fixed
    }
}