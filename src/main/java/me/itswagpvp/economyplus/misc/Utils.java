package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.commands.Eco;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    // Error sound played to player
    public static void playErrorSound (CommandSender sender) {

        if (!EconomyPlus.getInstance().getConfig().getBoolean("Sounds.Use")) {
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            return;
        }

        Player player = (Player) sender;

        try {
            Sound x = Sound.valueOf(EconomyPlus.getInstance().getConfig().getString("Sounds.Error", "ENTITY_VILLAGER_NO"));
            player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §7Error on the §cplayErrorSound§7! Check your config!");
            e.printStackTrace();
        }
    }

    // Success sound played to player
    public static void playSuccessSound (CommandSender sender) {

        if (!EconomyPlus.getInstance().getConfig().getBoolean("Sounds.Use")) {
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            return;
        }

        Player player = (Player) sender;

        try {
            Sound x = Sound.valueOf(EconomyPlus.getInstance().getConfig().getString("Sounds.Success", "ENTITY_PLAYER_LEVELUP"));
            player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §7Error on the §cplaySuccessSound§7! Check your config!");
            e.printStackTrace();
        }
    }

    public static void onReload (CommandSender p) {
        long before = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §aReloading the plugin! This action may take a while!");

        try {
            EconomyPlus.plugin.saveDefaultConfig();
            EconomyPlus.plugin.reloadConfig();

            EconomyPlus.plugin.createMessagesConfig();

        }catch (Exception e) {
            p.sendMessage("§cError on reloading the plugin!");
            p.sendMessage(e.getMessage());
            return;
        } finally {
            p.sendMessage(EconomyPlus.plugin.getMessage("Reload")
                    .replaceAll("%time%", "" + (System.currentTimeMillis() - before)));
            return;
        }

    }

    public String toLong(double amt) {
        return String.valueOf((long) amt);
    }

    public String format(double d) {
        return String.format("%.2f", d);
    }

    public String fixMoney(double d) {

        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000D) + EconomyPlus.getInstance().getConfig().getString("Format.k");
        }
        if (d < 1000000000L) {
            return format(d / 1000000D) + EconomyPlus.getInstance().getConfig().getString("Format.M");
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000D) + EconomyPlus.getInstance().getConfig().getString("Format.B");
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000D) + EconomyPlus.getInstance().getConfig().getString("Format.T");
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000D) + EconomyPlus.getInstance().getConfig().getString("Format.Q");
        }

        return String.format("%.2f", d);
    }

}
