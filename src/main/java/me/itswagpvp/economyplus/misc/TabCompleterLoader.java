package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;
import static me.itswagpvp.economyplus.misc.BalTopManager.getPages;

public class TabCompleterLoader implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        // /eco
        if (command.getName().equalsIgnoreCase("eco")) {

            switch (args.length) {
                case 1: {
                    return getPlayerNames();
                }
                case 2: {
                    return Arrays.asList("give", "set", "take", "reset");
                }
                case 3: {

                    if (args[1].equalsIgnoreCase("reset")) {
                        return Collections.singletonList("");
                    }

                    return Arrays.asList("0", "100", "1000");
                }
                default:
                    return Collections.singletonList("");
            }

        }

        // EconomyPlus main command TabCompleter
        if (command.getName().equalsIgnoreCase("economyplus")) {
            switch (args.length) {
                case 1:
                    return Arrays.asList("help", "debug", "reload", "hologram", "update", "convert", "exclude");
                case 2:
                    if (args[1].equalsIgnoreCase("convert")) {
                        Arrays.asList("UUID", "NICKNAME");
                    }
            }
            return Collections.singletonList("");
        }

        // /bal
        if (command.getName().equalsIgnoreCase("bal")) {
            if (args.length == 1) {
                return getPlayerNames();
            }
            return Collections.singletonList("");
        }

        // /pay
        if (command.getName().equalsIgnoreCase("pay")) {
            switch (args.length) {
                case 1: {
                    return getPlayerNames();
                }

                case 2: {
                    return Arrays.asList("10", "100", "1000");
                }

                default:
                    return Collections.singletonList("");
            }
        }

        // /baltop
        if (command.getName().equalsIgnoreCase("baltop")) {

            if (args.length == 1) {

                List<String> tab = new ArrayList<>();
                for(int i = 1; i<= getPages(); i++){
                    tab.add(String.valueOf(i));
                }

                return tab;
            }

            return Collections.singletonList("");

        }

        // /bank
        if (command.getName().equalsIgnoreCase("bank")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return Arrays.asList("deposit", "withdraw", "admin");
                }
                case 2: {
                    if (args[0].equalsIgnoreCase("admin")) {
                        return Arrays.asList("set", "get");
                    }

                    return Arrays.asList("10", "100", "1000");
                }

                case 3: {
                    if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("get")) {
                        return getPlayerNames();
                    }

                    return Collections.singletonList("");
                }

                case 4: {
                    if (args[1].equalsIgnoreCase("set")) {
                        return Arrays.asList("0", "10", "100");
                    }

                    return Collections.singletonList("");
                }

                case 5: {
                    if (args[1].equalsIgnoreCase("set")) {
                        return getPlayerNames();
                    }

                    return Collections.singletonList("");
                }

                case 6: {
                    if (args[1].equalsIgnoreCase("set")) {
                        return Arrays.asList("0", "10", "100");
                    }
                }

                default:
                    return Collections.singletonList("");
            }
        }

        return Collections.singletonList("");
    }

    private static List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        if (EconomyPlus.getStorageMode() == StorageMode.UUID) {
            // check for invalid user?
            CacheManager.getCache(1).forEach((player,value) -> {
                //debug
                String name = PlayerHandler.getName(player, true);
                if (!name.equalsIgnoreCase(player)) {
                    playerNames.add(name);
                } else {
                    // invalid user
                    if (plugin.purgeInvalid) { // has a exception when removing player and tab executing
                        EconomyPlus.getDBType().removePlayer(player);
                        CacheManager.getCache(1).remove(player);
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[EconomyPlus] Removed invalid account: " + player);
                    }
                }
            });
        } else {
            CacheManager.getCache(1).forEach((player,value) -> {
                //debug
                playerNames.add(player);
                Bukkit.broadcastMessage("NICKNAME: " + player);
            });
        }
        return playerNames;
    }

}
