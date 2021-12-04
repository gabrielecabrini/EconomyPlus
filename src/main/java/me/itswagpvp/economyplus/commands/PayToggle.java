package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.misc.StorageManager;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class PayToggle implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(plugin.getMessage("NoConsole"));
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("economyplus.paytoggle")) {
            p.sendMessage(plugin.getMessage("NoPerms"));
            Utils.playErrorSound(p);
            return true;
        }

        StorageManager storage = new StorageManager();
        if (!storage.getStorageConfig().getBoolean("PayToggle." + p.getName())) {
            storage.getStorageConfig().set("PayToggle." + p.getName(), true);
            p.sendMessage(plugin.getMessage("Pay.Toggle.Enabled"));
        } else {
            storage.getStorageConfig().set("PayToggle." + p.getName(), false);
            p.sendMessage(plugin.getMessage("Pay.Toggle.Disabled"));
        }

        Utils.playSuccessSound(p);
        storage.saveStorageConfig();

        return true;
    }
}
