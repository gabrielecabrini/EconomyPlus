package me.itswagpvp.economyplus.hooks.events;

import me.itswagpvp.economyplus.database.misc.Selector;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBankChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    private String player;
    private double newBank;

    public PlayerBankChangeEvent(String player, double newBank) {
        this.player = player;
        this.newBank = newBank;
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    public void setNewBank(double newBank) {
        this.newBank = newBank;
    }

    public double getNewBank() {
        return newBank;
    }

    public Player getPlayer() {
        return Selector.stringToPlayer(player);
    }
}