package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.hooks.MVdWPlaceholderAPI;
import me.itswagpvp.economyplus.hooks.PlaceholderAPI;
import me.itswagpvp.economyplus.messages.MessageUtils;
import me.itswagpvp.economyplus.messages.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Utils {

    // Error sound played to player
    public static void playErrorSound (CommandSender sender) {

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
    public static void playSuccessSound (CommandSender sender) {

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

    public static void onReload (CommandSender p) {
        long before = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] §aReloading the plugin! This action may take a while!");

        try {

            if (EconomyPlus.getDBType() == DatabaseType.YAML) {
                plugin.createYMLStorage();
            }

            plugin.saveDefaultConfig();
            plugin.reloadConfig();

            EconomyPlus.data = new Data();
            new Data();

            plugin.createHologramConfig();

            String messages = plugin.getConfig().getString("Language");

            try {
                EconomyPlus.messagesType = MessagesFile.valueOf(messages);

                new MessageUtils().initialize();
            } catch (Exception e) {
                EconomyPlus.messagesType = MessagesFile.EN;
                return;
            }

        }catch (Exception e) {
            p.sendMessage("§cError on reloading the plugin! (" + e.getMessage() + ")");
            return;
        } finally {
            p.sendMessage(plugin.getMessage("Reload")
                    .replaceAll("%time%", "" + (System.currentTimeMillis() - before)));
        }

    }

    public String format(double d) {
        if (plugin.getConfig().getBoolean("Decimals")) {
            return String.format("%.2f", d);
        } else {
            return String.format("%.0f", d);
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

    // Load MVdWPlaceholders

    public void loadMVdWPlaceholderAPI () {
        // MVdWPlaceholderAPI

        if (!plugin.getConfig().getBoolean("Hooks.MVdWPlaceholderAPI")) {
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("   - MVdWPlaceholderAPI: §cNot found!");
            return;
        }

        try {
            MVdWPlaceholderAPI MvdWPlaceholderAPI = new MVdWPlaceholderAPI();
            MvdWPlaceholderAPI.loadMVdWPlaceholders();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - MVdWPlaceholderAPI: §cError!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("   - MVdWPlaceholderAPI: §aDone!");
        }

        return;
    }

    // Load PlaceholderAPI

    public void loadPlaceholderAPI () {
        if (!plugin.getConfig().getBoolean("Hooks.PlaceholderAPI")) {
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §cNot found!");
            return;
        }

        try {
            new PlaceholderAPI(plugin).register();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §cError!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }finally {
            Bukkit.getConsoleSender().sendMessage("   - PlaceholderAPI: §aDone!");
        }
    }

}
