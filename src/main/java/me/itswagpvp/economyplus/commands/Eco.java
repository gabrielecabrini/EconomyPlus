package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.utils.DiscordWebhook;
import me.itswagpvp.economyplus.vault.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Eco implements CommandExecutor {

    public static String webhookURL = plugin.getConfig().getString("DiscordWebhook.Url");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 3) {

            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (args[2].startsWith("-")) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return true;
            }

            String arg = args[2].replace(",",".");

            double value;
            try {
                value = Double.parseDouble(arg);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid number!"));
                return true;
            }

            Economy money = new Economy(p);
            Utils utility = new Utils();

            if (args[1].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("economyplus.eco.set")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                money.setBalance(value);

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(value))
                                .replaceAll("%money%", "" + utility.format(value)));
                        Utils.playSuccessSound(p.getPlayer());
                       if (plugin.getConfig().getBoolean("DiscordWebhook.Enabled", false)) {
                            DiscordWebhook webhook = new DiscordWebhook(webhookURL);
                            webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription(p.getPlayer().getName() + " to set his money to " + value + "$ !"));
                            try {
                                webhook.execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[1].equalsIgnoreCase("take")) {

                if (!sender.hasPermission("economyplus.eco.take")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                double res = money.getBalance(p) - value;

                if (res < 0D) {
                    res = 0D;
                    money.setBalance(0D);
                } else {
                    money.takeBalance(value);
                }

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money%", "" + utility.format(res))
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(res)));
                        Utils.playSuccessSound(p.getPlayer());
                        if (plugin.getConfig().getBoolean("DiscordWebhook.Enabled", false)) {
                            DiscordWebhook webhook = new DiscordWebhook(webhookURL);
                            webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription(p.getPlayer().getName() + " took " + value + "$ !"));
                            try {
                                webhook.execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[1].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("economyplus.eco.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                money.addBalance(value);

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    if (plugin.isMessageEnabled("Money.Refreshed")) {
                        p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                                .replaceAll("%money_formatted%", "" + utility.fixMoney(money.getBalance()))
                                .replaceAll("%money%", "" + utility.format(money.getBalance())));
                        Utils.playSuccessSound(p.getPlayer());
                        if (plugin.getConfig().getBoolean("DiscordWebhook.Enabled", false)) {
                            DiscordWebhook webhook = new DiscordWebhook(webhookURL);
                            webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription(p.getPlayer().getName() + " it's giving " + value + "$ !"));
                            try {
                                webhook.execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                Utils.playSuccessSound(sender);


                return true;
            }

            return true;

        }

        if (args.length == 2) {
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (!args[0].equalsIgnoreCase("reset")) {

                if (!sender.hasPermission("economyplus.eco.reset")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                Economy eco = new Economy(p);
                eco.setBalance(plugin.getConfig().getDouble("Starting-Balance"));

                if (plugin.isMessageEnabled("Money.Done")) {
                    sender.sendMessage(plugin.getMessage("Money.Done"));
                }

                if (p.getPlayer() != null) {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                    Utils.playErrorSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);
                if (plugin.getConfig().getBoolean("DiscordWebhook.Enabled", false)) {
                    DiscordWebhook webhook = new DiscordWebhook(webhookURL);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription(p.getPlayer().getName() + "reset his wallet!"));
                    try {
                        webhook.execute();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
        }

        if(!sender.hasPermission("economyplus.eco.reset") || !sender.hasPermission("economyplus.eco.give") || !sender.hasPermission("economyplus.eco.take") || !sender.hasPermission("economyplus.eco.set")) {
            sender.sendMessage(plugin.getMessage("NoPerms"));
            Utils.playErrorSound(sender);
            return true;
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }
}
