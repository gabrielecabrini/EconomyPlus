package me.itswagpvp.economyplus.discord;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.utils.DiscordWebhook;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class MessagesDiscord {

    public static String webhookURL = plugin.getConfig().getString("DiscordWebhook.Url");

    public static void Enabled() throws IOException {
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);
        webhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription("EconomyPlus Enabled DiscordWebhook"));
        webhook.execute();
    }

    public static void Disabled() throws IOException {



    }

    public static void WebHook(Player p) {



    }

}
