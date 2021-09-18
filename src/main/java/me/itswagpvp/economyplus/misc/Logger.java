package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;

public class Logger {
    private static EconomyPlus plugin;

    public Logger(EconomyPlus plugin) {
        Logger.plugin = plugin;
    }

    public static java.util.logging.Logger getLogger(){
        return plugin.getLogger();
    }
}