package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.messages.MessageUtils;
import me.itswagpvp.economyplus.messages.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Utils {

    // Error sound played to player
    public static void playErrorSound(CommandSender sender) {

        if (!plugin.getConfig().getBoolean("Sounds.Use")) {
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            return;
        }

        Player player = (Player) sender;

        try {
            Sound x = Sound.valueOf(plugin.getConfig().getString("Sounds.Error", "ENTITY_VILLAGER_NO"));
            player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §7Error on the §cplayErrorSound§7! Check your config!");
            e.printStackTrace();
        }
    }

    // Success sound played to player
    public static void playSuccessSound(CommandSender sender) {

        if (!plugin.getConfig().getBoolean("Sounds.Use")) {
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            return;
        }

        Player player = (Player) sender;

        try {
            Sound x = Sound.valueOf(plugin.getConfig().getString("Sounds.Success", "ENTITY_PLAYER_LEVELUP"));
            player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §7Error on the §cplaySuccessSound§7! Check your config!");
            e.printStackTrace();
        }
    }

    public static void reloadPlugin(CommandSender p) {
        long before = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §aReloading the plugin! This action may take a while!");

        try {

            if (EconomyPlus.getDBType() == DatabaseType.YAML) {
                plugin.createYMLStorage();
            }

            plugin.saveDefaultConfig();
            plugin.reloadConfig();

            EconomyPlus.balTopManager = new BalTopManager();
            new BalTopManager();

            new StorageManager().createStorageConfig();

            String messages = plugin.getConfig().getString("Language");

            try {
                EconomyPlus.messagesType = MessagesFile.valueOf(messages);

                new MessageUtils().initialize();
            } catch (Exception e) {
                EconomyPlus.messagesType = MessagesFile.EN;
                return;
            }

        } catch (Exception e) {
            p.sendMessage("§cError on reloading the plugin! (" + e.getMessage() + ")");
            return;
        } finally {
            p.sendMessage(plugin.getMessage("Reload")
                    .replaceAll("%time%", "" + (System.currentTimeMillis() - before)));
        }

    }

    public String format(double d) {

        double value;

        if (plugin.getConfig().getBoolean("Decimals")) value = Double.parseDouble(String.format("%.2f", d));
        else value = Double.parseDouble(String.format("%.0f", d));

        if (plugin.getConfig().getBoolean("Baltop.Pattern.Enabled", false)) {

            String pattern = plugin.getConfig().getString("Baltop.Pattern.Value", "###.###,##");

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern(pattern);
            return decimalFormat.format(value);
        } else {
            return String.valueOf(value);
        }

    }

    public String fixMoney(double d) {

        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000D) + plugin.getConfig().getString("Format.k");
        }
        if (d < 1000000000L) {
            return format(d / 1000000D) + plugin.getConfig().getString("Format.M");
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000D) + plugin.getConfig().getString("Format.B");
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000D) + plugin.getConfig().getString("Format.T");
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000D) + plugin.getConfig().getString("Format.Q");
        }

        return format(d);
    }

    public static String hexColor(String text) {
        Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);
    }

    public static boolean supportHexColors() {
        return Bukkit.getVersion().contains("16") || Bukkit.getVersion().contains("17");
    }

}
