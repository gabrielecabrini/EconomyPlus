package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.storage.StorageMode;
import org.bukkit.OfflinePlayer;

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
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) return getBalance(String.valueOf(this.p.getUniqueId()));
        else if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) return getBalance(p.getName());
        return 0;
    }

    // Set the money for a player
    public void setBalance() {
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) EconomyPlus.getDBType().setTokens(String.valueOf(p.getUniqueId()), money);
        else if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) EconomyPlus.getDBType().setTokens(p.getName(), money);
    }

    // Add moneys to a player account
    public void addBalance() {
        double result = getBalance() + this.money;
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            EconomyPlus.getDBType().setTokens(this.p.getName(), result);
            super.depositPlayer(this.p.getName(), money);
        } else if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            EconomyPlus.getDBType().setTokens(String.valueOf(this.p.getUniqueId()), result);
            super.depositPlayer(String.valueOf(this.p.getUniqueId()), money);
        }

    }

    // Remove moneys from a player's account
    public void takeBalance() {
        double result = getBalance() - this.money;
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) {
            EconomyPlus.getDBType().setTokens(this.p.getName(), result);
            super.withdrawPlayer(this.p.getName(), this.money);
        } else if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            EconomyPlus.getDBType().setTokens(String.valueOf(this.p.getUniqueId()), result);
            super.withdrawPlayer(String.valueOf(this.p.getUniqueId()), this.money);
        }

    }

    public void setBank () {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) EconomyPlus.getDBType().setBank(this.p.getName(), money);
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) EconomyPlus.getDBType().setBank(String.valueOf(this.p.getUniqueId()), money);
    }

    public double getBank () {
        if (EconomyPlus.getStorageMode() == StorageMode.NICKNAME) return EconomyPlus.getDBType().getBank(this.p.getName());
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) return EconomyPlus.getDBType().getBank(String.valueOf(this.p.getUniqueId()));
        return 0;
    }

    // Controls if the player has enough moneys
    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}
