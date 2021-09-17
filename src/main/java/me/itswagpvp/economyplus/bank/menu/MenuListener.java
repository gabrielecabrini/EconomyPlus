package me.itswagpvp.economyplus.bank.menu;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {

    private static final EconomyPlus plugin = EconomyPlus.getInstance();

    int amount = 10;

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null) {
            return;
        }

        if (e.getCurrentItem().getType() == Material.getMaterial(plugin.getConfig().getString("Bank.GUI.Borders"))) {
            e.setCancelled(true);
        }

        String withdrawTitle = plugin.getConfig().getString("Bank.GUI.Withdraw.Title")
                .replaceAll("&", "§");

        String depositTitle = plugin.getConfig().getString("Bank.GUI.Deposit.Title")
                .replaceAll("&", "§");

        if (e.getView().getTitle().equals(withdrawTitle) || e.getView().getTitle().equals(depositTitle)) {
            e.setCancelled(true);
        }

        Player player = (Player) e.getWhoClicked();

        // Check deposit inventory
        if (e.getView().getTitle().equals(depositTitle)) {

            ItemMeta paperItem_meta = e.getView().getItem(4).getItemMeta();

            if (!e.getCurrentItem().getType().isItem()) {
                player.sendMessage("No Item");
                return;
            }

            if (e.getCurrentItem().getType() == Material.RED_WOOL) {

                if ("§c-1".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 1;
                else if ("§c-10".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 10;
                else if ("§c-100".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 100;
                else if ("§c-1000".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 1000;
                if (amount < 1) {
                    amount = 1;
                }

                paperItem_meta.setDisplayName("§e" + amount);
                e.getView().getItem(22).setItemMeta(paperItem_meta);

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {

                if ("§a+1".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 1;
                else if ("§a+10".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 10;
                else if ("§a+100".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 100;
                else if ("§a+1000".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 1000;
                if (amount < 1) {
                    amount = 1;
                }

                paperItem_meta.setDisplayName("§e" + amount);
                e.getView().getItem(22).setItemMeta(paperItem_meta);

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.PAPER) {

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.getMaterial(plugin.getConfig().getString("Bank.GUI.ConfirmButton"))) {
                double balance = new Economy(player, amount).getBalance();
                if ((balance - amount) < 0) {
                    player.sendMessage(plugin.getMessage("Pay.NoMoney"));
                    Utils.playErrorSound(player);
                    e.getView().close();
                    return;
                }

                Economy eco = new Economy(player, amount);
                eco.takeBalance();

                double value = eco.getBank();

                Economy econ = new Economy(player, (amount + value));
                econ.setBank();

                player.sendMessage(plugin.getMessage("Bank.Deposit").replaceAll("%money%", "" + amount));
                amount = 10;
                Utils.playSuccessSound(player);
                e.setCancelled(true);
                e.getView().close();
                return;
            }
        }

        // Check withdraw inventory
        if (e.getView().getTitle().equals(withdrawTitle)) {

            ItemMeta paperItem_meta = e.getView().getItem(4).getItemMeta();

            //make sure they clicked on a player head
            if (!e.getCurrentItem().getType().isItem()) {
                player.sendMessage("No Item");
                return;
            }

            if (e.getCurrentItem().getType() == Material.RED_WOOL) {

                if ("§c-1".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 1;
                else if ("§c-10".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 10;
                else if ("§c-100".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 100;
                else if ("§c-1000".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount -= 1000;
                if (amount < 1) {
                    amount = 1;
                }

                paperItem_meta.setDisplayName("§e" + amount);
                e.getView().getItem(22).setItemMeta(paperItem_meta);

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {

                if ("§a+1".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 1;
                else if ("§a+10".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 10;
                else if ("§a+100".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 100;
                else if ("§a+1000".equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
                    amount += 1000;
                if (amount < 1) {
                    amount = 1;
                }

                paperItem_meta.setDisplayName("§e" + amount);
                e.getView().getItem(22).setItemMeta(paperItem_meta);

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.PAPER) {

                e.setCancelled(true);

            } else if (e.getCurrentItem().getType() == Material.getMaterial(plugin.getConfig().getString("Bank.GUI.ConfirmButton"))) {

                double balance = new Economy(player, 0).getBank();
                if (amount > balance) {
                    player.sendMessage(plugin.getMessage("Bank.NoMoney"));
                    Utils.playErrorSound(player);
                    e.getView().close();
                    return;
                }

                Economy eco = new Economy(player, amount);
                eco.addBalance();

                double value = eco.getBank();

                Economy econ = new Economy(player, (value - amount));
                econ.setBank();

                player.sendMessage(plugin.getMessage("Bank.Withdraw").replaceAll("%money%", "" + amount));
                amount = 10;
                Utils.playSuccessSound(player);
                e.setCancelled(true);
                e.getView().close();
            }
        }
    }
}