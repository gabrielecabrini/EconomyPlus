package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Economy extends VEconomy {

    private final OfflinePlayer p;
    private final double money;

    // Constructor
    public Economy(OfflinePlayer p, double money) {
        super(EconomyPlus.plugin);
        this.p = p;
        this.money = money;
    }

    // Returns the money of a player
    public double getBalance() {
        return getBalance(this.p.getName());
    }

    // Set the money for a player
    public void setBalance() {
        EconomyPlus.getDBType().setTokens(p.getName(), money);
    }

    // Add moneys to a player account
    public void addBalance() {
        double result = getBalance() + this.money;
        EconomyPlus.getDBType().setTokens(this.p.getName(), result);
        super.depositPlayer(this.p.getName(), money);
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        double result = getBalance() - this.money;
        EconomyPlus.getDBType().setTokens(this.p.getName(), result);
        super.withdrawPlayer(this.p.getName(), this.money);
    }

    public void setBank () {
        EconomyPlus.getDBType().setBank(this.p.getName(), money);
    }

    public double getBank () {
        return EconomyPlus.getDBType().getBank(this.p.getName());
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
