package me.itswagpvp.economyplus.database.sqlite;

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

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public abstract class Database {

    Connection connection;

    // The name of the table we created back in SQLite class.
    public String table = "data";

    public abstract Connection getSQLiteConnection();

    public abstract void load();

    public void initialize () {
        connection = getSQLiteConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();

            updateTable();

            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public void updateTable() {
        String sql = "ALTER TABLE " + table + " ADD COLUMN bank DOUBLE";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.toString().contains("Duplicate column name 'bank'")) {
                return;
            }

            if (e.toString().contains("duplicate column name: bank")) {
                return;
            }
            e.printStackTrace();
        }
    }

    // Retrieve the balance of the player
    public double getTokens(String player) {

        CompletableFuture<Double> getDouble = CompletableFuture.supplyAsync(() -> {
            Connection conn = getSQLiteConnection();
            try (
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+player+"';");
                    ResultSet rs = ps.executeQuery()
            ) {
                while(rs.next()){
                    if(rs.getString("player").equalsIgnoreCase(player)){
                        return rs.getDouble("moneys");
                    }
                }
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
            return 0.00;
        });

        try {
            return getDouble.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 0.00;
    }

    // Save the balance to the player's database
    public void setTokens (String player, double tokens) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Connection conn = getSQLiteConnection();
            try (
                    PreparedStatement ps = conn.prepareStatement("REPLACE INTO " + table + " (player,moneys,bank) VALUES(?,?,?)")
            ){

                ps.setString(1, player);

                ps.setDouble(2, tokens);

                ps.setDouble(3, getBank(player));

                ps.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
        });
    }

    // Retrieve the bank of the player
    public double getBank (String player) {

        CompletableFuture<Double> getDouble = CompletableFuture.supplyAsync(() -> {
            Connection conn = getSQLiteConnection();
            try (
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+player+"';");
                    ResultSet rs = ps.executeQuery()
            ) {
                while(rs.next()){
                    if(rs.getString("player").equalsIgnoreCase(player)){
                        return rs.getDouble("bank");
                    }
                }
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
            return 0.00;
        });

        try {
            return getDouble.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 0.00;
    }

    // Save the balance to the player's database
    public void setBank (String player, double tokens) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Connection conn = getSQLiteConnection();
            try (
                    PreparedStatement ps = conn.prepareStatement("REPLACE INTO " + table + " (player,moneys,bank) VALUES(?,?,?)")
            ){

                ps.setString(1, player);

                ps.setDouble(2, getTokens(player));

                ps.setDouble(3, tokens);

                ps.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
        });
    }

    // Gets the list of the players in the database
    public List<String> getList () {
        CompletableFuture<List<String>> getList = CompletableFuture.supplyAsync(() -> {
            Connection conn = getSQLiteConnection();
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

    // Remove a user (UUID/NICKNAME) from the database
    public void removeUser(String user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Connection conn = getSQLiteConnection();
            String sql = "DELETE FROM " + table + " where player = '" + user + "'";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Create a default player
    public boolean createPlayer(String player) {
        setTokens(player, plugin.getConfig().getDouble("Starting-Balance"));
        setBank(player, plugin.getConfig().getDouble("Starting-Bank-Balance"));
        return true;
    }

    // Closes the database connection
    public void close(PreparedStatement ps, ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
        }
    }
}