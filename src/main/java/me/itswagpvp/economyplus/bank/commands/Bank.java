package me.itswagpvp.economyplus.bank.commands;

import me.itswagpvp.economyplus.bank.menu.Deposit;
import me.itswagpvp.economyplus.bank.menu.Withdraw;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bank implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (sender != null) {

            if (args.length <= 0) {
                p.sendMessage("Use deposit/withdraw");
                return true;
            }

            if (args[0].equalsIgnoreCase("withdraw")) {

                Withdraw.openMenu(p, 27);
            } else if (args[0].equalsIgnoreCase("deposit")) {

                Deposit.openMenu(p, 27);
            }
        }

        return true;
    }

}