package me.itswagpvp.economyplus.hooks.discord;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.utils.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class MessagesDiscord {



    // LoadBefore
    static long before;

    public static void Enabled() {
        String webhookURL = plugin.getConfig().getString("DiscordWebhook.Url");
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);
        Bukkit.getConsoleSender().sendMessage("§8+-----------------------------+");
        Bukkit.getConsoleSender().sendMessage("§dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§8->  Enabled DiscordWebhook");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");
        webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription("§dEconomy§5Plus §8Enabled DiscordWebhook"));
    }

    public static void Disabled() {
        String webhookURL = plugin.getConfig().getString("DiscordWebhook.Url");
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);
        Bukkit.getConsoleSender().sendMessage("§8+-----------------------------+");
        Bukkit.getConsoleSender().sendMessage("§dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§8->  Disabled DiscordWebhook");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");
        webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription("§dEconomy§5Plus §8Disabled DiscordWebhook"));
    }

    public static void WebHook(Player p) {
    }

}
