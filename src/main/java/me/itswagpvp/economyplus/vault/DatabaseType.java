package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.storage.mysql.MySQL;

enum DatabaseType {
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
    };

    public abstract boolean contains(String playerName);
    public abstract double getToken(String playerName);
    public abstract void setTokens(String playerName, double tokens);
    public abstract void setBank(String playerName, double tokens);
    public abstract double getBank(String playerName);

}
