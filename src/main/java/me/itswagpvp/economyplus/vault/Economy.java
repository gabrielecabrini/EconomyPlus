package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.OfflinePlayer;

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
        return getBalance(_playerToString(player));
    }

    // Set the money of a player
    public void setBalance() {
        EconomyPlus.getDBType().setTokens(_playerToString(player), money);
    }

    // Add moneys to a player account
    public void addBalance() {
        super.depositPlayer(_playerToString(player), money);
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        super.withdrawPlayer(_playerToString(player), money);
    }

    // Set player's bank to the constructor value
    public void setBank() {
        EconomyPlus.getDBType().setBank(_playerToString(player), money);
    }

    // Returns the player bank
    public double getBank() {
        return EconomyPlus.getDBType().getBank(_playerToString(player));
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return has(player, money);
    }
}
