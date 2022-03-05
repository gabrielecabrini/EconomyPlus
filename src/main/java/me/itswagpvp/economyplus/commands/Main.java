package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.hooks.holograms.HolographicDisplays;
import me.itswagpvp.economyplus.misc.Converter;
import me.itswagpvp.economyplus.misc.StorageManager;
import me.itswagpvp.economyplus.misc.Updater;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Main implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§d§lEconomy§5§lPlus §7v" + plugin.getDescription().getVersion() + " made by §d_ItsWagPvP");
            sender.sendMessage("§7For help do /economyplus help");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("economyplus.reload")) {

                    sender.sendMessage(plugin.getMessage("NoPerms"));

                    Utils.playErrorSound(sender);
                    return true;
                }

                Utils.reloadPlugin(sender);

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[0].equalsIgnoreCase("debug")) {

                if (!(sender instanceof ConsoleCommandSender)) {

                    if (plugin.isMessageEnabled("NoPlayer")) {
                        sender.sendMessage(plugin.getMessage("NoPlayer"));
                    }

                    Utils.playErrorSound(sender);
                    return true;
                }

                sender.sendMessage("§8+------------------------------------+");
                sender.sendMessage("             §dEconomy§5Plus");
                sender.sendMessage("                §4Debug");
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7MC-Version of the server: §c" + Bukkit.getBukkitVersion());
                sender.sendMessage("§f-> §7Version of the plugin: §e" + plugin.getDescription().getVersion());
                sender.sendMessage("§f-> §7Version of the config: §e" + plugin.getConfig().getString("Version"));
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7Database: §b" + plugin.getConfig().getString("Database.Type"));
                sender.sendMessage("§f-> §7Storage-mode: §2" + plugin.getConfig().getString("Database.Mode", "NICKNAME"));
                sender.sendMessage("§f-> §7Messages file: §2" + plugin.getConfig().getString("Language", "EN"));
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7Server software: §6" + Bukkit.getName());
                sender.sendMessage("§f-> §7Software version: §6" + Bukkit.getVersion());
                sender.sendMessage("§f-> §7Vault Version: §d" + Bukkit.getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion());
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7PlaceholderAPI: §a" + Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"));
                sender.sendMessage("§f-> §7HolographicDisplays: §a" + Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"));
                sender.sendMessage("§8+------------------------------------+");

                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§d§lEconomy§5§lPlus §7v" + plugin.getDescription().getVersion() + " made by §d_ItsWagPvP");
                sender.sendMessage("§7If you need support, join the discord server!");
                sender.sendMessage("§f-> §9https://discord.itswagpvp.eu/");

                return true;
            }

            if (args[0].equalsIgnoreCase("hologram")) {

                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(plugin.getMessage("NoConsole"));
                    return true;
                }

                Player p = (Player) sender;

                if (!p.hasPermission("economyplus.hologram")) {
                    p.sendMessage(plugin.getMessage("NoPerms"));
                }

                Location loc = p.getLocation();

                StorageManager storageManager = new StorageManager();

                storageManager.getStorageConfig().set("Hologram.BalTop.World", loc.getWorld().getName());
                new StorageManager().saveStorageConfig();

                storageManager.getStorageConfig().set("Hologram.BalTop.X", loc.getX());
                new StorageManager().saveStorageConfig();

                storageManager.getStorageConfig().set("Hologram.BalTop.Y", loc.getY());
                new StorageManager().saveStorageConfig();

                storageManager.getStorageConfig().set("Hologram.BalTop.Z", loc.getZ());
                new StorageManager().saveStorageConfig();

                new HolographicDisplays().createHologram();

                return true;
            }

            if (args[0].equalsIgnoreCase("update")) {

                if (!sender.hasPermission("economyplus.update")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                Updater.getInstance().downloadUpdate(sender);

                return true;
            }

        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("convert")) {

                if (!(sender instanceof ConsoleCommandSender)) {
                    if (plugin.isMessageEnabled("NoPlayer")) {
                        sender.sendMessage(plugin.getMessage("NoPlayer"));
                        Utils.playErrorSound(sender);
                    }
                    return true;
                }

                String newStorageMode = args[1];
                if (!(newStorageMode.equalsIgnoreCase("uuid") || newStorageMode.equalsIgnoreCase("nickname"))) {
                    sender.sendMessage("§cYou have to set /ep convert <UUID/NICKNAME>");
                    return true;
                }

                int updatedAccounts = new Converter().convert();
                sender.sendMessage("§aYou have converted " + updatedAccounts + " accounts to " + newStorageMode.toUpperCase(Locale.ROOT) + " storage mode!");
                return true;
            }

            if (args[0].equalsIgnoreCase("exclude")) {

                if (!(sender instanceof ConsoleCommandSender)) {
                    if (plugin.isMessageEnabled("NoPlayer")) {
                        sender.sendMessage(plugin.getMessage("NoPlayer"));
                        Utils.playErrorSound(sender);
                    }
                    return true;
                }

                if (plugin.getConfig().getBoolean("BalTop.Exclude." + args[1])) {
                    new StorageManager().getStorageConfig().set("BalTop.Exclude." + args[1], false);
                    sender.sendMessage("§aIncluded " + args[1] + " in the BalTop!");
                } else {
                    new StorageManager().getStorageConfig().set("BalTop.Exclude." + args[1], true);
                    sender.sendMessage("§aExcluded " + args[1] + " from the BalTop!");
                }

                new StorageManager().saveStorageConfig();

                return true;
            }
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Main"));
        Utils.playErrorSound(sender);

        return true;
    }
}