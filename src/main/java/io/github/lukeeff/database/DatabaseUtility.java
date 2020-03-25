package io.github.lukeeff.database;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import org.bukkit.entity.Player;

import java.io.File;

public class DatabaseUtility {

    public static ImprovedNames plugin;
    private static File databaseFolder;


    /**
     * Takes a player param and returns their uuid in as a String
     * @param player the player we will be getting a uuid from
     * @return the player uuid as a String
     */
    public static String playerToUUIDString(Player player) {
        return player.getUniqueId().toString();
    }

    /**
     * Finds the database file location
     * @return the path of the db file
     */
    public static String getDatabasePath() {
        final String databasePath = Utility.databasePath;
        final String databaseFileName = getConfigString(databasePath);
        final String path = "\\" + databaseFileName + ".db";
        return databaseFolder.getPath().concat(path);
    }

    /**
     * Create the database folder. Will not overwrite an existing folder
     */
    public static void createDatabaseFolder() {
        final String databaseFolderName = Utility.databaseFolderName;
        databaseFolder = new File(getDataFolder(),databaseFolderName);
        databaseFolder.mkdirs();
    }

    private static String getConfigString(String path) {
        return Utility.getConfigString(path);
    }

    private static File getDataFolder() {
        return Utility.getDataFolder();
    }

}
