package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.apache.commons.lang.math.NumberUtils;
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

            if (!NumberUtils.isNumber(args[2])) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return true;
            }

            double value = Double.parseDouble(args[2]);

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
                .replaceAll("%money%", "" + utility.fixMoney(Double.parseDouble(args[2]))));

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

                money.takeBalance();



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
                        .replaceAll("%money%", "" + utility.fixMoney(res)));

                Utils.playSuccessSound(sender);
                Utils.playSuccessSound(p);

                return true;
            }

            return true;

        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }
}
