package me.itswagpvp.economyplus.dbStorage.yml;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class YMLManager {

    private static final EconomyPlus plugin = EconomyPlus.getInstance();

    public boolean contains(String playerName) {
        CompletableFuture<Boolean> getBoolean = CompletableFuture.supplyAsync(() -> plugin.getYMLData().contains("Data." + playerName));

        try {
            return getBoolean.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    public double getTokens (String name)  {
        CompletableFuture<Double> getTokens = CompletableFuture.supplyAsync(() -> plugin.getYMLData().getDouble("Data." + name + ".tokens"));

        try {
            return getTokens.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 0D;
    }

    public void setTokens(String name, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getYMLData().set("Data." + name + ".tokens", value);
            plugin.saveYMLConfig();
        });
    }

    public double getBank(String name) {
        return plugin.getYMLData().getDouble("Data." + name + ".bank");
    }

    public void setBank(String name, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getYMLData().set("Data." + name + ".bank", value);
            plugin.saveYMLConfig();
        });
    }

    public List<String> getList () {
        return null;
    }

}
