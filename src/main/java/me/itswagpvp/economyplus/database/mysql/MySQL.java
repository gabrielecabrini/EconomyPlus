package me.itswagpvp.economyplus.database.mysql;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class MySQL {

    final String user = plugin.getConfig().getString("Database.User");
    final String password = plugin.getConfig().getString("Database.Password");
    final String host = plugin.getConfig().getString("Database.Host");
    final String port = plugin.getConfig().getString("Database.Port");
    final String database = plugin.getConfig().getString("Database.Database");
    final String table = plugin.getConfig().getString("Database.Table");
    final boolean autoReconnect = plugin.getConfig().getBoolean("Database.AutoReconnect");
    final boolean useSSL = plugin.getConfig().getBoolean("Database.useSSL", false);

    final String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=" + autoReconnect + "&useSSL=" + useSSL + "&characterEncoding=utf8";

    static Connection connection;

    // Connect to the database
    public void connect() {
        try {

            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    // Close the database connection if not null
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {

                connection.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE " + table + " ("
                + "player VARCHAR(45) NOT NULL,"
                + "moneys DOUBLE NOT NULL,"
                + "bank DOUBLE NOT NULL,"
                + "PRIMARY KEY (player))";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.toString().contains("Table '" + table + "' already exists")) {
                return;
            }
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    // Retrieve the balance of the player
    public double getTokens(String player) {

        CompletableFuture<Double> getDouble = CompletableFuture.supplyAsync(() -> {

            try (
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player + "';");
                    ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    if (rs.getString("player").equalsIgnoreCase(player)) {
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
    public void setTokens(String player, double tokens) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try (
                    PreparedStatement ps = connection.prepareStatement("REPLACE INTO " + table + " (player,moneys,bank) VALUES(?,?,?)")
            ) {

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
    public double getBank(String player) {

        CompletableFuture<Double> getDouble = CompletableFuture.supplyAsync(() -> {

            try (
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player + "';");
                    ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    if (rs.getString("player").equalsIgnoreCase(player)) {
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
    public void setBank(String player, double tokens) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try (
                    PreparedStatement ps = connection.prepareStatement("REPLACE INTO " + table + " (player,moneys,bank) VALUES(?,?,?)")
            ) {

                ps.setString(1, player);

                ps.setDouble(2, getTokens(player));

                ps.setDouble(3, tokens);

                ps.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
        });
    }

    // Get the list of the players saved
    public List<String> getList() {
        CompletableFuture<List<String>> getList = CompletableFuture.supplyAsync(() -> {

            List<String> list = new ArrayList<>();
            try (
                    PreparedStatement ps = connection.prepareStatement("SELECT player FROM " + table);
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

    // Create a player account
    public boolean createPlayer(String player) {
        setTokens(player, plugin.getConfig().getDouble("Starting-Balance"));
        setBank(player, plugin.getConfig().getDouble("Starting-Bank-Balance"));
        return true;
    }

    // Remove a user (UUID/NICKNAME) from the database
    public void removeUser(String user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "DELETE FROM " + table + " where player = '" + user + "'";
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Convert a user (UUID/NICKNAME) from the database
    public void changeUser(OfflinePlayer user, String convertTo) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            String playerName = user.getName();
            String playerUuid = String.valueOf(user.getUniqueId());

            if (convertTo.equals("UUID")) {
                try {
                    PreparedStatement ps = connection.prepareStatement(
                            "UPDATE " + table + " " +
                                    "SET player = \"" + playerUuid + "\" " +
                                    "WHERE player = \"" + playerName + "\"");
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
                }
            } else if (convertTo.equals("NICKNAME")) {
                try {
                    PreparedStatement ps = connection.prepareStatement(
                            "UPDATE " + table + " " +
                                    "SET player = \"" + playerName + "\" " +
                                    "WHERE player = \"" + playerUuid + "\"");
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
                }
            }
        });
    }

}