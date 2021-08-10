package me.itswagpvp.economyplus.bank.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Withdraw {

    public static void openMenu(Player p, int Size) {


        Inventory gui = Bukkit.getServer().createInventory(p, Size, "Withdraw");

        ItemStack RemoveItem = new ItemStack(Material.RED_WOOL, 1);
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
        gui.addItem(RemoveItem);

        RemoveItem_meta.setDisplayName("-100");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.addItem(RemoveItem);

        RemoveItem_meta.setDisplayName("-10");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.addItem(RemoveItem);

        RemoveItem_meta.setDisplayName("-1");
        RemoveItem.setItemMeta(RemoveItem_meta);
        gui.addItem(RemoveItem);

        //-------

        AmountItem_meta.setDisplayName(String.valueOf(10));
        AmountItem.setItemMeta(AmountItem_meta);
        gui.addItem(AmountItem);

        //-------

        AddItem_meta.setDisplayName("+1");
        AddItem.setItemMeta(AddItem_meta);
        gui.addItem(AddItem);

        AddItem_meta.setDisplayName("+10");
        AddItem.setItemMeta(AddItem_meta);
        gui.addItem(AddItem);

        AddItem_meta.setDisplayName("+100");
        AddItem.setItemMeta(AddItem_meta);
        gui.addItem(AddItem);

        AddItem_meta.setDisplayName("+1000");
        AddItem.setItemMeta(AddItem_meta);
        gui.addItem(AddItem);

        //--------

        ConfirmItem_meta.setDisplayName("Conferma");
        ConfirmItem.setItemMeta(ConfirmItem_meta);
        gui.addItem(ConfirmItem);

        //--------
        p.openInventory(gui);
    }


}
