package me.itswagpvp.economyplus;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.itswagpvp.economyplus.bank.commands.Bank;
import me.itswagpvp.economyplus.bank.other.InterestsManager;
import me.itswagpvp.economyplus.commands.*;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.DatabaseType;
import me.itswagpvp.economyplus.database.misc.StorageMode;
import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;
import me.itswagpvp.economyplus.events.Join;
import me.itswagpvp.economyplus.hooks.PlaceholderLoader;
import me.itswagpvp.economyplus.hooks.holograms.HolographicDisplays;
import me.itswagpvp.economyplus.messages.DefaultFiles;
import me.itswagpvp.economyplus.messages.MessageUtils;
import me.itswagpvp.economyplus.messages.MessagesFile;
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

public final class EconomyPlus extends JavaPlugin {

    // Database
    private static DatabaseType dbType = DatabaseType.UNDEFINED;
    private static StorageMode storageMode = StorageMode.UNDEFINED;

    // YAML Database (data.yml)
    private File ymlFile;
    private FileConfiguration ymlConfig;

    // Messages
    public static MessagesFile messagesType = MessagesFile.UNDEFINED;

    // BalTop
    public static BalTopManager balTopManager;

    // Plugin instance
    public static EconomyPlus plugin;

    // Debug mode
    public static boolean debugMode;

    Updater updater;

    @Override
    public void onEnable() {
        // Plugin startup logic
        long before = System.currentTimeMillis();

        plugin = this;

        saveDefaultConfig();

        if (getConfig().getBoolean("Debug-Mode", false)) debugMode = true;

        plugin.getConfig().options().copyDefaults(true);

        new StorageManager().createStorageConfig();

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
            Bukkit.getConsoleSender().sendMessage("              §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§f-> §cCan't find Vault!");
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §aEnabling");
        Bukkit.getConsoleSender().sendMessage("§8");

        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading core:");

        loadDatabase();

        loadEconomy();

        loadEvents();

        loadCommands();

        loadMessages();

        Bukkit.getConsoleSender().sendMessage("§8");
        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading hooks:");

        loadMetrics();

        loadHolograms();

        loadPlaceholders();

        updater = Updater.getInstance(this);

        if (!plugin.getDescription().getVersion().equals(plugin.getConfig().getString("Version"))) {
            Bukkit.getConsoleSender().sendMessage("§f-> §eRemind to update config.yml! Your config version is outdated!");
        }

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");

        if (dbType == DatabaseType.UNDEFINED) {
            Bukkit.getConsoleSender().sendMessage("§c[EconomyPlus] Unable to start the plugin without a valid database option!");
            getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §cDisabling");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§f-> §cStopping threads...");
        ThreadsUtils.stopAllThreads();

        Bukkit.getConsoleSender().sendMessage("§f-> §cClosing database connection");

        try {
            dbType.close();
            Bukkit.getScheduler().cancelTasks(plugin);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");

    }

    // Hook into VaultEconomy
    private void loadEconomy() {

        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VEconomy(plugin), plugin, ServicePriority.Normal);
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("   - §fVault: §CError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fVault: §6Hooked");

    }

    private void loadDatabase() {

        // Select how the plugin needs to storage the player datas
        if (getConfig().getString("Database.Mode", "NICKNAME").equalsIgnoreCase("UUID")) {
            storageMode = StorageMode.UUID;
            Bukkit.getConsoleSender().sendMessage("   - §fStorage-Mode: §aUUID");
        } else if (getConfig().getString("Database.Mode", "NICKNAME").equalsIgnoreCase("NICKNAME")) {
            storageMode = StorageMode.NICKNAME;
            Bukkit.getConsoleSender().sendMessage("   - §fStorage-Mode: §aNICKNAME");
        } else {
            storageMode = StorageMode.UUID;
            Bukkit.getConsoleSender().sendMessage("   - §fStorage-Mode: §aUUID");
        }

        // Detect and set the type of database
        if (getConfig().getString("Database.Type").equalsIgnoreCase("MySQL")) {
            try {
                new MySQL().connect();
                new MySQL().createTable();
                new MySQL().updateTable();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cError (MySQL)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }
            dbType = DatabaseType.MySQL;
            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (MySQL)");
        } else if (getConfig().getString("Database.Type").equalsIgnoreCase("H2")) {
            try {
                new SQLite().load();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cError (SQLite)");
                e.printStackTrace();
                return;
            }

            dbType = DatabaseType.H2;
            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (SQLite)");
        } else if (getConfig().getString("Database.Type").equalsIgnoreCase("YAML")) {
            try {
                createYMLStorage();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cError (YAML)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }

            dbType = DatabaseType.YAML;
            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (YAML)");
        } else {
            String type = plugin.getConfig().getString("Database.Type");
            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cInvalid database type: " + type);
            dbType = DatabaseType.UNDEFINED;
        }

        // Load the cache for the database - Vault API
        if (dbType == DatabaseType.MySQL) {
            new CacheManager().cacheOnlineDatabase();
            long period = plugin.getConfig().getLong("Database.Cache.MySQL", 10) * 20;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> new CacheManager().cacheOnlineDatabase(), 120, period);
        } else {
            new CacheManager().cacheLocalDatabase();
            new CacheManager().startAutoSave();
        }

        Bukkit.getConsoleSender().sendMessage("     - §fCaching accounts...");

        new InterestsManager().startBankInterests();
    }

    private void loadEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new Join(), this);
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

            if (plugin.getConfig().getBoolean("Bank.Enabled")) {
                getCommand("bank").setExecutor(new Bank());
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

    private void loadHolograms() {

        if (!plugin.getConfig().getBoolean("Hooks.HolographicDisplays")) return;

        boolean useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

        if (useHolographicDisplays) {

            Bukkit.getConsoleSender().sendMessage("   - HolographicDisplays: §aDone!");

            if (new StorageManager().getStorageConfig().getString("Hologram.BalTop.World") != null) {

                new HolographicDisplays().createHologram();
                
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §cCan't find the jar!");
        }

    }

    // Loads the placeholder for PlaceholderAPI
    private void loadPlaceholders() {

        // PlaceholderAPI
        new PlaceholderLoader().loadPlaceholderAPI();
        Bukkit.getConsoleSender().sendMessage("§f");
    }

    private void loadMessages() {
        String messages = plugin.getConfig().getString("Language", "EN");
        try {

            messagesType = MessagesFile.valueOf(messages);

            new MessageUtils().initialize();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fMessages: §cInvalid file! (" + messages + "), using EN");
            messagesType = MessagesFile.EN;
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fMessages: §aLoaded " + messages + ".yml file!");

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
        return getServer().getPluginManager().isPluginEnabled("Vault");
    }

    // Get the string from /messages/file.yml and format it with color codes (hex for 1.16+)
    public String getMessage(String path) {
        if (!new DefaultFiles().getMessagesFile().isString(path)) {
            return path;
        }

        String rawMessage = new DefaultFiles().getMessagesFile().getString(path);

        assert rawMessage != null;

        //TODO Keep this only for implementing #isMessageEnabled
        if (rawMessage.equalsIgnoreCase("none")) {
            return "";
        }

        if (Utils.supportHexColors()) {
            String hexMessage = Utils.hexColor(rawMessage);
            return ChatColor.translateAlternateColorCodes('&', hexMessage);
        }

        return ChatColor.translateAlternateColorCodes('&', rawMessage);
    }

    public boolean isMessageEnabled(String path) {
        if (!new DefaultFiles().getMessagesFile().isString(path)) {
            return false;
        }

        return !new DefaultFiles().getMessagesFile().getString(path).equalsIgnoreCase("none");
    }

    // Returns data.yml if DatabaseType is YAML
    public FileConfiguration getYMLData() {
        return this.ymlConfig;
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
        ymlFile = new File(plugin.getDataFolder(), "data.yml");
        if (!ymlFile.exists()) {
            ymlFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
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
        plugin.getConfig().set("Database.Mode", newStorageMode);
        plugin.saveConfig();
    }

}