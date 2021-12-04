package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.BalTopManager;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class BalTop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("economyplus.baltop")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            List<String> header = plugin.getConfig().getStringList("Baltop.Chat.Header");

            EconomyPlus.balTopManager = new BalTopManager();
            new BalTopManager();
            BalTopManager balTopManager = EconomyPlus.plugin.getBalTopManager();

            Utils utilities = new Utils();

            int page;

            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                page = 1;
            }

            int start = (page - 1) * 10;

            for(String message : header) {
                sender.sendMessage(message
                        .replaceAll("%page%", "" + page)
                        .replaceAll("&", "§"));
            }


            for (int i = start; i < balTopManager.getBalTop().size() && i < start + 10; i++ ) {
                BalTopManager.PlayerData pData = balTopManager.getBalTop().get(i);

                String name = pData.getName();
                double money = pData.getMoney();

                sender.sendMessage(plugin.getConfig().getString("Baltop.Chat.Player-Format")
                        .replaceAll("&", "§")
                        .replaceAll("%number%", "" + (i + 1))
                        .replaceAll("%player%", "" + name)
                        .replaceAll("%money%", "" + utilities.format(money))
                        .replaceAll("%money_formatted%", "" + utilities.fixMoney(money)));
            }
        });

        return true;
    }
}