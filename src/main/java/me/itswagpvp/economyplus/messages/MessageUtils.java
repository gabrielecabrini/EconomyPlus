package me.itswagpvp.economyplus.messages;

import org.bukkit.Bukkit;

import java.io.*;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class MessageUtils {

    private File file = null;

    // Create every file
    public void initialize() {
        new DefaultFiles().createMessagesEN();
        new DefaultFiles().createMessagesIT();
        new DefaultFiles().createMessagesRO();
        new DefaultFiles().createMessagesAL();
        new DefaultFiles().createMessagesDE();
    }

    // Save the resource in /messages folder
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(plugin.getDataFolder() + "/messages", resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(plugin.getDataFolder() + "/messages", resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                Bukkit.getConsoleSender().sendMessage("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage("Could not save " + outFile.getName() + " to " + outFile);
            ex.printStackTrace();
        }
    }

}
