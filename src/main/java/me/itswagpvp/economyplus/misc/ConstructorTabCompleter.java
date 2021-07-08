package me.itswagpvp.economyplus.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> listDefault = Arrays.asList("");
        // Online Player List
        List<String> playerNames = Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        // /eco
        if (command.getName().equalsIgnoreCase("eco")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return playerNames;
                }
                case 2: {
                    List<String> listUse = Arrays.asList("set", "give", "take");
                    return listUse;
                }
                case 3: {
                    List<String> listMoney = Arrays.asList("0", "100", "1000");
                    return listMoney;
                }
                default:
                    return listDefault;
            }
        }

        // /ep
        if (command.getName().equalsIgnoreCase("economyplus")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    List<String> listUse = Arrays.asList("help", "debug", "reload");
                    return listUse;
                }
                default:
                    return listDefault;
            }
        }

        // /bal
        if (command.getName().equalsIgnoreCase("bal")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return playerNames;
                }
                default:
                    return listDefault;
            }
        }

        // /pay
        if (command.getName().equalsIgnoreCase("pay")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return playerNames;
                }

                case 2: {
                    List<String> numbers = Arrays.asList("10", "100", "1000");
                    return numbers;
                }

                default:
                    return listDefault;
            }
        }

        // /baltop
        if (command.getName().equalsIgnoreCase("baltop")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    List<String> numbers = Arrays.asList("1", "2", "3");
                    return numbers;
                }
                default:
                    return listDefault;
            }
        }

        return listDefault;
    }
}
