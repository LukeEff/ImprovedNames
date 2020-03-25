package io.github.lukeeff.event_listener;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    //TODO GUI
    ImprovedNames plugin;
    private String guiTitle, somename;

    public InventoryListener(ImprovedNames instance) {
        plugin = instance;
    }

}
