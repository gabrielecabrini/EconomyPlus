package me.itswagpvp.economyplus;

import me.itswagpvp.economyplus.metrics.bStats;
import me.itswagpvp.economyplus.commands.Bal;
import me.itswagpvp.economyplus.commands.Eco;
import me.itswagpvp.economyplus.commands.Main;
import me.itswagpvp.economyplus.commands.Pay;
import me.itswagpvp.economyplus.database.Database;
import me.itswagpvp.economyplus.database.SQLite;
import me.itswagpvp.economyplus.events.Join;
import me.itswagpvp.economyplus.misc.ConstructorTabCompleter;
import me.itswagpvp.economyplus.misc.updater.UpdateMessage;
import me.itswagpvp.economyplus.hooks.PlaceholderAPI;
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

public final class EconomyPlus extends JavaPlugin {

    private FileConfiguration messagesConfig;

    // Database
    private Database db;

    // Economy
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

        loadDatabase();

        loadEconomy();

        loadEvents();

        loadCommands();

        loadMetrics();

        loadPlaceholders();

        Bukkit.getConsoleSender().sendMessage("§8+---------------[§a " + (System.currentTimeMillis() - before) + "ms §8]-------------+");

        new UpdateMessage().updater(92975);
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

    // Loads the SQLite database
    public void loadDatabase() {
        try {
            this.db = new SQLite(this);
            this.db.load();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §bDatabase loaded! (database.db)");
        }
    }

    public void loadEvents() {
        try {
            Bukkit.getPluginManager().registerEvents(new Join(), this);
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cError loading the events!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §aEvents loaded");
        }
    }

    public void loadCommands() {
        try {

            getCommand("economyplus").setExecutor(new Main());
            getCommand("economyplus").setTabCompleter(new ConstructorTabCompleter());

            getCommand("bal").setExecutor(new Bal());
            getCommand("bal").setTabCompleter(new ConstructorTabCompleter());

            getCommand("pay").setExecutor(new Pay());
            getCommand("pay").setTabCompleter(new ConstructorTabCompleter());

            getCommand("eco").setExecutor(new Eco());
            getCommand("eco").setTabCompleter(new ConstructorTabCompleter());

        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cError loading the commands!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §aCommands loaded");
        }
    }

    // Loads the bStats metrics
    public void loadMetrics() {

        if (!getConfig().getBoolean("Metrics")) {
            return;
        }

        try {
            new bStats(this, 11565);
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cError on loading the metrics:");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        } finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §aMetrics loaded!");
        }
    }

    // Hooks with placeholderapi
    public void loadPlaceholders() {
        if (!getConfig().getBoolean("Hooks.PlaceholderAPI")) {
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cCould not find PlaceholderAPI! Install it to use placeholders!");
            return;
        }

        try {
            new PlaceholderAPI(this).register();
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§f-> §cError on hooking with PlaceholderAPI!");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            return;
        }finally {
            Bukkit.getConsoleSender().sendMessage("§f-> §aHooked with PlaceholderAPI!");
        }
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

}
