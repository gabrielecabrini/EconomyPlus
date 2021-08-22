package me.itswagpvp.economyplus.bank.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.bank.menu.Deposit;
import menu.Withdraw;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Bank implements CommandExecutor {

    private static EconomyPlus plugin = EconomyPlus.getInstance();

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
            p.sendMessage(plugin.getMessage("InvalidArgs.Bank"));
            Utils.playErrorSound(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("withdraw")) {

            Withdraw.openMenu(p, 54);
            Utils.playSuccessSound(p);

        }

        if (args[0].equalsIgnoreCase("deposit")) {

            Deposit.openMenu(p, 54);
            Utils.playSuccessSound(p);

        }

        return true;

    }
}