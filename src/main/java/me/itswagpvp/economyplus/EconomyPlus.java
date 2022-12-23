package me.itswagpvp.economyplus;

import me.itswagpvp.economyplus.bank.commands.Bank;
import me.itswagpvp.economyplus.bank.other.InterestsManager;
import me.itswagpvp.economyplus.commands.*;
import me.itswagpvp.economyplus.database.CacheManager;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;
import me.itswagpvp.economyplus.hooks.PlaceholderAPI;
import me.itswagpvp.economyplus.hooks.holograms.HolographicDisplays;
import me.itswagpvp.economyplus.listener.PlayerHandler;
import me.itswagpvp.economyplus.messages.Messages;
import me.itswagpvp.economyplus.metrics.bStats;
import me.itswagpvp.economyplus.misc.*;
import me.itswagpvp.economyplus.vault.VEconomy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static me.itswagpvp.economyplus.messages.Messages.getMessageConfig;

public class EconomyPlus extends JavaPlugin {

    public boolean REQUIRE_BASIC_PERMISSIONS = getConfig().getBoolean("Require-Basic-Permissions", true);

    public boolean PLUGIN_UPDATER = getConfig().getBoolean("Updater.Plugin-Updater", true);
    public boolean SAVE_NAMES = getConfig().getBoolean("Invalid-Users.Save-Names", true);
    public static double PLUGIN_VERSION;
    public static double CONFIG_VERSION;

    public static EconomyPlus plugin; // Plugin instance

    public static String lang = "EN"; // Language

    public static BalTopManager balTopManager; // BalTop

    public static boolean debugMode; // Debug mode

    public static FileConfiguration ymlConfig;

    private static DatabaseType dbType = DatabaseType.UNDEFINED; // Database

    private static StorageMode storageMode = StorageMode.UNDEFINED; // Storage Type (UUID,Nickname)
    long before; // Plugin loading time

    String vault = ""; // Vault message

    private File ymlFile;

    // Returns the DatabaseType (MYSQL/H2/YAML/Undefined)
    public static DatabaseType getDBType() {
        return dbType;
    }

    // Returns the StorageMode (NICKNAME/UUID)
    public static StorageMode getStorageMode() {
        return storageMode;
    }

    // Made for /ep convert
    public void setStorageMode(String newStorageMode) {
        storageMode = StorageMode.valueOf(newStorageMode);
        getConfig().set("Database.Mode", newStorageMode);
        saveConfig();
    }

    //Logger
    public void pluginLog(String value) {
        Bukkit.getConsoleSender().sendMessage("[EconomyPlus] " + ChatColor.translateAlternateColorCodes('&', value));
    }

    private static String configUpdate = null;
    public void loadDefaultConfig() {

        File file = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // file doesn't exist so create new config & return
        if (!file.exists()) {
            saveDefaultConfig();
            return;
        }

        // Config version is same as plugin version OR config updater is disabled
        double version = CONFIG_VERSION;
        if (CONFIG_VERSION == PLUGIN_VERSION || !getConfig().getBoolean("Updater.Config-Updater")) {
            saveDefaultConfig();
            return;
        }

        // save configuration file to folder configs
        try {
            config.save(new File(plugin.getDataFolder() + File.separator + "configs" + File.separator + CONFIG_VERSION + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // save config values
        Map<String, Object> old = new HashMap<>();
        for (String value : config.getConfigurationSection("").getKeys(true)) {
            if (!value.equalsIgnoreCase("Version")) {
                if (getConfig().getConfigurationSection(value) == null) {
                    old.put(value, getConfig().get(value));
                }
            }
        }

        // delete config and create new one
        file.delete();
        saveDefaultConfig();
        reloadConfig();

        // getConfig() is new config
        // config is old config

        // update new config
        int total = old.size();
        for (String value : getConfig().getConfigurationSection("").getKeys(true)) {
            if (old.get(value) != null) {
                if (!value.equalsIgnoreCase("Version")) { // value is not version
                    getConfig().set(value, old.get(value)); // set value to new config
                    old.remove(value); // remove from map
                }
            }
        }

        //
        saveConfig();
        reloadConfig();
        //

        // get if there are any new config options
        Map<Object, Object> newoptions = new HashMap<>();
        for (String value : getConfig().getConfigurationSection("").getKeys(true)) {
            if (getConfig().getConfigurationSection(value) == null) {
                if (config.get(value) == null) {
                    newoptions.put(value, getConfig().get(value));
                }
            }
        }

        // messaging

        int failed = old.size();
        String converted = (total - old.size()) + "/" + total;

        CONFIG_VERSION = getConfig().getDouble("Version", PLUGIN_VERSION);
        if (version > PLUGIN_VERSION) {
            configUpdate = "§f-> §aYour config.yml was updated.\n   - §fVersion: §a" + CONFIG_VERSION + " §f<- §e" + version;
        } else if (version < PLUGIN_VERSION) {
            configUpdate = "§f-> §aYour config.yml was updated.\n   - §fVersion: §e" + version + " §f-> §a" + CONFIG_VERSION;
        } else if (version == PLUGIN_VERSION) {
            configUpdate = "§f-> §aYour config.yml was verified.\n   - §fVersion: §a" + CONFIG_VERSION;
        }

        if (failed == 0) {
            configUpdate = configUpdate + "\n   - §fConverted: §a" + converted;
        } else {
            configUpdate = configUpdate + "\n   - §fConverted: §c" + converted + "\n   - §fRemoved-Options: §c[" + failed + "]";
            for (Map.Entry<String, Object> value : old.entrySet()) {
                configUpdate = configUpdate + "\n     - §f" + value.getKey() + ": §c" + value.getValue();
            }
        }

        if (!newoptions.isEmpty()) {
            configUpdate = configUpdate + "\n   - §fNew-Options: §a[" + newoptions.size() + "]";
            for (Map.Entry<Object, Object> value : newoptions.entrySet()) {
                if (value.getValue().toString().equalsIgnoreCase("true")) {
                    configUpdate = configUpdate + "\n     - §f" + value.getKey() + ": §a" + value.getValue();
                } else if (value.getValue().toString().equalsIgnoreCase("false")) {
                    configUpdate = configUpdate + "\n     - §f" + value.getKey() + ": §c" + value.getValue();
                } else {
                    configUpdate = configUpdate + "\n     - §f" + value.getKey() + ": §e" + value.getValue();
                }
            }
        }

        configUpdate = configUpdate + "\n   - §bConfiguration was saved.";


    }

    public void onLoad() { // Plugin startup logic

        before = System.currentTimeMillis();

        plugin = this;

        PLUGIN_VERSION = Double.parseDouble(getDescription().getVersion());
        CONFIG_VERSION = getConfig().getDouble("Version", PLUGIN_VERSION);

        loadDefaultConfig();

        if (getConfig().getBoolean("Debug-Mode", false)) {
            debugMode = true;
            getLogger().setLevel(Level.FINEST);
        }

        getConfig().options().copyDefaults(true);

        new StorageManager().createStorageConfig();

        if (!setupEconomy()) {
            vaultError("Can't find Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadDatabase();

        loadEconomy();

        if (dbType == DatabaseType.UNDEFINED) {
            Bukkit.getConsoleSender().sendMessage("§c[EconomyPlus] Unable to start the plugin without a valid database option!");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onEnable() {

        loadPlaceholderAPI();

        double configver = Double.parseDouble(getConfig().getString("Version")); // Configuration Version
        double pluginver = Double.parseDouble(getDescription().getVersion()); // Plugin Version

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("             §aEnabled §d" + pluginver);
        Bukkit.getConsoleSender().sendMessage("§8");

        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading core:");

        Bukkit.getConsoleSender().sendMessage("   - §fStorage-Mode: §a" + storageMode.toString());

        Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (" + dbType.toString().replace("H2", "SQLite") + ")");

        enableDatabase();

        Bukkit.getConsoleSender().sendMessage("   - §fVault: " + vault);

        loadCommands();

        loadMessages();

        loadMetrics();

        sanityCheck();

        loadEvents();

        Bukkit.getConsoleSender().sendMessage("§8");

        boolean bplaceholder = plugin.getConfig().getBoolean("Hooks.PlaceholderAPI", true);
        boolean bholograms = plugin.getConfig().getBoolean("Hooks.HolographicDisplays", true);
        if (bplaceholder || bholograms) { /* If atleast one of the plugins (to hook into) is set to true in config */
            Bukkit.getConsoleSender().sendMessage("§f-> §cLoading hooks:");
            if (bholograms) { loadHolograms(); }
            if (bplaceholder) { Bukkit.getConsoleSender().sendMessage("   - §fPlaceholderAPI: " + placeholder); }
            Bukkit.getConsoleSender().sendMessage("§f");
        }

        if (configUpdate != null) { // config was recently updated
            String configUpdateSplit[] = configUpdate.split("\n");
            for (String configmsg : configUpdateSplit) {
                Bukkit.getConsoleSender().sendMessage(configmsg);
            }
            Bukkit.getConsoleSender().sendMessage("");
            configUpdate = null;
        }

        if (configver != pluginver) { /* Config is not updated */

            int outdated; /* Amount of version the plugin is outdated or over updatedby */

            Bukkit.getConsoleSender().sendMessage("§f-> §eYour config.yml is outdated!");

            if (configver > pluginver) { //ahead versions (could auto fix maybe but prob not?)
                outdated = Integer.parseInt(String.valueOf(Math.round((configver - pluginver) / 0.1)).replace(".0", ""));
                Bukkit.getConsoleSender().sendMessage("   - §fConfig: " + "§c" + configver + " (" + outdated + " versions ahead" + ")");
            } else { //behind versions (outdated)
                outdated = Integer.parseInt(String.valueOf(Math.round((pluginver - configver) / 0.1)).replace(".0", ""));
                Bukkit.getConsoleSender().sendMessage("   - §fConfig: " + "§c" + configver + " (" + outdated + " versions behind" + ")");
            }

            Bukkit.getConsoleSender().sendMessage("   - §fPlugin: " + "§d" + getDescription().getVersion());
            Bukkit.getConsoleSender().sendMessage("");

        }

        Updater.check();

        boolean bank = plugin.getConfig().getBoolean("Bank.Enabled", true);
        boolean interest = plugin.getConfig().getBoolean("Bank.Interests.Enabled", true);
        if (bank && interest) { new InterestsManager().startBankInterests(); } /* If bank and interest is enabled in config start the interest timer */

        PlayerHandler.loadUsernames();

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");

        if (pluginver >= pluginver) {
            Bukkit.getConsoleSender().sendMessage("[EconomyPlus] You are up to date! §d(v" + pluginver + ")");
        }

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §cDisabling");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§f-> §cStopping threads...");
        ThreadsUtils.stopAllThreads();

        Bukkit.getConsoleSender().sendMessage("§f-> §cClosing database connection");

        try {
            dbType.close();
            Bukkit.getScheduler().cancelTasks(this);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");

    }

    // Hook into VaultEconomy
    private void loadEconomy() {

        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VEconomy(this), this, ServicePriority.Normal);
        } catch (ClassNotFoundException e) {
            vault = "§CError\n" + e.getMessage();
            return;
        }

        vault = "§6Hooked";

    }

    private void loadDatabase() {

        // Select how the plugin needs to storage the player datas
        if (getConfig().getString("Database.Mode", "UUID").equalsIgnoreCase("UUID")) {
            storageMode = StorageMode.UUID;
        } else if (getConfig().getString("Database.Mode", "UUID").equalsIgnoreCase("NICKNAME")) {
            storageMode = StorageMode.NICKNAME;
        } else {
            storageMode = StorageMode.UUID;
        }

        // Detect and set the type of database
        if (getConfig().getString("Database.Type").equalsIgnoreCase("MySQL")) {
            try {
                new MySQL().connect();
                new MySQL().createTable();
                new MySQL().updateTable();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§fDatabase: §cError (MySQL)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }
            dbType = DatabaseType.MySQL;
        } else if (getConfig().getString("Database.Type").equalsIgnoreCase("H2")) {
            try {
                new SQLite().load();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§fDatabase: §cError (SQLite)");
                e.printStackTrace();
                return;
            }

            dbType = DatabaseType.H2;
        } else if (getConfig().getString("Database.Type").equalsIgnoreCase("YAML")) {
            try {
                createYMLStorage();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§fDatabase: §cError (YAML)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }

            dbType = DatabaseType.YAML;
        } else {
            dbType = DatabaseType.UNDEFINED;
        }

    }

    private void enableDatabase() {
        // Load the cache for the database - Vault API
        if (dbType == DatabaseType.MySQL) {
            new CacheManager().cacheOnlineDatabase();
            long period = getConfig().getLong("Database.Cache.MySQL", 10) * 20;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> new CacheManager().cacheOnlineDatabase(), 120, period);
        } else {
            new CacheManager().cacheLocalDatabase();
            new CacheManager().startAutoSave();
        }

        Bukkit.getConsoleSender().sendMessage("     - §fCaching accounts...");
    }

    private void loadEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new PlayerHandler(), this);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §cError loading listeners");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    private void loadCommands() {

        try {

            getCommand("baltop").setExecutor(new BalTop());
            getCommand("baltop").setTabCompleter(new TabCompleterLoader());

            getCommand("economyplus").setExecutor(new Main());
            getCommand("economyplus").setTabCompleter(new TabCompleterLoader());

            getCommand("bal").setExecutor(new Bal());
            getCommand("bal").setTabCompleter(new TabCompleterLoader());

            getCommand("pay").setExecutor(new Pay());
            getCommand("pay").setTabCompleter(new TabCompleterLoader());

            getCommand("eco").setExecutor(new Eco());
            getCommand("eco").setTabCompleter(new TabCompleterLoader());

            getCommand("bank").setExecutor(new Bank());

            if (getConfig().getBoolean("Bank.Enabled")) {
                getCommand("bank").setTabCompleter(new TabCompleterLoader());
            }

            getCommand("paytoggle").setExecutor(new PayToggle());
            getCommand("paytoggle").setTabCompleter(new TabCompleterLoader());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fCommands: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }
        Bukkit.getConsoleSender().sendMessage("   - §fCommands: §aLoaded");
    }

    // Loads the bStats metrics
    private void loadMetrics() {

        if (!getConfig().getBoolean("Metrics")) return;

        try {
            new bStats(this, 11565);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §cError loading bStats");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    private static String placeholder;

    private void loadPlaceholderAPI() {

        if (!plugin.getConfig().getBoolean("Hooks.PlaceholderAPI")) return;

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            placeholder = "§cCan't find the jar!";
            return;
        }

        try {
            new PlaceholderAPI(plugin).register();
        } catch (Exception e) {
            placeholder = "§cError!\n" + e.getMessage();
        } finally {
            placeholder = "§aHooked!";
        }

    }

    private void loadHolograms() {

        if (!getConfig().getBoolean("Hooks.HolographicDisplays")) return;

        if (getServer().getPluginManager().getPlugin("HolographicDisplays") == null) {
            Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §cCan't find the jar!");
            return;
        }

        try {

            if (new StorageManager().getStorageConfig().getString("Hologram.BalTop.World") != null) {

                Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §aHooked!");

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> new HolographicDisplays().createHologram(), 1);

                return;
            }

        } catch (Exception e) {

            Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §cError!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;

        }

        Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §aHooked!");

    }

    private void loadMessages() {

        Messages.load();

        String messages = getConfig().getString("Language");
        if (!(Messages.getMessageConfig(messages.toUpperCase()) == null)) {
            lang = messages.toUpperCase();
            Bukkit.getConsoleSender().sendMessage("   - §fMessages: §a" + lang);
        } else {
            Bukkit.getConsoleSender().sendMessage("   - §fMessages: §cInvalid file! (" + messages + "), using EN");
        }

    }

    public BalTopManager getBalTopManager() {

        if (balTopManager == null) {
            balTopManager = new BalTopManager();
            new BalTopManager();
        }
        return balTopManager;
    }

    // Controls if there's Vault installed
    private boolean setupEconomy() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }

    private void sanityCheck() {
        if (getServer().getPluginManager().isPluginEnabled("Vault")) return;

        vaultError("Vault is not enabled.");
        getServer().getPluginManager().disablePlugin(this);
    }

    private void vaultError(String specific) {
        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §cDisabling");
        Bukkit.getConsoleSender().sendMessage("§8");
        Bukkit.getConsoleSender().sendMessage("§f-> §c" + specific);
        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
    }

    // Get the string from /messages/file.yml and format it with color codes (hex for 1.16+)
    public String getMessage(String path) {

        if (getMessageConfig(lang).get(path) == null) {
            return path;
        }

        String rawMessage = getMessageConfig(lang).getString(path);

        assert rawMessage != null;

        //TODO Keep this only for implementing #isMessageEnabled
        if (rawMessage.equalsIgnoreCase("none")) {
            return "";
        }

        if (Utils.supportRGBColors()) {
            String hexMessage = Utils.hexColor(rawMessage);
            return ChatColor.translateAlternateColorCodes('&', hexMessage);
        }

        return ChatColor.translateAlternateColorCodes('&', rawMessage);
    }

    public boolean isMessageEnabled(String path) {
        if (!getMessageConfig(lang).isString(path)) {
            return false;
        }

        return !getMessageConfig(lang).getString(path).equalsIgnoreCase("none");
    }

    // Returns data.yml if DatabaseType is YAML
    public FileConfiguration getYMLData() {
        return ymlConfig;
    }

    // Safe-save data.yml
    public void saveYMLConfig() {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create data.yml if DatabaseType is YAML
    public void createYMLStorage() {
        ymlFile = new File(getDataFolder(), "data.yml");
        if (!ymlFile.exists()) {
            ymlFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        loadYML();
    }

    // Load the updated data.yml
    private void loadYML() {
        ymlConfig = new YamlConfiguration();
        try {
            ymlConfig.load(ymlFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}