package me.itswagpvp.economyplus.hooks;

import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;


public class MVdWPlaceholderAPI {

    public void loadMVdWPlaceholders () {

        Utils utilities = new Utils();

        // {economyplus_money}

        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return new Utils().format(eco.getBalance());
                });

        // {economyplus_money_formatted}

        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "economyplus_money_formatted",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return String.valueOf(utilities.fixMoney(eco.getBalance()));
                });

        // {economyplus_bank}

        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "economyplus_bank",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return new Utils().format(eco.getBank());
                });

        // {economyplus_bank_formatted}

        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "economyplus_bank_formatted",
                placeholderReplaceEvent -> {

                    Economy eco = new Economy(placeholderReplaceEvent.getPlayer(), 0);
                    return String.valueOf(utilities.fixMoney(eco.getBank()));
                });
    }

}
