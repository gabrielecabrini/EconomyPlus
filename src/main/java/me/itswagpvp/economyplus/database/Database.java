package me.itswagpvp.economyplus.database;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public abstract class Database {

    EconomyPlus plugin;
    Connection connection;

    // The name of the table we created back in SQLite class.
    public String table = "data";
    public Database(EconomyPlus instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    public double getTokens(String player) {
        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+player+"';");
                ResultSet rs = ps.executeQuery()
        ) {
            while(rs.next()){
                if(rs.getString("player").equalsIgnoreCase(player)){ // Tell database to search for the player you sent into the method. e.g getTokens(sam) It will look for sam.
                    return rs.getDouble("moneys"); // Return the players amount of moneys. If you wanted to get total (just a random number for an example for you guys) You would change this to total!
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        return 0;

    }

    // Now we need methods to save things to the database
    public void setTokens(String player, double tokens) {
        Bukkit.getScheduler().runTaskAsynchronously(EconomyPlus.getInstance(), () -> {
            Connection conn = getSQLConnection();
            try (
                    PreparedStatement ps = conn.prepareStatement("REPLACE INTO " + table + " (player,moneys) VALUES(?,?)")
            ){

                ps.setString(1, player);


                ps.setDouble(2, tokens);

                ps.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
            }
        });
    }

    // Gets the list of the players in the database
    public List<String> getList () {
        CompletableFuture<List<String>> getList = CompletableFuture.supplyAsync(() -> {
            Connection conn = getSQLConnection();
            List<String> list = new ArrayList<>();
            try (
                    PreparedStatement ps = conn.prepareStatement("SELECT player FROM 'data'");
                    ResultSet rs = ps.executeQuery()
            ) {

                while (rs.next()) {
                    list.add(rs.getString("player"));
                }

                return list;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return list;
        });

        try {
            return getList.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    // Closes the database connection
    public void close(PreparedStatement ps, ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Errors.close(plugin, ex);
        }
    }
}