package io.github.lukeeff.gui;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.string_modification.Color;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

abstract class CoreGUI {

    ImprovedNames plugin;
    //TODO GUI
    public CoreGUI(ImprovedNames instance) {
        plugin = instance;
    }

    /**
     * Create a bukkit inventory for a gui
     * @param title the title of the gui
     * @param size the size of the gui
     * @return the inventory
     */
    protected Inventory createInventory(String title, int size) {
        return Bukkit.createInventory(null, size, title);
    }

    /**
     * Set the meta for any item
     * @param meta the item you are modifying the meta of
     * @param name the new name of the item
     * @param lore the new lore of the item
     */
    protected void setMeta(ItemMeta meta, String name, ArrayList<String> lore) {
        meta.setDisplayName(name);
        meta.setLore(lore);
    }

    /**
     * Adds all items to their respective locations in the inventory
     * @param itemIndex a key value pair with the key being the item and the Integer being the location
     */
    protected void addItemsToInventory(Inventory gui, HashMap<ItemStack, Integer> itemIndex) {
        int index;
        for(ItemStack item: itemIndex.keySet()) {
            index = itemIndex.get(item);
            gui.addItem(item);
            gui.setItem(index, item);
        }
    }

    /**
     * Gets a string object from a path
     * @param path the path to the config key
     * @return the String from the config
     */
    protected String getConfigString(String path) {
        return Utility.getConfigString(path);
    }

    /**
     * Returns a string in color
     * @param message the message not in color
     * @return the message in color
     */
    protected String toColor(String message) {
        return Color.messageToColor(message);
    }

    /**
     * Get item meta of an ItemStack object
     * @param item the ItemStack you want meta from
     * @return the ItemMeta object
     */
    protected ItemMeta getItemMeta(ItemStack item) {
        return item.getItemMeta();
    }

    abstract void inventoryBlueprint();

}
