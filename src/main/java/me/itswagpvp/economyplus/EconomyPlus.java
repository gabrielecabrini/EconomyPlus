package me.itswagpvp.economyplus;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.itswagpvp.economyplus.bank.commands.Bank;
import me.itswagpvp.economyplus.bank.menu.MenuListener;
import me.itswagpvp.economyplus.commands.*;
import me.itswagpvp.economyplus.dbStorage.mysql.MySQL;
import me.itswagpvp.economyplus.dbStorage.sqlite.SQLite;
import me.itswagpvp.economyplus.events.Join;
import me.itswagpvp.economyplus.hooks.HolographicDisplays;
import me.itswagpvp.economyplus.messages.DefaultFiles;
import me.itswagpvp.economyplus.messages.MessageUtils;
import me.itswagpvp.economyplus.messages.MessagesFile;
import me.itswagpvp.economyplus.metrics.bStats;
import me.itswagpvp.economyplus.misc.*;
import me.itswagpvp.economyplus.vault.VEconomy;
import me.itswagpvp.economyplus.vault.VHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class EconomyPlus extends JavaPlugin {

    // Database
    private static DatabaseType dbType = DatabaseType.Undefined;

    // Messages
    public static MessagesFile messagesType = MessagesFile.Undefined;

    // (holograms.yml)
    private File hologramFile;
    private FileConfiguration hologramConfig;

    // FileConfig (data.yml)
    private File ymlFile;
    private FileConfiguration ymlConfig;

    // Economy
    public static VEconomy veco;
    public static VHook hook;

    // BalTop
    public static Data data;

    // Plugin instance
    public static EconomyPlus plugin;

    Updater updater;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        long before = System.currentTimeMillis();

        saveDefaultConfig();

        plugin.getConfig().options().copyDefaults(true);

        createHologramConfig();

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
            Bukkit.getConsoleSender().sendMessage("              §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§f-> §cCan't find Vault!");
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §aEnabling");
        Bukkit.getConsoleSender().sendMessage("§8");

        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading core!");

        loadDatabase();

        loadEconomy();

        loadEvents();

        loadCommands();

        loadMessages();

        Bukkit.getConsoleSender().sendMessage("§8");
        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading hooks!");

        loadMetrics();

        loadHolograms();

        Bukkit.getConsoleSender().sendMessage("§f");
        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading placeholders:");

        loadPlaceholders();

        updater = Updater.getInstance(this);

        if (plugin.getDescription().getVersion() != plugin.getConfig().getString("Version")) {
            Bukkit.getConsoleSender().sendMessage("§f-> §eRemind to update the config.yml! Your config version is outdated!");
        }

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");

        if (dbType == DatabaseType.Undefined) {
            Bukkit.getConsoleSender().sendMessage("§c[EconomyPlus] Unable to start the plugin without a valid database option!");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        Bukkit.getConsoleSender().sendMessage("             §dEconomy§5Plus");
        Bukkit.getConsoleSender().sendMessage("              §cDisabling");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§f-> §cUnhooking from Vault");

        hook.offHook();

        Bukkit.getConsoleSender().sendMessage("§f-> §cClosing database connection");

        try {
            dbType.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");

    }

    private void loadEconomy() {
        try {
            veco = new VEconomy(plugin);
            hook = new VHook();

            hook.onHook();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fVault: §CError");
            e.printStackTrace();
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fVault: §6Hooked");
    }

    // Loads the SQLite database
    private void loadDatabase() {
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
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
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
            dbType = DatabaseType.Undefined;
        }
    }

    private void loadEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new Join(), this);
            Bukkit.getPluginManager().registerEvents(new Updater(this), this);
            Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fEvents: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fEvents: §aLoaded");
    }

    private void loadCommands() {
        try {

            getCommand("baltop").setExecutor(new BalTop());
            getCommand("baltop").setTabCompleter(new ConstructorTabCompleter());

            getCommand("economyplus").setExecutor(new Main());
            getCommand("economyplus").setTabCompleter(new ConstructorTabCompleter());

            getCommand("bal").setExecutor(new Bal());
            getCommand("bal").setTabCompleter(new ConstructorTabCompleter());

            getCommand("pay").setExecutor(new Pay());
            getCommand("pay").setTabCompleter(new ConstructorTabCompleter());

            getCommand("eco").setExecutor(new Eco());
            getCommand("eco").setTabCompleter(new ConstructorTabCompleter());

            getCommand("bank").setExecutor(new Bank());
            getCommand("bank").setTabCompleter(new ConstructorTabCompleter());

        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fCommands: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }
        Bukkit.getConsoleSender().sendMessage("   - §fCommands: §aLoaded");
    }

    // Loads the bStats metrics
    private void loadMetrics() {

        if (!getConfig().getBoolean("Metrics")) {
            return;
        }

        try {
            new bStats(this, 11565);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fbStats: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }
        Bukkit.getConsoleSender().sendMessage("   - §fbStats: §aLoaded");
    }

    private void loadHolograms() {

        if (!plugin.getConfig().getBoolean("Hooks.HolographicDisplays")) {
            return;
        }

        boolean useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

        if (useHolographicDisplays) {

            Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §aFound");

            if (getHologramConfig().getString("Hologram.BalTop.World") != null) {

                long refreshRate = plugin.getConfig().getLong("Baltop.Hologram.Refresh-Rate", 60) * 20L;

                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

                    for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
                        hologram.delete();
                    }

                    new HolographicDisplays().refreshHologram();
                }, 0L, refreshRate);
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("   - §fHolographicDisplays: §cCan't find the jar!");
        }

    }

    // Loads the placeholder for PlaceholderAPI or MVdWPlaceholderAPI
    private void loadPlaceholders() {

        // MVdWPlaceholderAPI
        new Utils().loadMVdWPlaceholderAPI();

        // PlaceholderAPI
        new Utils().loadPlaceholderAPI();
        Bukkit.getConsoleSender().sendMessage("§f");
    }

    private void loadMessages() {
        String messages = plugin.getConfig().getString("Language");
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

    public Data getData() {

        if (data == null) {
            data = new Data();
            new Data();
        }
        return data;
    }

    // Controls if there's Vault installed
    private boolean setupEconomy() {
        return getServer().getPluginManager().isPluginEnabled("Vault");
    }

    public String getMessage(String path) {
        if (!new DefaultFiles().getMessagesFile().isString(path)) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', new DefaultFiles().getMessagesFile().getString(path));
    }

    public FileConfiguration getHologramConfig() {
        return this.hologramConfig;
    }

    public void saveHologramConfig() {
        try {
            hologramConfig.save(hologramFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createHologramConfig() {
        hologramFile = new File(plugin.getDataFolder(), "holograms.yml");
        if (!hologramFile.exists()) {
            hologramFile.getParentFile().mkdirs();
            plugin.saveResource("holograms.yml", false);
        }

        hologramConfig = new YamlConfiguration();
        try {
            hologramConfig.load(hologramFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getYMLData() {
        return this.ymlConfig;
    }

    public void saveYMLConfig() {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createYMLStorage() {
        ymlFile = new File(plugin.getDataFolder(), "data.yml");
        if (!ymlFile.exists()) {
            hologramFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }

        loadYML();
    }

    private void loadYML() {
        ymlConfig = new YamlConfiguration();
        try {
            ymlConfig.load(ymlFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseType getDBType() {
        return dbType;
    }
}