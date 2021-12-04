package me.itswagpvp.economyplus.database.sqlite;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Logger {
    public static java.util.logging.Logger getLogger(){
        return plugin.getLogger();
    }
}