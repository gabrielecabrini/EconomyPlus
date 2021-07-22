package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.storage.mysql.MySQL;

import java.util.*;

public class Data {

    public List<PlayerData> balTop;
    public Map<String,PlayerData> balTopName;
    private final String type = EconomyPlus.getInstance().getConfig().getString("Database.Type");

    public Data() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();

        if (type.equalsIgnoreCase("H2")) {
            loadFromH2Data();
        }

        if (type.equalsIgnoreCase("MySQL")) {
            loadFromSQLData();
        }

    }


    private void loadFromData() {
        getBalTop().clear();

        for ( String playerName : EconomyPlus.getInstance().getRDatabase().getList()) {

            Double money = EconomyPlus.getInstance().getRDatabase().getTokens(playerName);

            PlayerData pData = new PlayerData(playerName, money);
            getBalTop().add( pData );
            getBalTopName().put( pData.getName(), pData );

        }

        Collections.sort( getBalTop(), new PlayerComparator() );

    }

    private void loadFromSQLData() {
        getBalTop().clear();

        for ( String playerName : new MySQL().getList()) {

            Double money = new MySQL().getTokens(playerName);

            PlayerData pData = new PlayerData(playerName, money);
            getBalTop().add( pData );
            getBalTopName().put( pData.getName(), pData );

        }

        Collections.sort( getBalTop(), new PlayerComparator() );

    }


    public List<PlayerData> getBalTop() {
        return balTop;
    }

    public Map<String, PlayerData> getBalTopName() {
        return balTopName;
    }

    public static class PlayerComparator
            implements Comparator<PlayerData> {

        @Override
        public int compare( PlayerData arg0, PlayerData arg1 )
        {
            int results = Double.compare( arg1.getMoney(), arg0.getMoney() );
            if ( results == 0 ) {
                results = arg0.getName().compareToIgnoreCase( arg1.getName() );
            }
            return results;
        }
    }

    public static class PlayerData {

        private final String name;
        private final double money;

        public PlayerData(String name, double money) {
            super();

            this.name = name;
            this.money = money;

        }

        public String getName() {
            return name;
        }

        public double getMoney() {
            return money;
        }

    }
}
