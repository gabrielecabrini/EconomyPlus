package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import me.itswagpvp.economyplus.hooks.events.PlayerBalanceChangeEvent;
import me.itswagpvp.economyplus.hooks.events.PlayerBankChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

public class Economy extends VEconomy {

    private final OfflinePlayer player;
    private final double money;

    public Economy(OfflinePlayer player, double money) {
        super(EconomyPlus.plugin);
        this.player = player;
        this.money = money;
    }

    // Returns the money of a player
    public double getBalance() {
        return super.getBalance(Selector.playerToString(player));
    }

    // Set the money of a player
    public void setBalance() {

        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), money);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        CacheManager.getCache(1).put(Selector.playerToString(player), money);
        EconomyPlus.getDBType().setTokens(Selector.playerToString(player), money);
    }

    // Add moneys to a player account
    public void addBalance() {
        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), getBalance() + money);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        super.depositPlayer(Selector.playerToString(player), money);
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        PlayerBalanceChangeEvent event = new PlayerBalanceChangeEvent(Selector.playerToString(player), getBalance() - money);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        super.withdrawPlayer(Selector.playerToString(player), money);
    }

    // Set player's bank to the constructor value
    public void setBank() {
        PlayerBankChangeEvent event = new PlayerBankChangeEvent(Selector.playerToString(player), money);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        CacheManager.getCache(2).put(Selector.playerToString(player), money);
        EconomyPlus.getDBType().setBank(Selector.playerToString(player), money);
    }

    // Returns the player bank
    public double getBank() {
        if (CacheManager.getCache(2).get(Selector.playerToString(player)) == null) {
            return 0D;
        }
        return CacheManager.getCache(2).get(Selector.playerToString(player));
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return has(player, money);
    }
}