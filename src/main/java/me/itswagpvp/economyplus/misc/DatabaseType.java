package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.dbStorage.mysql.MySQL;
import org.apache.commons.lang.NotImplementedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum DatabaseType {
    H2{
        @Override
        public boolean contains(String playerName) {
            return EconomyPlus.getInstance().getRDatabase().getList().contains(playerName);
        }

        @Override
        public double getToken(String playerName) {
            return EconomyPlus.getInstance().getRDatabase().getTokens(playerName);
        }

        @Override
        public void setTokens(String playerName, double tokens) {
            EconomyPlus.getInstance().getRDatabase().setTokens(playerName, tokens);
        }

        @Override
        public void setBank(String playerName, double tokens) {
            EconomyPlus.getInstance().getRDatabase().setBank(playerName, tokens);
        }

        @Override
        public double getBank(String playerName) {
            return EconomyPlus.getInstance().getRDatabase().getBank(playerName);
        }

        @Override
        public List<String> getList() {
            return EconomyPlus.getInstance().getRDatabase().getList();
        }

        @Override
        public void close() throws SQLException {
            EconomyPlus.getInstance().getRDatabase().getSQLiteConnection().close();
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
        public void close() {
            new MySQL().closeConnection();
        }
    },
    Json{
        @Override
        public boolean contains(String playerName) {
            return false;
        }

        @Override
        public double getToken(String playerName) {
            return 0;
        }

        @Override
        public void setTokens(String playerName, double tokens) {

        }

        @Override
        public void setBank(String playerName, double tokens) {

        }

        @Override
        public double getBank(String playerName) {
            return 0;
        }

        @Override
        public List<String> getList() {
            throw new NotImplementedException();
        }

        @Override
        public void close() {
            throw new NotImplementedException();
        }
    },
    YAML{
        @Override
        public boolean contains(String playerName) {
            return new YMLManager().contains(playerName);
        }

        @Override
        public double getToken(String playerName) {
            return 0;
        }

        @Override
        public void setTokens(String playerName, double tokens) {

        }

        @Override
        public void setBank(String playerName, double tokens) {

        }

        @Override
        public double getBank(String playerName) {
            return 0;
        }

        @Override
        public List<String> getList() {
            throw new NotImplementedException();
        }

        @Override
        public void close() {}
    },
    Undefined{
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
        public void close() {}
    };

    public abstract boolean contains(String playerName);
    public abstract double getToken(String playerName);
    public abstract void setTokens(String playerName, double tokens);
    public abstract void setBank(String playerName, double tokens);
    public abstract double getBank(String playerName);
    public abstract List<String> getList();
    public abstract void close() throws SQLException;
}
