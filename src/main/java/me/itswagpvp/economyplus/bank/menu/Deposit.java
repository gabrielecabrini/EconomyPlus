package me.itswagpvp.economyplus.bank.menu;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Deposit {

    private static final EconomyPlus plugin = EconomyPlus.getInstance();

    public static void openMenu(Player p, int size) {

        String title = plugin.getConfig().getString("Bank.GUI.Deposit.Title")
                .replaceAll("&", "§");

        Inventory gui = Bukkit.getServer().createInventory(p, size, title);

        int[] cornerNumbers = {0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};

        for (int index = 0; index < cornerNumbers.length; index++) {
            gui.setItem(cornerNumbers[index], new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        ItemStack RemoveItem = new ItemStack(Material.RED_WOOL);
        ItemMeta RemoveItem_meta = RemoveItem.getItemMeta();
        ItemStack AddItem = new ItemStack(Material.GREEN_WOOL, 1);
        ItemMeta AddItem_meta = RemoveItem.getItemMeta();
        ItemStack AmountItem = new ItemStack(Material.PAPER, 1);
        ItemMeta AmountItem_meta = AmountItem.getItemMeta();
        ItemStack ConfirmItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta ConfirmItem_meta = ConfirmItem.getItemMeta();

        //-------

        RemoveItem_meta.setDisplayName("-1000");

        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.setItem(10, RemoveItem);

        RemoveItem_meta.setDisplayName("-100");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.setItem(19, RemoveItem);

        RemoveItem_meta.setDisplayName("-10");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.setItem(28, RemoveItem);

        RemoveItem_meta.setDisplayName("-1");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.setItem(37, RemoveItem);

        //-------

        AmountItem_meta.setDisplayName(String.valueOf(10));
        AmountItem.setItemMeta(AmountItem_meta);
        gui.setItem(22, AmountItem);

        //-------

        AddItem_meta.setDisplayName("+1");
        AddItem.setItemMeta(AddItem_meta);
        gui.setItem(16, AddItem);

        AddItem_meta.setDisplayName("+10");
        AddItem.setItemMeta(AddItem_meta);
        gui.setItem(25, AddItem);

        AddItem_meta.setDisplayName("+100");
        AddItem.setItemMeta(AddItem_meta);
        gui.setItem(34, AddItem);

        AddItem_meta.setDisplayName("+1000");
        AddItem.setItemMeta(AddItem_meta);
        gui.setItem(43, AddItem);

        //--------

        ConfirmItem_meta.setDisplayName("Conferma");
        ConfirmItem.setItemMeta(ConfirmItem_meta);
        gui.setItem(31, ConfirmItem);

        //--------
        p.openInventory(gui);
    }

}
