package me.itswagpvp.economyplus.listener;

import me.itswagpvp.economyplus.misc.Updater;
import me.itswagpvp.economyplus.vault.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class PlayerHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("Updater.Notifications", true)) {
            if (p.hasPermission("economyplus.update")) {
                Updater.checkForPlayerUpdate(p);
            }
        }

        Economy eco = new Economy(p);
        if (!(eco.hasAccount(p))) { //player doesn't have an account
            eco.setBalance(plugin.getConfig().getDouble("Starting-Balance", 0.00D));
            eco.setBank(plugin.getConfig().getDouble("Starting-Bank-Balance", 0.00D));
        }

        if (!p.hasPlayedBefore()) {
            saveName(p);
        }

    }

    public static String getName(String sid, boolean ifInvalidReturnUUID) {

        UUID id = UUID.fromString(sid);

        OfflinePlayer op = Bukkit.getOfflinePlayer(id);

        if (op.getName() != null) {
            return op.getName();
        }

        // player hasn't played before

        // check save names

        // thinking about removing this and replacing it with something else somehow
        // or just removing it entirely
        if (plugin.SAVE_NAMES) { // save names is enabled
            FileConfiguration config = YamlConfiguration.loadConfiguration(getStorageFile());
            if (config.getString("usernames." + id) != null) { // check if name is stored
                return config.getString("usernames." + id); // if so return it
            }
        }

        // IF USER IS INVALID

        // returnuuidifinvalid is true
        if (ifInvalidReturnUUID) {
            // will return uuid
            return String.valueOf(id);
        }

        // else return Invalid User
        return "Invalid User";
    }

    public static void saveName(OfflinePlayer p) {

        if (!plugin.SAVE_NAMES) {
            return;
        }

        File file = getStorageFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("usernames." + p.getUniqueId(), p.getName());

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadUsernames() {

        if (!plugin.SAVE_NAMES) {
            return;
        }

        File file = getStorageFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (config.get("usernames." + p.getUniqueId()) == null) {
                config.set("usernames." + p.getUniqueId(), p.getName());
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static File getStorageFile() {

        File file = new File(plugin.getDataFolder() + File.separator + "storage.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}