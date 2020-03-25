package io.github.lukeeff.gui;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import org.bukkit.inventory.Inventory;


public class PlayerGUI extends CoreGUI {
    //TODO GUI
    public PlayerGUI(ImprovedNames instance) {
        super(instance);
    }

    @Override
    void inventoryBlueprint() {
        String title = Utility.playerGUITitle;
    }


}
