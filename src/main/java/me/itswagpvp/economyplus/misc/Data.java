package me.itswagpvp.economyplus.misc;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class Data {

    public List<PlayerData> balTop;
    public Map<String,PlayerData> balTopName;

    public Data() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();

        loadFromData();
    }

    // Save data and save data for the baltop
    public void saveData(OfflinePlayer p, double money) {

        EconomyPlus.getInstance().getRDatabase().setTokens(p.getName(), money);


        if ( getBalTopName().containsKey( p.getName() ) ) {
            // update the stored money:
            getBalTopName().get( p.getName() ).setMoney( money );
        }
        else {
            // New payer data: Update both collections:
            PlayerData pData = new PlayerData(p.getName(), money);
            getBalTop().add( pData );
            getBalTopName().put( pData.getName(), pData );
        }

        // Sort the getBalTop() List:
        Collections.sort(getBalTop(), new PlayerComparator() );

    }

    public boolean hasBalance(OfflinePlayer p) {
        return EconomyPlus.getInstance().getRDatabase().getList().contains(p.getName());
    }

    public boolean hasBalance(String s) {
        return EconomyPlus.getInstance().getRDatabase().getList().contains(s);
    }

    public double getValue(OfflinePlayer p) {
        return EconomyPlus.getInstance().getRDatabase().getTokens(p.getName());
    }

    private void loadFromData() {
        getBalTop().clear();

        for ( String playerName : EconomyPlus.getInstance().getRDatabase().getList()) {

            Double money = Double.valueOf(EconomyPlus.getInstance().getRDatabase().getTokens(playerName));

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

    public class PlayerComparator
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

    public class PlayerData {

        private final String name;
        private double money;

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

        public void setMoney(double money) {
            this.money = money;
        }

    }
}
