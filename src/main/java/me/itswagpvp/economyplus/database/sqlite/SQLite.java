package me.itswagpvp.economyplus.database.sqlite;

import me.itswagpvp.economyplus.EconomyPlus;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class SQLite extends Database {
    String dbname;
    public SQLite() {
        dbname = "database";
    }

    // SQL Query
    // Prepare the table for the database
    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS data (" +
            "`player` varchar(32) NOT NULL," +
            "`moneys` double(32) NOT NULL," +
            "`bank` double(32) NOT NULL," +
            "PRIMARY KEY (`player`)" +
            ");";

    // Creates the SQL database file
    public Connection getSQLiteConnection() {
        File dataFolder = new File(EconomyPlus.plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if(connection!=null && !connection.isClosed()) return connection;
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    // Creates the table and tokens
    @Override
    public void load() {
        connection = getSQLiteConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        initialize();
    }
}
