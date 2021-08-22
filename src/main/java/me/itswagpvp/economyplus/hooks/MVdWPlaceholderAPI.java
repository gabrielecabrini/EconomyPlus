package me.itswagpvp.economyplus.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;

public class MVdWPlaceholderAPI {

    public static EconomyPlus plugin = EconomyPlus.getInstance();

    public void loadMVdWPlaceholders () {

        Utils utilities = new Utils();

        // {economyplus_money}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return new Utils().format(eco.getBalance());
                });

        // {economyplus_money_formatted}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money_formatted",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return String.valueOf(utilities.fixMoney(eco.getBalance()));
                });

        // {economyplus_bank}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_bank",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return new Utils().format(eco.getBank());
                });

        // {economyplus_bank_formatted}

        PlaceholderAPI.registerPlaceholder(plugin, "economyplus_bank_formatted",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return String.valueOf(utilities.fixMoney(eco.getBank()));
                });
    }

}
