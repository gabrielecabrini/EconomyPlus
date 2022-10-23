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

    //Do we need to check for an update every hour? If it already alerts the user on join and on console start?
    //Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::check, 5 * 60 * 20, 3600 * 20); // Checks for an update every hour

    static EconomyPlus plugin = EconomyPlus.plugin;

    static int behind = 0;
    static boolean enabled = plugin.getConfig().getBoolean("Updater");

    static boolean alreadyDownloaded = false;
    static double cv = Double.parseDouble(plugin.getDescription().getVersion());
    static double lv = 0;

    public static void check() {

        if (!enabled || alreadyDownloaded) return;

        if (cv < getLatestVersion()) {

            behind = Integer.parseInt(String.valueOf(Math.round((getLatestVersion() - cv) / 0.1)).replace(".0", ""));

            Bukkit.getConsoleSender().sendMessage("§f-> §dEconomy§5Plus §cis outdated! (" + "v" + cv + ")");
            Bukkit.getConsoleSender().sendMessage("   - §fYou are §c" + behind + " §fversions!");
            Bukkit.getConsoleSender().sendMessage("   - §fUpdate to §dv" + getLatestVersion() + " §fusing §d/ep update");
            Bukkit.getConsoleSender().sendMessage("");

        }

    }

    public static double getLatestVersion() {

        if (!(lv == 0)) {
            return lv;
        }

        try {

            URL url = new URL("https://api.github.com/repos/ItsWagPvP/EconomyPlus/tags");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {

                if (line.contains("name")) {

                    line = line.split(":")[1];

                    line = line.replaceAll("\"", "");
                    line = line.replace(",zipball_url", "");
                    line = line.replaceAll("V", ""); //removes V in version if there is a V in the tag name
                    line = line.replaceAll("v", ""); //removes V in version if there is a V in the tag name

                    lv = Double.parseDouble(line);

                }
            }

            connection.disconnect();
            inputStream.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lv == 0) {
            plugin.getLogger().warning("Error with updater finding latest version!");
            return cv;
        }

        return lv;

    }

    private static boolean getUpdateAvailable() {

        return !alreadyDownloaded && cv < lv;

    }

    public static void checkForPlayerUpdate(Player e) {

        if (!enabled || alreadyDownloaded) return;

        boolean notifications = plugin.getConfig().getBoolean("Updater-Notifications") || plugin.getConfig().get("Updater-Notifications") == null;

        if (!getUpdateAvailable()) {
            return;
        }

        plugin.getLogger().info("notifications - " + notifications);

        if (notifications) {
            behind = Integer.parseInt(String.valueOf(Math.round((getLatestVersion() - cv) / 0.1)).replace(".0", ""));
            e.getPlayer().sendMessage("" +
                    "§7An update is available for §dEconomyPlus§7! §d(v" + getLatestVersion() + ")" +
                    "\n§7You are §c" + behind + " §7versions behind! §c(v" + cv + ")" +
                    "\n§7You can download it with §5/ep update");
        }

    }

    public static void downloadUpdate(CommandSender p) {

        if (!enabled) {
            p.sendMessage("§cThe updater is disabled.");
            Utils.playErrorSound(p);
            return;
        }

        if (!p.hasPermission("economyplus.update")) {
            p.sendMessage(plugin.getMessage("NoPerms"));
            return;
        }

        if (!getUpdateAvailable() || alreadyDownloaded) {
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
            URL website = new URL("https://github.com/ItsWagPvP/EconomyPlus/releases/download/V" + getLatestVersion() + "/EconomyPlus.jar");
            HttpURLConnection con = (HttpURLConnection) website.openConnection();

            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            FileOutputStream fos = new FileOutputStream(pluginsPath + "EconomyPlus.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            con.disconnect();
            rbc.close();
            fos.close();
            p.sendMessage("§aDone! §c§nIt is recommended you restart your server now for the plugin to update.");
            alreadyDownloaded = true;
        } catch (Exception e) {
            p.sendMessage("§cAn error occurred while trying to download the newest version. Check console for more info");
            e.printStackTrace();
        }

    }

}