package io.github.lukeeff.database;

import io.github.lukeeff.config.Utility;
import org.bukkit.entity.Player;

import java.io.File;

public class DatabaseUtility extends SQLiteSyntax {

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
        final String path = "\\" + DATABASENAME + ".db";
        return databaseFolder.getPath().concat(path);
    }

    /**
     * Create the database folder. Will not overwrite an existing folder
     */
    public static void createDatabaseFolder() {
        databaseFolder = new File(getDataFolder(), DATABASEFOLDERNAME);
        databaseFolder.mkdirs();
    }

    private static File getDataFolder() {
        return Utility.getDataFolder();
    }

}
