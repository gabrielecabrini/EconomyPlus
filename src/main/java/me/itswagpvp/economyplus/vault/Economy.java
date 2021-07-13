package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.storage.mysql.MySQL;
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

    final String type = plugin.getConfig().getString("Database.Type");

    // Returns the money of a player
    public double getBalance() {
        if (type.equalsIgnoreCase("H2")) {
            return plugin.getRDatabase().getTokens(this.p.getName());
        }

        if (type.equalsIgnoreCase("MySQL")) {
            return new MySQL().getTokens(this.p.getName());
        }

        return 0D;
    }

    // Set the money for a player
    public void setBalance() {

        if (type.equalsIgnoreCase("H2")) {
            plugin.getRDatabase().setTokens(this.p.getName(), this.money);
        }

        if (type.equalsIgnoreCase("MySQL")) {
            new MySQL().setTokens(this.p.getName(), this.money);
        }
    }

    // Add moneys to a player account
    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
        double result = getBalance() + this.money;

        if (type.equalsIgnoreCase("H2")) {
            plugin.getRDatabase().setTokens(this.p.getName(), result);
        }

        if (type.equalsIgnoreCase("MySQL")) {
            new MySQL().setTokens(this.p.getName(), result);
        }
    }

    // Remove moneys from a player's account
    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
        double result = getBalance() - this.money;

        if (type.equalsIgnoreCase("H2")) {
            plugin.getRDatabase().setTokens(this.p.getName(), result);
        }

        if (type.equalsIgnoreCase("MySQL")) {
            new MySQL().setTokens(this.p.getName(), result);
        }
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
