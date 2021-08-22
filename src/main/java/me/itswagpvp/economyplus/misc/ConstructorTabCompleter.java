package me.itswagpvp.economyplus.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> listDefault = Collections.singletonList("");
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
                    return Arrays.asList("give", "set", "take");
                }
                case 3: {
                    return Arrays.asList("0", "100", "1000");
                }
                default:
                    return listDefault;
            }
        }

        // EconomyPlus main command TabCompleter
        if (command.getName().equalsIgnoreCase("economyplus")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return Arrays.asList("help", "debug", "reload", "hologram", "update");
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
                    return Arrays.asList("10", "100", "1000");
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
                    return Arrays.asList("1", "2", "3");
                }
                default:
                    return listDefault;
            }
        }

        // /bank
        if (command.getName().equalsIgnoreCase("bank")) {
            int i = (args.length);
            switch (i) {
                case 1: {
                    return Arrays.asList("deposit", "withdraw");
                }
                default:
                    return listDefault;
            }
        }

        return listDefault;
    }
}
