package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Data;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BalTop implements CommandExecutor {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("economyplus.baltop")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            List<String> header = plugin.getConfig().getStringList("Baltop.Chat.Header");

            EconomyPlus.data = new Data();
            new Data();
            Data data = EconomyPlus.plugin.getData();

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

            for ( int i = start; i < data.getBalTop().size() && i < start + 10; i++ ) {
                Data.PlayerData pData = data.getBalTop().get(i);

                String name = pData.getName();
                double money = pData.getMoney();

                sender.sendMessage(plugin.getConfig().getString("Baltop.Chat.Player-Format")
                        .replaceAll("&", "§")
                        .replaceAll("%number%", "" + (i + 1))
                        .replaceAll("%player%", "" + name)
                        .replaceAll("%money%", "" + utilities.fixMoney(money)));
            }

        });

        return true;
    }
}