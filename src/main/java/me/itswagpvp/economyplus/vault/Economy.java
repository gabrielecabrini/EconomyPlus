package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.entity.Player;

public class Economy extends VEconomy {

    private final Player p;
    private final double money;

    // Constructor
    public Economy(Player p, double money) {
        super(EconomyPlus.getInstance());
        this.p = p;
        this.money = money;
    }

    // Returns the money of a player
    public double getBalance() {
        return getBalance(this.p.getName());
    }

    // Set the money for a player
    public void setBalance() {
        dbType.setTokens(p.getName(), money);
    }

    // Add moneys to a player account
    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
        double result = getBalance() + this.money;
        dbType.setTokens(this.p.getName(), result);
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
        double result = getBalance() - this.money;
        dbType.setTokens(this.p.getName(), result);
    }

    public void setBank () {
        dbType.setBank(this.p.getName(), money);
    }

    public double getBank () {
        return dbType.getBank(this.p.getName());
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
