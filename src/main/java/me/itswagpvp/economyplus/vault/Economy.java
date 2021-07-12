package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.entity.Player;

public class Economy extends VEconomy {

    private Player p;
    private double money;

    // Constructor
    public Economy(Player p, double money) {
        super(EconomyPlus.getInstance());
        this.p = p;
        this.money = money;
    }

    // Returns the money of a player
    public double getBalance() {
        return plugin.getRDatabase().getTokens(this.p.getName());
    }

    // Set the money for a player
    public void setBalance() {
        plugin.getRDatabase().setTokens(this.p.getName(), this.money);
    }

    // Add moneys to a player account
    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
        double result = getBalance() + this.money;
        plugin.getRDatabase().setTokens(this.p.getName(), result);
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
        double result = getBalance() - this.money;
        plugin.getRDatabase().setTokens(this.p.getName(), result);
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
