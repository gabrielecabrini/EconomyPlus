package me.itswagpvp.economyplus.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;

public class MVdWPlaceholderAPI {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    public String loadMVdWPlaceholders () {

        Utils utilities = new Utils();

        // {economyplus_money}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money",
                new PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {

                        Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                        return String.format("%.2f", eco.getBalance());
                    }
                });

        // {economyplus_money_formatted}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money_formatted",
                new PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {

                        Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                        return String.valueOf(utilities.fixMoney(eco.getBalance()));
                    }
                });

     return "Invalid placeholder!";
    }

}
