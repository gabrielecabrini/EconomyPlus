package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Main implements CommandExecutor {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§d§lEconomyPlus §7v" + EconomyPlus.getInstance().getDescription().getVersion() + " made by §d_ItsWagPvP");
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

                Utils.onReload(sender);

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[0].equalsIgnoreCase("debug")) {

                if (!(sender instanceof ConsoleCommandSender)) {

                    sender.sendMessage(plugin.getMessage("NoPlayer"));

                    Utils.playErrorSound(sender);
                    return true;
                }

                sender.sendMessage("§8+------------------------------------+");
                sender.sendMessage("             §dEconomyPlus");
                sender.sendMessage("                §4Debug");
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7MC-Version of the server: §c" + Bukkit.getBukkitVersion());
                sender.sendMessage("§f-> §7Version of the plugin: §e" + EconomyPlus.plugin.getDescription().getVersion());
                sender.sendMessage("§f-> §7Version of the config: §e" + EconomyPlus.getInstance().getConfig().getString("Version"));
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7Server software: §6" + Bukkit.getName());
                sender.sendMessage("§f-> §7Software version: §6" + Bukkit.getVersion());
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7Online players: §3" + Bukkit.getServer().getOnlinePlayers().size());
                sender.sendMessage("§f-> §7Players saved: §3" + EconomyPlus.getInstance().getRDatabase().getList().size());
                sender.sendMessage("§a");
                sender.sendMessage("§f-> §7Vault Version: §d" + Bukkit.getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion());
                sender.sendMessage("§f-> §7PlaceholderAPI: §a" + Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"));
                sender.sendMessage("§8+------------------------------------+");

                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§d§lEconomyPlus §7v" + EconomyPlus.getInstance().getDescription().getVersion() + " made by §d_ItsWagPvP");
                sender.sendMessage("§7If you need support, join the discord server!");
                sender.sendMessage("§f-> §9https://discord.gg/vVM5SyKc8z");

                return true;
            }

            sender.sendMessage(plugin.getMessage("InvalidArgs.Main"));
            Utils.playErrorSound(sender);

        }

        return true;
    }
}