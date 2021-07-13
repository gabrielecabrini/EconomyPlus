package me.itswagpvp.economyplus.storage.sqlite;

import me.itswagpvp.economyplus.EconomyPlus;

import java.util.logging.Level;

public class Errors {
    public static String sqlConnectionExecute(){
        return "Couldn't execute MySQL statement: ";
    }

    public static void close(EconomyPlus plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
