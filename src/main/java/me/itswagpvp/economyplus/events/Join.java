package me.itswagpvp.economyplus.events;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.updater.UpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    public EconomyPlus plugin = EconomyPlus.getInstance();

    @EventHandler
    public void setStartBalance (PlayerJoinEvent event) {

        if (event.getPlayer().hasPlayedBefore()) {
            return;
        }

        plugin.getRDatabase().setTokens(event.getPlayer().getName(), plugin.getConfig().getDouble("Starting-Balance"));

    }

    @EventHandler
    public void updateMessage (PlayerJoinEvent event) {

        if (EconomyPlus.ver < 12) {
            return;
        }

        if (!plugin.getConfig().getBoolean("Updater.Player")) {
            return;
        }

        Player p = event.getPlayer();

        if (!p.hasPermission("economyplus.updater")) {
            return;
        }

        int resourceId = 92975;
        new UpdateChecker(plugin, resourceId).getVersion(version -> {

            if (version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
                return;
            }

            String currentVersion = plugin.getDescription().getVersion();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EconomyPlus.plugin, () -> {

                //

                p.sendMessage("§d[EconomyPlus] §7Found an update: v" + version + " §e(You have v" + currentVersion + ")");
                TextComponent message = new TextComponent("§Click this message to download the newer version!");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/92975/"));
                message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§dOpen the spigot page!" ).create() ) );
                p.spigot().sendMessage(message);

            }, 0);
        });
    }
}
