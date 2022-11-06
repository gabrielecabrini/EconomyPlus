package me.itswagpvp.economyplus.messages;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static me.itswagpvp.economyplus.EconomyPlus.log;
import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Messages {

    private static final String path = plugin.getDataFolder() + "/messages";

    //Could change to loop through recourses folder but decided not to because of other files that would conflict.
    private static final List<String> list = Arrays.asList("EN", "IT", "RO", "AL", "DE", "ZH", "FR", "RU", "ES"); //language file names

    public static void load() {

        for (String name : list) {

            File file = new File(path, name + ".yml");

            if (!file.exists()) {

                file.getParentFile().mkdirs();

                InputStream stream = EconomyPlus.getPlugin(EconomyPlus.class).getResource(file.getName());

                try {

                    if (!(stream == null)) {
                        if (!(new File(plugin.getDataFolder() + File.separator + "messages" + File.separator + file.getName()).exists())) {
                            Files.copy(stream, new File(plugin.getDataFolder() + File.separator + "messages" + File.separator + file.getName()).toPath());
                        }
                    } else {
                        log("&cInvalid file in plugin recourses while loading messages: " + file.getName());
                    }

                } catch (IOException e) {
                    log("Failed to save " + file.getName() + " to: " + plugin.getDataFolder() + File.separator + "messages");
                    e.printStackTrace();
                }

            }

        }

    }

    public static FileConfiguration getMessageConfig(String name) {
        File file = new File(path, name + ".yml");
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(new File(path, name + ".yml"));
        }
        return null;
    }

}
