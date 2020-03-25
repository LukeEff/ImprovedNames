package io.github.lukeeff.config;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.string_modification.Color;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class Utility {

    //All data keys in config file that is used is declared here.
    public static ImprovedNames plugin;
    public static final String databaseFolderName = "sqlite";
    public static final String databasePath = "database-name";
    public static final String colorPath = "color-symbol";
    public static final String tablePath = "table";
    public static final String mapListPath = "sql-database-attributes";
    public static final String wrongVersionPath = "wrong-version";
    //For the GUI
    //TODO Create a good GUI.
    public static final String playerGUI = "player-gui";
    public static final String playerGUITitle = "Rename a player";
    public static final String coreGUI = "core-gui";
    public static final String coreGUITitle = "Improved Names";
    public static final String colorsGUI = "color-gui";
    public static final String colorsGUITitle = "Choose a color";

    /**
     * Gets the config reference from the main instance
     * @return the instance of the main class config reference
     */
    public static FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Get the instance of the main class
     * @return the instance of the main class
     */
    public static ImprovedNames getMainInstance() {
        return plugin;
    }

    /**
     * Turns a message to color
     * @param message The message being turned to color
     * @return The message in color
     */
    public static String toColor(String message) {
        return Color.messageToColor(message);
    }

    /**
     * Get the plugin data folder
     * @return the plugin data folder
     */
    public static File getDataFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Get a map list out from the config
     * @return the map list from the target param path
     */
    public static List<?> getMapList() {
        return getConfig().getList(mapListPath);
    }

    /**
     * Gets a string from the config
     * @param path the path to the string
     * @return the string from the target param path
     */
    public static String getConfigString(String path) {
        return getConfig().getString(path);
    }

    /**
     * Gets a string from the config and turns it into a colored message
     * @param path the path to the String
     * @return the String in color
     */
    public static String getConfigStringInColor(String path) {
        String message = getConfigString(path);
        return toColor(message);
    }

    /**
     * Gets an int from the config
     * @param path the path to the int
     * @return the int from the specified path
     */
    public static int getConfigInt(String path) {
        return getConfig().getInt(path);
    }



}
