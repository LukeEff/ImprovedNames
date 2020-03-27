package io.github.lukeeff.config;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.string_modification.Color;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class Utility {

    //All data keys in config file that is used is declared here.
    public static ImprovedNames plugin;
    private static final String databaseFolderNamePath = "database-folder-name";
    public static final String databasePath = "database-name";
    public static final String colorPath = "color-symbol";
    public static final String tablePath = "table";
    private static final String tableAttributesPath = "sql-database-attributes";
    public static final String wrongVersionPath = "wrong-version";
    private static final String groupsPath = "groups";
    private static final String invalidTeamConfigMessagePath = "invalid-group-message";
    private static final String prefixInTabPath = "prefix-in-tab";
    private static final String suffixInTabPath = "suffix-in-tab";
    private static final String OFFLINEMESSAGEPATH = "offline-message";
    private static final String TOOLONGNAMEPATH = "name-too-long";
    private static final String RENAMEUSAGEPATH = "rename-usage";
    private static final String RENAMESUCCESSPATH = "rename-success";
    private static final String INVALIDNICKNAMEPATH = "invalid-nickname-message";
    private static final String CLEAREDNICKNAMESUCCESSPATH = "cleared-nickname-success";

    public static String getClearedNicknameSuccessMessage() {
        return getConfigStringInColor(CLEAREDNICKNAMESUCCESSPATH);
    }
    public static String getDatabaseName() {
        return getConfigString(databasePath);
    }

    public static String getInvalidNicknameMessage() {
        return getConfigStringInColor(INVALIDNICKNAMEPATH);
    }
    public static String getDatabaseFolderName() {
        return getConfigString(databaseFolderNamePath);
    }

    public static boolean getPrefixInTabValid() {
        return plugin.config.getBoolean(prefixInTabPath);
    }

    public static boolean getSuffixInTabValid() {
        return plugin.config.getBoolean(suffixInTabPath);
    }

    public static String getOfflineMessage() {
        return getConfigStringInColor(OFFLINEMESSAGEPATH);
    }

    public static String getRenameUsageMessage() {
        return getConfigStringInColor(RENAMEUSAGEPATH);
    }

    public static String getTooLongNameMessage() {
        return getConfigStringInColor(TOOLONGNAMEPATH);
    }

    public static String getRenameSuccessMessage() {
        return getConfigStringInColor(RENAMESUCCESSPATH);
    }

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

    public static List<?> getGroupList() {return getConfig().getList(groupsPath); }

    /**
     * Get a map list out from the config
     * @return the map list from the target param path
     */
    public static List<?> getConfigList() {
        return getConfig().getList(tableAttributesPath);
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

    public static String getInvalidTeamConfigMessage() {
        return getConfigStringInColor(invalidTeamConfigMessagePath);
    }


}
