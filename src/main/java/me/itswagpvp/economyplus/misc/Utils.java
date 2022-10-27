package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.itswagpvp.economyplus.EconomyPlus.log;
import static me.itswagpvp.economyplus.EconomyPlus.plugin;
import static me.itswagpvp.economyplus.vault.Economy.round;

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
            player.playSound(player.getPlayer().getLocation(), x, 1, 1);
        } catch (Exception e) {
            log("[EconomyPlus] &7Error on the &cplayErrorSound&7! Check your config!");
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
            player.playSound(player.getPlayer().getLocation(), x, 1, 1);
        } catch (Exception e) {
            log("[EconomyPlus] &7Error on the &cplaySuccessSound§&! Check your config!");
            e.printStackTrace();
        }
    }

    public static void reloadPlugin(CommandSender p) {
        long before = System.currentTimeMillis();

        log("[EconomyPlus] &aReloading the plugin! This action may take a while!");

        try {

            if (EconomyPlus.getDBType() == DatabaseType.YAML) {
                plugin.createYMLStorage();
            }

            plugin.saveDefaultConfig();
            plugin.reloadConfig();

            EconomyPlus.balTopManager = new BalTopManager();
            new BalTopManager();

            new StorageManager().createStorageConfig();

            Messages.load();

        } catch (Exception e) {
            p.sendMessage("§cError on reloading the plugin! (" + e.getMessage() + ")");
        } finally {
            p.sendMessage(plugin.getMessage("Reload")
                    .replaceAll("%time%", "" + (System.currentTimeMillis() - before)));
        }

        log("&aReloaded!");

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

    public static boolean supportRGBColors() {
        return Bukkit.getVersion().contains("16")
                || Bukkit.getVersion().contains("17")
                || Bukkit.getVersion().contains("18")
                || Bukkit.getVersion().contains("19");
    }

    public String format(double d) {

        String value = String.valueOf(d);
        if(value.contains("E")) {
            value = String.valueOf(BigDecimal.valueOf(d).doubleValue());

            if(!(value.contains("E"))) {
                StringBuilder b = new StringBuilder(value);
                b.replace(value.lastIndexOf("0"), value.lastIndexOf("0") + 1, "");
                value = b.toString();
            } else {
                //needs work (formatting 0.00000005 returns E values
                // trying to make a formatting system but i think my mind is actually going crazy. I am feeling something far from this reality
                // when dealing with these issues. Ill have to come back to it feel free to make changes please.....
                // this really is doing me.
            }
        }

        Bukkit.broadcastMessage("1 - " + value);

        int decimal = 2;
        if (!(plugin.getConfig().get("Formatting.Decimal-Places") == null)) {
            decimal = plugin.getConfig().getInt("Formatting.Decimal-Places");
        }

        if (plugin.getConfig().getBoolean("Formatting.Use-Decimals")) {

            Bukkit.broadcastMessage("2 - " + value);

            if (plugin.getConfig().getBoolean("Formatting.Remove-Unnecessary-Values")) {

                //split string from 10.20 to 10 and 20
                String split = value.split("\\.")[1];
                Bukkit.broadcastMessage("3 - " + value);

                //if all chars are 0
                int count = split.length() - split.replaceAll("0", "").length();

                if (count == split.length()) {
                    value = value.split("\\.")[0];
                    Bukkit.broadcastMessage("4 - " + value);
                } else {

                    Bukkit.broadcastMessage("5 - " + value);

                    //else ensure it is round decimal size
                    if (!(split.length() == decimal)) {
                        Bukkit.broadcastMessage("6 - " + value);

                        if (split.length() < decimal) {

                            Bukkit.broadcastMessage("7 - " + value);

                            for (int i = 0; i < decimal - split.length(); i++) {
                                value = value + "0";
                            }

                        } else {
                            value = String.format("%." + decimal + "f", d);
                            Bukkit.broadcastMessage("8 - " + value);
                        }

                    }

                }

            }

        } else {
            Bukkit.broadcastMessage("9 - " + value);
            value = String.format("%.0f", d);
        }

        if (plugin.getConfig().getBoolean("Baltop.Pattern.Enabled")) {
            Bukkit.broadcastMessage("10");
            String pattern = plugin.getConfig().getString("Baltop.Pattern.Value", "###,###.##");

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern(pattern);
            return decimalFormat.format(value);
        } else {
            Bukkit.broadcastMessage("11 - " + value);
            return value;
        }

    }

    public String fixMoney(double d) {

        Bukkit.broadcastMessage("fix: " + d);

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

}
