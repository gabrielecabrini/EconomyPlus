package me.itswagpvp.economyplus;

import me.itswagpvp.economyplus.commands.*;
import me.itswagpvp.economyplus.metrics.bStats;
import me.itswagpvp.economyplus.database.local.Database;
import me.itswagpvp.economyplus.database.local.SQLite;
import me.itswagpvp.economyplus.events.Join;
import me.itswagpvp.economyplus.misc.ConstructorTabCompleter;
import me.itswagpvp.economyplus.misc.Data;
import me.itswagpvp.economyplus.misc.updater.UpdateMessage;
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

    private FileConfiguration messagesConfig;

    // Database
    private Database db;

    // Economy
    public static VEconomy veco;
    public static VHook hook;

    public static Data data;

    // plugin instance
    public static EconomyPlus plugin;
    public static Data data;

    public static String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
    public static int ver = Integer.parseInt(split[1]);

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        long before = System.currentTimeMillis();

        saveDefaultConfig();
        createMessagesConfig();

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

        Bukkit.getConsoleSender().sendMessage("§8");
        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading miscellaneous!");

        loadEvents();

        loadCommands();

        loadMetrics();

        loadPlaceholders();

        if (ver < 12) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cThe updater won't work under 1.12!");
        }

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");

        if (ver >= 12) {
            new UpdateMessage().updater(92975);
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

            String type = plugin.getConfig().getString("Database.Type");

            if (type.equalsIgnoreCase("H2")) {
                getRDatabase().getSQLiteConnection().close();
            }

            if (type.equalsIgnoreCase("MySQL")) {
                new MySQL().closeConnection();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");




    }

    public void loadEconomy () {
        try {
            veco = new VEconomy(plugin);
            hook = new VHook();

            hook.onHook();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fVault: §CError");
            e.printStackTrace();
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fVault: §6Hooked");
    }

    // Loads the SQLite database
    public void loadDatabase() {
        if (getConfig().getString("Database.Type").equalsIgnoreCase("MySQL")) {
            try {
                new MySQL().connect();
                new MySQL().createTable();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cError (MySQL)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }

            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (MySQL)");
        }

        if (getConfig().getString("Database.Type").equalsIgnoreCase("H2")) {
            try {
                this.db = new SQLite(this);
                this.db.load();
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §cError (SQLite)");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                return;
            }

            Bukkit.getConsoleSender().sendMessage("   - §fDatabase: §bLoaded (SQLite)");
        }
    }

    public void loadEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new Join(), this);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fEvents: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }

        Bukkit.getConsoleSender().sendMessage("   - §fEvents: §aLoaded");
    }

    public void loadCommands() {
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

        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fCommands: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }
        Bukkit.getConsoleSender().sendMessage("   - §fCommands: §aLoaded");
    }

    // Loads the bStats metrics
    public void loadMetrics() {

        if (!getConfig().getBoolean("Metrics")) {
            return;
        }

        try {
            new bStats(this, 11565);
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("   - §fMetrics: §cError");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }
        Bukkit.getConsoleSender().sendMessage("   - §fMetrics: §aLoaded");
    }

    public void loadPlaceholders() {

        // Messages

        Bukkit.getConsoleSender().sendMessage("§f");
        Bukkit.getConsoleSender().sendMessage("§f-> §cLoading placeholders:");

        // MVdWPlaceholderAPI
        new Utils().loadMVdWPlaceholderAPI();

        // PlaceholderAPI

        new Utils().loadPlaceholderAPI();
        Bukkit.getConsoleSender().sendMessage("§f");
    }

    // Returns plugin instance
    public static EconomyPlus getInstance() {
        return plugin;
    }

    public Data getData() {

        if (data == null) {
            data = new Data();
            new Data();
        }
        return data;
    }

    // Returns the database
    public Database getRDatabase() {
        return db;
    }

    // Controls if there's Vault installed
    private boolean setupEconomy() {
        return getServer().getPluginManager().isPluginEnabled("Vault");
    }

    public FileConfiguration getMessagesFile() {
        return this.messagesConfig;
    }

    public void createMessagesConfig() {
        // Messages file
        File messagesConfigFile = new File(getDataFolder(), "messages.yml");
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

    public String getMessage(String path) {
        if (!getMessagesFile().isString(path)) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', getMessagesFile().getString(path));
    }

    public Data getData() {

        if (data == null) {
            data = new Data();
            new Data();
        }
        return data;
    }

}
