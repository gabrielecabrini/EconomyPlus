package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;

public class Updater implements Listener {

    static EconomyPlus plugin = null;
    static Updater instance = null;

    boolean ready = false;
    boolean updateAvailable = false;
    boolean alreadyDownloaded = false;

    String latestVersion = "";
    String currentVersion = "";

    boolean enabled;

    public Updater(EconomyPlus plugin) {
        instance = this;
        enabled = plugin.getConfig().getBoolean("Updater", true);

        if (!enabled) return;

        Updater.plugin = plugin;

        currentVersion = plugin.getDescription().getVersion();

        check();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::check, 5 * 60 * 20,3600 * 20); // Checks for an update every hour
    }

    public void check() {

        if (!enabled || alreadyDownloaded) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://dev.itswagpvp.eu/api/EconomyPlus/version.html");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                StringBuilder string = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    string.append(line);
                }

                latestVersion = string.toString();

                if (latestVersion.isEmpty()) return;

                if (!latestVersion.equals(currentVersion)) {
                    updateAvailable = true;
                }

                if (updateAvailable && !ready) {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] An update is available! §d(v" + latestVersion + ")");
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] You have §cv" + plugin.getDescription().getVersion());
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] Download it with /ep update!");
                } else if (!ready) {
                    Bukkit.getConsoleSender().sendMessage("[EconomyPlus] You are up to date! §d(v" + latestVersion + ")");
                }

                ready = true;
            } catch (IOException e) {
                if (!e.getMessage().contains("HTTP response code: 503")) {
                    plugin.getLogger().warning("Unable to check for an update! " + e.getMessage());
                }
            }
        });
    }

    public static Updater getInstance() {
        return instance;
    }

    public static Updater getInstance(EconomyPlus plugin) {
        if (instance == null) {
            new Updater(plugin);
        }
        return instance;
    }

    public void checkForPlayerUpdate(Player e) {
        if (!enabled || alreadyDownloaded) return;
        if (ready && updateAvailable && plugin.getConfig().getBoolean("Updater", true)) {
            if (!e.getPlayer().hasPermission("economyplus.update") || e.getPlayer().isOp()) return;
            e.getPlayer().sendMessage("" +
                    "§7An update is available for §dEconomyPlus§7! " +
                    "\n§7You can download it with §5/ep update");
        }
    }

    public void downloadUpdate(CommandSender p) {
        if (!enabled) {
            p.sendMessage("§cThe updater is disabled.");
            Utils.playErrorSound(p);
            return;
        }
        if (!p.hasPermission("economyplus.update")) {
            p.sendMessage(plugin.getMessage("NoPerms"));
            return;
        }
        if (!updateAvailable || alreadyDownloaded) {
            p.sendMessage(("§cThere is no update to download!"));
            Utils.playErrorSound(p);
            return;
        }
        String curJarName = "EconomyPlus.jar";
        String[] slashParts = plugin.getDataFolder().toString().split(Matcher.quoteReplacement(File.separator));
        StringBuilder pluginsPath = new StringBuilder();
        int i = 0;
        for (String part : slashParts) {
            pluginsPath.append(part).append(File.separator);
            i++;
            if (i + 1 >= slashParts.length) break;
        }
        File oldJar = new File(pluginsPath + curJarName);
        if (!oldJar.exists()) {
            Bukkit.getLogger().warning("[EconomyPlus] Unable to find jar " + pluginsPath + curJarName);
            p.sendMessage("§cUnable to find old jar! §7Please make sure it matches the format §fEconomyPlus.jar§7!");
            return;
        }

        try {
            URL website = new URL("https://dev.itswagpvp.eu/plugins/jar/EconomyPlus.jar");
            HttpURLConnection con = (HttpURLConnection) website.openConnection();

            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            FileOutputStream fos = new FileOutputStream(pluginsPath + "EconomyPlus.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            p.sendMessage("§aDone! §7Restart the server and the new version will be ready!");
            updateAvailable = false;
            alreadyDownloaded = true;
        } catch (Exception e) {
            p.sendMessage("§cAn error occurred while trying to download the newest version. Check console for more info");
            e.printStackTrace();
        }
    }

}
