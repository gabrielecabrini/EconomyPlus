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

    // Transforms a Double to a Long
    private String toLong(double amt) {
        return String.valueOf((long) amt);
    }

    // Returns the money of a player
    public long getBalance() {
        return plugin.getRDatabase().getTokens(this.p.getName());
    }

    // Set the money for a player
    public void setBalance() {
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(this.money)));
    }

    // Add moneys to a player account
    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
        long x = getBalance();
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(this.money + x)));
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
        long x = getBalance();
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(Math.max(x - this.money, 0))));
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
