package io.github.lukeeff.database;

import io.github.lukeeff.ImprovedNames;
import net.md_5.bungee.api.ChatColor;

import java.sql.*;

public class SQLite extends SQLiteSyntax {

    ImprovedNames plugin;
    private static Connection connection;

    /**
     * Constructor for SQLite database initialization. Must be called at onEnable();
     * @param instance instance of the main class
     */
    public SQLite(ImprovedNames instance) {
        plugin = instance;
        DatabaseUtility.createDatabaseFolder();
        establishDatabaseConnection();
    }

    /**
     * Establishes a connection to the SQLite database.
     * Automatically will create one if it does not exist.
     */
    private void establishDatabaseConnection() {
        try {
            final String path = DatabaseUtility.getDatabasePath();
            connection = DriverManager.getConnection(driver + path);
            createDatabaseTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Strips all colors from a string and makes it lowercase
     * @param string the string
     * @return the string with no colors or capitals
     */
    public static String rawify(String string) {
        return ChatColor.stripColor(string).toLowerCase();
    }

    /**
     * Will create a new table if one does not already exist.
     * @throws SQLException thrown in respect to a syntax error.
     */
    private void createDatabaseTable() throws SQLException {
        Statement statement = connection.createStatement(); //Can't use PreparedStatement for this.
        statement.executeUpdate(CREATETABLETASK); //SQLite syntax provided in SQLiteSyntax class.
    }

    /**
     * Adds a player to the database.
     * @param uuid the uuid of the player as a string
     * @param realName the username of the player
     * @throws SQLException thrown when SQL syntax is wrong
     */
    public static void addPlayer(String uuid, String realName, String nickName, String prefix, String suffix)  {
        try {
            PreparedStatement insertPlayer = connection.prepareStatement(INSERTPLAYER);
            final String RAWNICKNAME = rawify(nickName);
            String[] elements = {uuid, realName, nickName, RAWNICKNAME, prefix, suffix};
            for (int i = 0; i < elements.length; i++) {
                insertPlayer.setString(i + 1, elements[i]);
            }
            insertPlayer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO Return a list of the cases in which it's true
    /**
     * Get data from the database from a specified column and specified key
     * @param whereColumnKey the key for some condition
     * @param fromColumn the column(s) that will be returned
     * @param whereColumn the column condition for key
     * @return the object found in the database
     * @throws SQLException thrown in respect to improper syntax
     */
    private static String getPlayerData(String whereColumnKey, String fromColumn, String whereColumn) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(selectSyntax(fromColumn, whereColumn));
        statement.setString(1, whereColumnKey);
        ResultSet name = statement.executeQuery();
        name.next();
        return name.getString(fromColumn);
    }

    /**
     * Get a player nick name from the database.
     * @param uuid the uuid of the player.
     * @return the nick name associated with the uuid.
     */
    public static String getPlayerNickname(String uuid) {
        try {
            return getPlayerData(uuid, NICKNAME, UUID);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a player username from a nickname.
     * @param nickname the nick name of the player.
     * @return the username associated with the nick name.
     */
    public static String getPlayerFromNickname(String nickname) {
        final String RAWNAME = rawify(nickname);
        try {
            return getPlayerData(RAWNAME, REALNAME, RAWNICKNAME);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the name of a player from a uuid.
     * @param uuid the uuid of the player.
     * @return the name of the player.
     */
    public static String getPlayerName(String uuid) {
        try {
            return getPlayerData(uuid, REALNAME, UUID);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a player prefix from the database
     * @param uuid the uuid of the player
     * @return the prefix associated with the uuid.
     */
    public static String getPrefix(String uuid) {
        try {
            return getPlayerData(uuid, PREFIX, UUID);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a player suffix from the database
     * @param uuid the uuid of the player
     * @return the suffix associated with the uuid
     */
    public static String getSuffix(String uuid) {
        try {
            return getPlayerData(uuid, SUFFIX, UUID);
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Modify and update player data from the database
     * @param uuid the uuid associated with the player
     * @param column the column with data being modified
     * @param newValue the new value for the column in respect to the player
     * @throws SQLException thrown with sql syntax error
     */
    private static void setPlayerData(String uuid, String column, String newValue) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(updateSyntax(column));
        statement.setString(1, newValue);
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    /**
     * Sets the player nickname in the database
     * @param uuid the uuid associated with the nickname being changed
     * @param nickname the nickname of the player
     */
    public static void setPlayerNickname(String uuid, String nickname) {
        final String RAWNAME = rawify(nickname);
        try {
            setPlayerData(uuid, RAWNICKNAME, RAWNAME);
            setPlayerData(uuid, NICKNAME, nickname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a player prefix in the database
     * @param uuid the uuid associated with the player
     * @param prefix the prefix being set
     */
    public static void setPlayerPrefix(String uuid, String prefix) {
        try {
            setPlayerData(uuid, PREFIX, prefix);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a player suffix in the database
     * @param uuid the uuid associated with the player
     * @param suffix the suffix being set
     */
    public static void setPlayerSuffix(String uuid, String suffix) {
        try {
            setPlayerData(uuid, SUFFIX, suffix);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if player is in database
     * @param uuid the uuid of the player
     * @return true if player name was found. False if player name wasn't found
     */
    public static boolean inDatabase(String uuid) {

        if(getPlayerName(uuid) != null) {
            return true;
        }
        return false;
    }



}
