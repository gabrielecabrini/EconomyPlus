package me.itswagpvp.economyplus;

import me.itswagpvp.economyplus.database.Database;
import me.itswagpvp.economyplus.database.SQLite;
import me.itswagpvp.economyplus.misc.UpdateChecker;
import me.itswagpvp.economyplus.misc.UpdateMessage;
import me.itswagpvp.economyplus.vault.VEconomy;
import me.itswagpvp.economyplus.vault.VHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class EconomyPlus extends JavaPlugin {

    // Messages file
    private File messagesConfigFile;
    private FileConfiguration messagesConfig;

    // Database
    private Database db;

    // Economy
    public static Economy econ;
    public static VEconomy veco;
    public static VHook hook;

    // plugin instance
    public static EconomyPlus plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        long before = System.currentTimeMillis();

        saveDefaultConfig();
        createMessagesConfig();

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("             §dEconomyPlus");
            Bukkit.getConsoleSender().sendMessage("              §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§f-> §cCan't find Vault!");
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomyPlus");
        Bukkit.getConsoleSender().sendMessage("              §aEnabling");
        Bukkit.getConsoleSender().sendMessage("§8");

        // Load database
        loadDatabase();

        // Load Economy and send the message
        loadEconomy();

        // Load Commands and send the message
        loadEvents();

        // Load Events and send the message
        loadCommands();

        new UpdateMessage().updater(92975);

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomyPlus");
        Bukkit.getConsoleSender().sendMessage("              §cDisabling");
        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");

        hook.offHook();
    }

    public void loadEconomy () {
        try {
            veco = new VEconomy(plugin);
            hook = new VHook();

            hook.onHook();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cError on hooking with Vault!");
            plugin.getServer().getPluginManager().disablePlugin(this);
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §aHooked with Vault!");
        }
    }

    public void loadDatabase() {
        try {
            this.db = new SQLite(this);
            this.db.load();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §bLoaded database!");
        }
    }

    public void loadEvents() {

    }

    public void loadCommands() {

    }

    public void loadMetrics() {

    }

    // Returns plugin instance
    public static EconomyPlus getInstance() {
        return plugin;
    }

    // Returns the database
    public Database getRDatabase() {
        return db;
    }

    // Controls if there's Vault installed
    private boolean setupEconomy() {
        return getServer().getPluginManager().isPluginEnabled("Vault");
    }

    // Vault economy instance
    public static Economy getEconomy() {
        if (econ == null) {
            return null;
        }
        return econ;
    }

    public FileConfiguration getMessages() {
        return this.messagesConfig;
    }

    private void createMessagesConfig() {
        messagesConfigFile = new File(getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            messagesConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
