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
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Eco implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        OfflinePlayer p = null;

        boolean all = false;

        if (args.length >= 1) {

            String name = args[0];

            if ((name.equalsIgnoreCase("@a") || name.equalsIgnoreCase("*") && args.length == 2 && args[1].equalsIgnoreCase("reset"))) {
                all = true;
            }

            if ((!name.matches("^[a-zA-Z0-9]*$") || args[0].length() > 16) && plugin.getConfig().getBoolean("Invalid-Users.Username-Limit", true)) {

                if (!(name.equalsIgnoreCase("@a") || name.equalsIgnoreCase("*"))) {
                    sender.sendMessage(ChatColor.RED + "Invalid Username: " + name);
                    Utils.playErrorSound(sender);
                    return false;
                }

            }

            if (!all) { // all flag wasn't used and args[0] is a player's username
                p = Bukkit.getOfflinePlayer(args[0]);
            }

        }

        if (!all) {

            if (p == null) {
                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                return false;
            }

            if (plugin.getConfig().getBoolean("Invalid-Users.Modify-Balance", false)) {
                PlayerHandler.saveName(p.getUniqueId(), p.getName());
            } else { // Modifying invalid users is disabled in config.
                if (!p.hasPlayedBefore() && !p.isOnline()) {
                    // player hasn't joined before and isn't currently online
                    sender.sendMessage(ChatColor.RED + args[0] + " hasn't joined before.");
                    Utils.playErrorSound(sender);
                    return false;
                }
            }

        }

        if (args.length == 3) {

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

            Economy money = new Economy(p);
            Utils utility = new Utils();

            if (args[1].equalsIgnoreCase("set")) {

                if (!sender.hasPermission("economyplus.eco.set")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
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

                if (!sender.hasPermission("economyplus.eco.take")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
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
                if (!sender.hasPermission("economyplus.eco.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
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

            sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
            Utils.playErrorSound(sender);

            return true;

        }

        if (args.length == 2) {

            if (args[1].equalsIgnoreCase("reset")) {

                if (!sender.hasPermission("economyplus.eco.reset")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                double starting = plugin.getConfig().getDouble("Starting-Balance"); // gets starting balance

                if (all) { // wild flag used (@a or *)

                    if (!(plugin.getConfig().getBoolean("Invalid-Users.Allow-Reset-All"))) {
                        sender.sendMessage(ChatColor.RED + "Resetting everyone's balance is disabled in config!");
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

                }

                else {

                    Economy eco = new Economy(p);
                    eco.setBalance(starting);

                    if (plugin.isMessageEnabled("Money.Done")) {
                        sender.sendMessage(plugin.getMessage("Money.Done"));
                    }

                    if (p.getPlayer() != null) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                        Utils.playErrorSound(p.getPlayer());
                    }

                    Utils.playSuccessSound(sender);

                }

                return true;
            }

        }

        if (!sender.hasPermission("economyplus.eco.reset") || !sender.hasPermission("economyplus.eco.give") || !sender.hasPermission("economyplus.eco.take") || !sender.hasPermission("economyplus.eco.set")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            Utils.playErrorSound(sender);
            return true;
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }

}
