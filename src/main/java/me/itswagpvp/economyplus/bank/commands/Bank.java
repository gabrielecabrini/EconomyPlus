package me.itswagpvp.economyplus.bank.commands;

import me.itswagpvp.economyplus.database.misc.Selector;
import me.itswagpvp.economyplus.hooks.events.PlayerBankChangeEvent;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Bank implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!plugin.getConfig().getBoolean("Bank.Enabled")) {

            sender.sendMessage(plugin.getMessage("Bank.Disabled"));
            Utils.playErrorSound(sender);
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(plugin.getMessage("NoConsole"));
            return true;
        }

        Player p = (Player) sender;

        if (args.length <= 0) {

            Utils utility = new Utils();
            double bank = new Economy(p, 0).getBank();

            p.sendMessage(plugin.getMessage("Bank.Self")
                    .replaceAll("%money_formatted%", "" + utility.fixMoney(bank))
                    .replaceAll("%money%", "" + utility.format(bank)));
            Utils.playSuccessSound(sender);
            return true;
        }

        if (args.length == 2) {

            double amount = Double.parseDouble(args[1]);
            double balance = new Economy(p, 0).getBalance();
            double bank = new Economy(p, 0).getBank();

            if (args[0].equalsIgnoreCase("withdraw")) {

                if (args[1].contains("-")) {
                    p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                    return true;
                }

                if (amount > bank) {
                    p.sendMessage(plugin.getMessage("Bank.NoMoney"));
                    Utils.playErrorSound(p);
                    return true;
                }

                Economy eco = new Economy(p, amount);
                eco.addBalance();

                double value = eco.getBank();

                Economy econ = new Economy(p, (value - amount));
                econ.setBank();

                p.sendMessage(plugin.getMessage("Bank.Withdraw").replaceAll("%money%", "" + amount));

                Utils.playSuccessSound(p);
                return true;
            }

            if (args[0].equalsIgnoreCase("deposit")) {

                if (args[1].contains("-")) {
                    p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                    return true;
                }

                if ((balance - amount) < 0) {
                    p.sendMessage(plugin.getMessage("Pay.NoMoney"));
                    Utils.playErrorSound(p);
                    return true;
                }

                Economy eco = new Economy(p, amount);
                eco.takeBalance();

                double value = eco.getBank();

                Economy econ = new Economy(p, (amount + value));
                econ.setBank();

                p.sendMessage(plugin.getMessage("Bank.Deposit").replaceAll("%money%", "" + amount));

                return true;
            }

            if (args[0].equalsIgnoreCase("admin")) {
                p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin")) {

                if (!p.hasPermission("economyplus.bank.admin")) {
                    p.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(p);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[2]);

                if (target == null) {
                    p.sendMessage(plugin.getMessage("PlayerNotFound"));
                    Utils.playErrorSound(p);
                    return true;
                }

                if (args[1].equalsIgnoreCase("get")) {

                    Utils utility = new Utils();
                    double bank = new Economy(target, 0).getBank();

                    p.sendMessage(plugin.getMessage("Bank.Admin.Get")
                                    .replaceAll("%player%", "" + target.getName())
                            .replaceAll("%money_formatted%", "" + utility.fixMoney(bank))
                            .replaceAll("%money%", "" + utility.format(bank)));

                    Utils.playSuccessSound(p);
                    return true;
                }

                if (args[1].equalsIgnoreCase("set")) {
                    p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                    Utils.playErrorSound(p);
                    return true;
                }

                return true;
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (!p.hasPermission("economyplus.bank.admin")) {
                    p.sendMessage(plugin.getMessage("NoPerms"));
                    return true;
                }

                if (args[1].equalsIgnoreCase("set")) {
                    Utils utility = new Utils();

                    Player target = Bukkit.getPlayer(args[2]);

                    if (target == null) {
                        p.sendMessage(plugin.getMessage("PlayerNotFound"));
                        Utils.playErrorSound(p);
                        return true;
                    }

                    double bank;
                    try {
                        bank = Double.parseDouble(args[3]);
                    } catch (Exception e) {
                        p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                        Utils.playErrorSound(p);
                        return true;
                    }

                    new Economy(target, bank).setBank();

                    p.sendMessage(plugin.getMessage("Bank.Admin.Set")
                            .replaceAll("%player%", "" + target.getName())
                            .replaceAll("%money_formatted%", "" + utility.fixMoney(bank))
                            .replaceAll("%money%", "" + utility.format(bank)));

                    target.sendMessage(plugin.getMessage("Bank.Admin.Refreshed")
                            .replaceAll("%money_formatted%", "" + utility.fixMoney(bank))
                            .replaceAll("%money%", "" + utility.format(bank)));

                    Utils.playSuccessSound(p);
                    Utils.playErrorSound(target);

                    return true;
                }

                p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
                Utils.playErrorSound(p);
                return true;

            }
        }

        p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
        Utils.playErrorSound(p);

        return true;

    }
}