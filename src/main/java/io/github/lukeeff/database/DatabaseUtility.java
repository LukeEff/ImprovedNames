package io.github.lukeeff.database;

import io.github.lukeeff.config.Utility;

import java.io.File;

public class DatabaseUtility extends SQLiteSyntax {

    private static File databaseFolder;

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
