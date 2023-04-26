package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;
import static me.itswagpvp.economyplus.listener.PlayerHandler.getName;
import static me.itswagpvp.economyplus.listener.PlayerHandler.hasAccount;

public class Bal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Utils.hasPerm(sender, "economyplus.balance") && plugin.REQUIRE_BASIC_PERMISSIONS) {
            return true;
        }

        if (sender instanceof ConsoleCommandSender && args.length != 1) {
            sender.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
            return false;
        }

        if (args.length == 0) {

            if (sender instanceof ConsoleCommandSender) {

            }

        }

            OfflinePlayer p2 = hasAccount(args[0]);

            if (p2 == null) {
                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                return true;
            }

            String name = getName(p2.getUniqueId().toString(), false);

            if (p2 == sender) {

                sender.sendMessage(plugin.getMessage("NoConsole"));

                return true;
            }

            Economy otherEco = new Economy(p2);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + new Utils().format(otherEco.getBalance()))
                    .replaceAll("%money_formatted%", "" + new Utils().fixMoney(otherEco.getBalance()))
                    .replaceAll("%player%", "" + name));

            Utils.playSuccessSound(sender);

            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {

            Economy selfEco = new Economy(p);

            p.sendMessage(plugin.getMessage("Balance.Self")
                    .replaceAll("%money%", "" + new Utils().format(selfEco.getBalance()))
                    .replaceAll("%money_formatted%", "" + new Utils().fixMoney(selfEco.getBalance())));

            Utils.playSuccessSound(sender);

            return true;
        }

        if (args.length == 1) {

            if (!Utils.hasPerm(p, "economyplus.balance.others") && plugin.REQUIRE_BASIC_PERMISSIONS) {
                return true;
            }

            OfflinePlayer p2 = hasAccount(args[0]);

            if (p2 == null) {
                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                return true;
            }

            String name = getName(p2.getUniqueId().toString(), false);

            if (p2 == sender) { // player mentioned is the sender

                Economy selfEco = new Economy(p);

                sender.sendMessage(plugin.getMessage("Balance.Self")
                        .replaceAll("%money%", "" + new Utils().format(selfEco.getBalance()))
                        .replaceAll("%money_formatted%", "" + new Utils().fixMoney(selfEco.getBalance())));

                Utils.playSuccessSound(sender);

                return true;
            }

            Economy otherEco = new Economy(p2);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + new Utils().format(otherEco.getBalance()))
                    .replaceAll("%money_formatted%", "" + new Utils().fixMoney(otherEco.getBalance()))
                    .replaceAll("%player%", "" + name));

            Utils.playSuccessSound(sender);

            return true;

        }

        p.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
        Utils.playErrorSound(sender);
        return true;

    }
}
