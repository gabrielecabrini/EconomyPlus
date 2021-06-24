package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Bal implements CommandExecutor {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            if (args.length != 1) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
                return true;
            }

            Player p2 = Bukkit.getServer().getPlayer(args[0]);

            if (p2 == null) {

                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                return true;

            }

            if (p2 == sender) {

                sender.sendMessage(plugin.getMessage("NoConsole"));

                return true;
            }

            Economy otherEco = new Economy(p2, 0);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + otherEco.getBalance())
                    .replaceAll("%player%", ""+ p2.getName()));

            Utils.playSuccessSound(sender);

            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {

            if (!p.hasPermission("economyplus.balance")) {
                sender.sendMessage(plugin.getMessage("NoPerms"));
                Utils.playErrorSound(sender);
                return true;
            }

            Economy selfEco = new Economy(p, 0);

            p.sendMessage(plugin.getMessage("Balance.Self")
            .replaceAll("%money%", String.format("%.2f", selfEco.getBalance())));

            Utils.playSuccessSound(sender);

            return true;
        }

        if (args.length == 1) {

            if (!p.hasPermission("economyplus.balance.others")) {
                sender.sendMessage(plugin.getMessage("NoPerms"));
                Utils.playErrorSound(sender);
                return true;
            }

            Player p2 = Bukkit.getServer().getPlayer(args[0]);

            if (p2 == null) {

                p.sendMessage(plugin.getMessage("PlayerNotFound"));
                Utils.playErrorSound(sender);
                return true;

            }

            if (p2 == sender) {

                Economy selfEco = new Economy(p, 0);

                sender.sendMessage(plugin.getMessage("Balance.Self")
                        .replaceAll("%money%", "" + String.format("%.2f", selfEco.getBalance())));

                Utils.playSuccessSound(sender);

                return true;
            }

            Economy otherEco = new Economy(p2, 0);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + String.format("%.2f", otherEco.getBalance()))
                    .replaceAll("%player%", ""+ p2.getName()));

            Utils.playSuccessSound(sender);

            return true;

        }

        p.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
        Utils.playErrorSound(sender);
        return true;

    }
}
