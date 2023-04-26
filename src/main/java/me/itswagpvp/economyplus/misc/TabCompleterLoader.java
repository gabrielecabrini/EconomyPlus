package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.database.CacheManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import static me.itswagpvp.economyplus.misc.BalTopManager.getPages;

public class TabCompleterLoader implements TabCompleter {

    // EconomyPlus main command TabCompleter

    // ?
    // StringUtil.copyPartialMatches(args[1], twoArgList, completions);
    // Collections.sort(completions);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("eco")) {

            if (args.length == 1) {
                return getPlayerNames();
            }

            else if (args.length == 2) {
                return Arrays.asList("give", "set", "take", "reset");
            }

            else if (args.length == 3 && !args[1].equalsIgnoreCase("reset")) {
                return Arrays.asList("0", "100", "1000");
            }

        }

        else if (label.equalsIgnoreCase("economyplus")) {

            if (args.length == 1) {
                return Arrays.asList("help", "debug", "reload", "hologram", "update", "convert", "exclude");
            }

            else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("convert")) {
                    Arrays.asList("UUID", "NICKNAME");
                }
            }

        }

        else if (label.equalsIgnoreCase("bal")) {

            if (args.length == 1) {
                return getPlayerNames();
            }

        }

        else if (label.equalsIgnoreCase("pay")) {

            if (args.length == 1) {
                return getPlayerNames();
            }

            else if (args.length == 2) {
                return Arrays.asList("10", "100", "1000");
            }

        }

        else if (label.equalsIgnoreCase("baltop")) {

            if (args.length == 1) {

                List<String> tab = new ArrayList<>();
                for(int i = 1; i<= getPages(); i++){
                    tab.add(String.valueOf(i));
                }

                return tab;
            }

        }

        else if (label.equalsIgnoreCase("bank")) {

            if (args.length == 1) {
                return Arrays.asList("deposit", "withdraw", "admin");
            }

            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("admin")) {
                    return Arrays.asList("set", "get");
                }

                return Arrays.asList("10", "100", "1000");
            }

            else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("get")) {
                    return getPlayerNames();
                }
            }

            else if (args.length == 4 && args[1].equalsIgnoreCase("set")) {
                return Arrays.asList("0", "10", "100");
            }

            else if (args.length == 5 && args[1].equalsIgnoreCase("set")) {
                return getPlayerNames();
            }

            else if (args.length == 6 && args[1].equalsIgnoreCase("set")) {
                return Arrays.asList("0", "10", "100");
            }

        }

        return Collections.singletonList("");
    }

    private static List<String> getPlayerNames() {

        List<String> usernames = new ArrayList<>();

        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            usernames.add(op.getName());
        }

        // check if there is any extra players not in playerdata comparing db?

        return usernames;

    }

}
