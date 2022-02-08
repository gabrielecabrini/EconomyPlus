package me.itswagpvp.economyplus.misc;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Level;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class StorageManager {

    private static File storageFile;
    private static FileConfiguration storageConfig;

    // Returns storage.yml
    public FileConfiguration getStorageConfig() {
        return storageConfig;
    }

    // Safe-save storage.yml
    public void saveStorageConfig() {
        try {
            storageConfig.save(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create storage.yml
    public void createStorageConfig() {
        File oldFile = new File(plugin.getDataFolder(), "holograms.yml");
        if (oldFile.exists()) {
            oldFile.renameTo(new File(plugin.getDataFolder(), "storage.yml"));
            createStorageConfig();
        } else {

            saveResource("storage.yml", false);

            storageFile = new File(plugin.getDataFolder(), "storage.yml");
        }

        loadStorageConfig();

    }

    public void loadStorageConfig() {
        storageConfig = new YamlConfiguration();
        try {
            storageConfig.load(storageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found!");
        }

        File outFile = new File(plugin.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

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
            }
        } catch (IOException ex) {
            System.out.println("Could not save " + outFile.getName() + " to " + outFile);
        }
    }
}
