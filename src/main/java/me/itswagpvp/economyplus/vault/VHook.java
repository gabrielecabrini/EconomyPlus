package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VHook {

    public EconomyPlus plugin = EconomyPlus.getInstance();
    public Economy veco;

    public void onHook() {
        veco = new VEconomy(plugin);
        plugin.getServer().getServicesManager().register(Economy.class, EconomyPlus.veco, plugin, ServicePriority.Highest);
    }

    public void offHook() {
        plugin.getServer().getServicesManager().unregister(Economy.class, EconomyPlus.veco);
    }
}
