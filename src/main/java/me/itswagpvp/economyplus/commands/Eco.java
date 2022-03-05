package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Eco implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 3) {
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (args[2].startsWith("-")) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return true;
            }

            String arg;
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

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null)  {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(Double.parseDouble(args[2])))
                                .replaceAll("%money%", "" + utility.format(Double.parseDouble(args[2]))));

                        Utils.playSuccessSound(p.getPlayer());
                    }
                }

                Utils.playSuccessSound(sender);

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
            }

            if (args[1].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("economyplus.eco.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                money.addBalance();

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

            return true;

        }

        if (args.length == 2) {
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (!args[0].equalsIgnoreCase("reset")) {

                if (!sender.hasPermission("economyplus.eco.reset")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                Economy eco = new Economy(p, plugin.getConfig().getDouble("Starting-Balance"));
                eco.setBalance();

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                    Utils.playErrorSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);

                return true;
            }
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }
}
