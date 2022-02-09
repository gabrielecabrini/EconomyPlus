package me.itswagpvp.economyplus.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class PlaceholderAPI extends PlaceholderExpansion {

    public EconomyPlus plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public PlaceholderAPI(EconomyPlus plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convenience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "economyplus";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convenience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    public String onPlaceholderRequest(Player player, @NotNull String identifier){

        if(player == null){
            return "";
        }

        Utils utilities = new Utils();

        // %economyplus_money%
        if(identifier.equals("money")){
            if (CacheManager.getCache(1).get(Selector.playerToString(player)) == null) {
                return new Utils().format(0D);
            }
            return new Utils().format(CacheManager.getCache(1).get(Selector.playerToString(player)));
        }

        //%economyplus_money_formatted%
        if (identifier.equals("money_formatted")) {
            if (CacheManager.getCache(1).get(Selector.playerToString(player)) == null) {
                return new Utils().fixMoney(0D);
            }
            return String.valueOf(utilities.fixMoney(CacheManager.getCache(1).get(Selector.playerToString(player))));
        }

        // %economyplus_bank%
        if(identifier.equals("bank")){
            if (CacheManager.getCache(2).get(Selector.playerToString(player)) == null) {
                return new Utils().format(0D);
            }
            return new Utils().format(CacheManager.getCache(2).get(Selector.playerToString(player)));
        }

        //%economyplus_money_formatted%
        if (identifier.equals("bank_formatted")) {
            if (CacheManager.getCache(2).get(Selector.playerToString(player)) == null) {
                return new Utils().fixMoney(0D);
            }
            return String.valueOf(utilities.fixMoney(CacheManager.getCache(2).get(Selector.playerToString(player))));
        }

        return "Invalid placeholder!";
    }
}

