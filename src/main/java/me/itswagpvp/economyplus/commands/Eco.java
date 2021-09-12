package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Eco implements CommandExecutor {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 3) {
            Player p = Bukkit.getServer().getPlayer(args[0]);

            if (p == null) {

                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                Utils.playErrorSound(sender);

                return true;
            }

            if (args[2].startsWith("-")) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return true;
            }

            String arg = null;
            if (args[2].contains(",")) {
                arg = args[2].replaceAll(",", ".");
            } else {
                arg = args[2];
            }

            double value = Double.parseDouble(arg);

            Economy money = new Economy(p, value);
            Utils utility = new Utils();

            if (args[1].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("economyplus.eco.set")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                money.setBalance();

                sender.sendMessage(plugin.getMessage("Money.Done"));
                p.sendMessage(plugin.getMessage("Money.Refreshed")
                .replaceAll("%money_formatted%", "" + utility.fixMoney(Double.parseDouble(args[2])))
                        .replaceAll("%money%", "" + utility.format(Double.parseDouble(args[2]))));

                Utils.playSuccessSound(sender);
                Utils.playSuccessSound(p);

                return true;
            }

            if (args[1].equalsIgnoreCase("take")) {
                if (!sender.hasPermission("economyplus.eco.take")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                double res = money.getBalance() - value;

                if (res < 0D) {
                    res = 0D;
                    Economy eco = new Economy(p, 0D);
                    eco.setBalance();
                } else {
                    money.takeBalance();
                }

                sender.sendMessage(plugin.getMessage("Money.Done"));
                p.sendMessage(plugin.getMessage("Money.Refreshed")
                        .replaceAll("%money%", "" + utility.fixMoney(res)));

                Utils.playSuccessSound(sender);
                Utils.playSuccessSound(p);

                return true;
            }

            if (args[1].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("economyplus.eco.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                double res = money.getBalance() + value;

                money.addBalance();

                sender.sendMessage(plugin.getMessage("Money.Done"));
                p.sendMessage(plugin.getMessage("Money.Refreshed")
                        .replaceAll("%money_formatted%", "" + utility.fixMoney(Double.parseDouble(args[2])))
                        .replaceAll("%money%", "" + utility.format(Double.parseDouble(args[2]))));

                Utils.playSuccessSound(sender);
                Utils.playSuccessSound(p);

                return true;
            }

            return true;

        }

        if (args.length == 2) {
            Player p = Bukkit.getServer().getPlayer(args[0]);

            if (p == null) {

                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                Utils.playErrorSound(sender);

                return true;
            }

            if (!args[0].equalsIgnoreCase("reset")) {

                Economy eco = new Economy(p, plugin.getConfig().getDouble("Starting-Balance"));
                eco.setBalance();

                sender.sendMessage(plugin.getMessage("Money.Done"));
                p.sendMessage(plugin.getMessage("Money.Reset"));

                Utils.playSuccessSound(sender);
                Utils.playErrorSound(p);

                return true;
            }
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }
}
