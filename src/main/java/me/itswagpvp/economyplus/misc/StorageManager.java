package me.itswagpvp.economyplus.misc;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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

            //plugin.getDataFolder().getParentFile().mkdirs();
            plugin.saveResource("storage.yml", false);

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
}
