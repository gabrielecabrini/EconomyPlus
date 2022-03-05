package me.itswagpvp.economyplus.database.misc;

import me.itswagpvp.economyplus.database.mysql.MySQL;
import me.itswagpvp.economyplus.database.sqlite.SQLite;
import me.itswagpvp.economyplus.database.yaml.YMLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum DatabaseType {
    H2{
        @Override
        public boolean contains(String playerName) {
            return new SQLite().getList().contains(playerName);
        }

        @Override
        public double getToken(String playerName) {
            return  new SQLite().getTokens(playerName);
        }

        @Override
        public void setTokens(String playerName, double tokens) {
            new SQLite().setTokens(playerName, tokens);
        }

        @Override
        public void setBank(String playerName, double tokens) {
            new SQLite().setBank(playerName, tokens);
        }

        @Override
        public double getBank(String playerName) {
            return new SQLite().getBank(playerName);
        }

        @Override
        public List<String> getList() {
            return  new SQLite().getList();
        }

        @Override
        public boolean createPlayer(String player) {
            return new SQLite().createPlayer(player);
        }

        @Override
        public void removePlayer(String player) {
            new SQLite().removeUser(player);
        }

        @Override
        public void close() throws SQLException {
            new SQLite().getSQLiteConnection().close();
        }
    },
    MySQL{
        @Override
        public boolean contains(String playerName) {
            return new MySQL().getList().contains(playerName);
        }

        @Override
        public double getToken(String playerName) {
            return new MySQL().getTokens(playerName);
        }

        @Override
        public void setTokens(String playerName, double tokens) {
            new MySQL().setTokens(playerName, tokens);
        }

        @Override
        public void setBank(String playerName, double tokens) {
            new MySQL().setBank(playerName, tokens);
        }

        @Override
        public double getBank(String playerName) {
            return new MySQL().getBank(playerName);
        }

        @Override
        public List<String> getList() {
            return new MySQL().getList();
        }

        @Override
        public boolean createPlayer(String player) {
            return new MySQL().createPlayer(player);
        }

        @Override
        public void removePlayer(String player) {
            new MySQL().removeUser(player);
        }

        @Override
        public void close() {
            new MySQL().closeConnection();
        }
    },
    YAML{
        @Override
        public boolean contains(String playerName) {
            return new YMLManager().contains(playerName);
        }

        @Override
        public double getToken(String playerName) {
            return new YMLManager().getTokens(playerName);
        }

        @Override
        public void setTokens(String playerName, double tokens) {
            new YMLManager().setTokens(playerName, tokens);
        }

        @Override
        public void setBank(String playerName, double tokens) {
            new YMLManager().setBank(playerName, tokens);
        }

        @Override
        public double getBank(String playerName) {
            return new YMLManager().getBank(playerName);
        }

        @Override
        public List<String> getList() {
            return new YMLManager().getList();
        }

        @Override
        public boolean createPlayer(String player) {
            return new YMLManager().createPlayer(player);
        }

        @Override
        public void removePlayer(String player) {
            new YMLManager().removePlayer(player);
        }

        @Override
        public void close() {}
    },
    UNDEFINED {
        @Override
        public boolean contains(String playerName) {
            return false;
        }

        @Override
        public double getToken(String playerName) {
            return 0D;
        }

        @Override
        public void setTokens(String playerName, double tokens) {}

        @Override
        public void setBank(String playerName, double tokens) {}

        @Override
        public double getBank(String playerName) {
            return 0D;
        }

        @Override
        public List<String> getList() {
            return new ArrayList<>();
        }

        @Override
        public boolean createPlayer(String player) {
            return false;
        }

        @Override
        public void removePlayer(String player) {}

        @Override
        public void close() {}
    };

    public abstract boolean contains(String playerName);
    public abstract double getToken(String playerName);
    public abstract void setTokens(String playerName, double tokens);
    public abstract void setBank(String playerName, double tokens);
    public abstract double getBank(String playerName);
    public abstract List<String> getList();
    public abstract boolean createPlayer(String player);
    public abstract void removePlayer(String player);
    public abstract void close() throws SQLException;
}
