package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Eco implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!Utils.hasPerm(sender, "economyplus.eco.reset") || !Utils.hasPerm(sender, "economyplus.eco.give") || !Utils.hasPerm(sender, "economyplus.eco.take") || !Utils.hasPerm(sender, "economyplus.eco.set")) {
            return true;
        }

        if (args.length == 2) {

            OfflinePlayer p;

            String name = args[0];

            double starting = plugin.getConfig().getDouble("Starting-Balance"); // gets starting balance

            if (args[1].equalsIgnoreCase("reset")) {

                if (name.equalsIgnoreCase("@a") || name.equalsIgnoreCase("*")) {

                    // is using @a or *

                    if (plugin.getConfig().getBoolean("Reset-All.Flags", false)) {

                        if (plugin.getConfig().getBoolean("Reset-All.Console-Only", true)) {
                            if (sender instanceof Player) {
                                // player is not console
                                sender.sendMessage(ChatColor.RED + "Only console can use @a and * flags!");
                                return true;
                            }
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "Resetting everyone's balance is disabled!");
                        Utils.playErrorSound(sender);
                        return true;
                    }

                    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {

                        Economy eco = new Economy(op);
                        eco.setBalance(starting);

                        if (op.getPlayer() != null && op.isOnline()) {
                            op.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                            Utils.playErrorSound(op.getPlayer());
                        }

                    }

                    if (plugin.isMessageEnabled("Money.Done")) {
                        sender.sendMessage(ChatColor.GREEN + "You have just refreshed everyone's balances!");
                    }

                    Utils.playSuccessSound(sender);

                } else {

                    // isn't using * or @a

                    p = Bukkit.getOfflinePlayer(args[0]);

                    if (!(p.hasPlayedBefore() || p.isOnline())) {

                        if (plugin.SET_INVALID) {
                            PlayerHandler.saveName(p);
                        } else {
                            // player hasn't joined before and isn't currently online
                            sender.sendMessage(ChatColor.RED + args[0] + " hasn't joined before.");
                            Utils.playErrorSound(sender);
                            return true;
                        }

                    }

                    if (!Utils.hasPerm(sender, "economyplus.eco.reset")) {
                        return true;
                    } else {

                        Economy eco = new Economy(p);
                        eco.setBalance(starting);

                        if (plugin.isMessageEnabled("Money.Done")) {
                            sender.sendMessage(plugin.getMessage("Money.Done"));
                        }

                        if (p.isOnline() && p.getPlayer() != null) {
                            p.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                        }

                        Utils.playSuccessSound(sender);

                    }

                }

                return true;
            }

        } else if (args.length == 3) {

            if (!(args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("take"))) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return false;
            }

            String arg = args[2].replace(",", ".");

            double value;
            try {
                value = Double.parseDouble(arg);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid number!"));
                return true;
            }

            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            Economy money = new Economy(p);
            Utils utility = new Utils();

            if (args[1].equalsIgnoreCase("set")) {

                if (!Utils.hasPerm(sender, "economyplus.eco.set")) {
                    return true;
                }

                money.setBalance(value);

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(value))
                                .replaceAll("%money%", "" + utility.format(value)));

                        Utils.playSuccessSound(p.getPlayer());
                    }
                }

                Utils.playSuccessSound(sender);

                return true;

            } else if (args[1].equalsIgnoreCase("take")) {

                if (!Utils.hasPerm(sender, "economyplus.eco.take")) {
                    return true;
                }

                double res = money.getBalance(p) - value;

                if (res < 0D) {
                    res = 0D;
                    money.setBalance(0D);
                } else {
                    money.takeBalance(value);
                }

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money%", "" + utility.format(res))
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(res)));
                        Utils.playSuccessSound(p.getPlayer());
                    }
                }

                Utils.playSuccessSound(sender);

                return true;

            } else if (args[1].equalsIgnoreCase("give")) {

                if (!Utils.hasPerm(sender, "economyplus.eco.give")) {
                    return true;
                }

                money.addBalance(value);

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(money.getBalance()))
                                .replaceAll("%money%", "" + utility.format(money.getBalance())));
                        Utils.playSuccessSound(p.getPlayer());
                    }
                }

                Utils.playSuccessSound(sender);
                return true;
            }

        }

        // usage
        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }

}