package io.github.lukeeff.database;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class SQLite extends SQLiteSyntax {

    ImprovedNames plugin;
    Connection connection;
    PreparedStatement statement;
    ResultSet results;
    String table, database, databaseConnectionPath;
    List sqlAttributes;

    public SQLite(ImprovedNames instance) {
        DatabaseUtility.createDatabaseFolder();
        initializeReferences(instance);

        try {
            connection = getConnection();
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes field variables
     * @param instance instance of main class
     */
    private void initializeReferences(ImprovedNames instance) {
        plugin = instance;
        table = getTable();
        database = getDatabase();
        databaseConnectionPath = getDatabaseConnectionPath();
        sqlAttributes = getMapList();
    }

    /**
     * Gets the database table name from the config
     * @return the database table name
     */
    private String getTable() {
        return getConfigString(Utility.tablePath);
    }

    /**
     * Gets the database name from the config
     * @return the database name
     */
    private String getDatabase() {
        return getConfigString(Utility.databasePath);
    }

    /**
     * Gets the database connection path from the config
     * @return the database connection path
     */
    private String getDatabaseConnectionPath() {
        return DatabaseUtility.getDatabasePath();
    }

    /**
     * Get the string value in respect to a config path
     * @param path the path that targets the value
     * @return the value in respect to the param path target
     */
    private String getConfigString(String path) {
        return Utility.getConfigString(path);
    }

    /**
     * Get a map list in respect to a config path
     * @return the value in respect to the param path target
     */
    private List<?> getMapList() {
        return Utility.getMapList();
    }

    /**
     * Create a new table in a SQLite database
     * @throws SQLException thrown usually in respect to a syntax error
     */
    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        List<List<String>> attributes = (List<List<String>>) getMapList(); //List of SQL attributes
        String sql = buildSQL(attributes);
        statement.executeUpdate(sql);
        }

        //TODO Delete this when finished with class
    private void previewSQL(String sql) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + sql);
    }

    /**
     * Builds a string for creating a new table for SQLite
     * @param attributes the attributes for the table
     * @return the instructions for a SQLite create table
     */
    private String buildSQL(List<List<String>> attributes) {
        StringBuilder sql = new StringBuilder();
        sql.append(createTable + table + createTableNewLine);

        for(List<String> attribute: attributes) {
            for(String element: attribute) {
                sql.append(element + space);
            }
            removeLastChar(sql);
            sql.append(commaNewLine);
        }
        removeLastChar(sql);
        removeLastChar(sql);
        sql.append(concludeCreateTable);

        return sql.toString();
    }

    /**
     * Remove the last character in a StringBuilder object.
     * @param string the StringBuilder object that is being muted
     * @return the StringBuilder object without the final character.
     */
    private StringBuilder removeLastChar(StringBuilder string) {
        int index = string.length() - 1;
        string.deleteCharAt(index);
        return string;
    }

    private Connection getConnection() {
        final String path = databaseConnectionPath;
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + path);
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Queries the database for the param record and returns true or false in respect to its existence
     * @param uuid the id the database is going to fetch
     * @return true if the record exists and false if it does not
     * @throws SQLException thrown when SQL server returns a warning or error
     */
    public boolean playerExists(UUID uuid) throws SQLException {

        setStatement(uuid);
        setResults();
        if(results.next()) { //Returns true if there is a record
            return true;
        }
        return false;
    }

    /**
     * Gets a player nickname from the database
     * @param uuid the uuid of the player
     * @return the nickname of the player. Null if not in database or sql exception
     */
    public String getPlayerNickname(String uuid) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT NAME FROM " + table + " WHERE UUID = ?");
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getString("NAME");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a player name through their nickname
     * @param nickName the nickname of the player
     * @return the real name of the player. Null if not in database or SQL exception
     */
    public String getPlayerFromNickname(String nickName) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT REALNAME FROM " + table + " WHERE RAWNICKNAME = ?");
            statement.setString(1, ChatColor.stripColor(nickName.toLowerCase()));
            ResultSet names = statement.executeQuery();
            names.next();
            return names.getString("REALNAME");
        } catch (SQLException e) {
            return ChatColor.RED + "No names found";
        }
    }


    /**
     * Selects data from the SQL database in respect to param.
     * @param uuid the data that will be selected in respect to this uuid.
     */
    public void setStatement(UUID uuid) {
        try {
            statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes and returns the statements query
     */
    public void setResults() {
        try {
            this.results = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes connection to SQL database and fetches player record if it exists. Creates new record if not.
     * @param player record in respect to player being checked for existence
     * @throws SQLException thrown when SQL server returns a warning or error
     */
    public void checkPlayerData(Player player) throws SQLException {

        setStatement(player.getUniqueId());
        setResults();
        results.next();
        if(!(playerExists(player.getUniqueId()))) {
            addPlayerToDB(player);
        }
    }

    /**
     * Modifies the record of any player in a SQL database.
     * @param player the query we are retrieving and modifying
     * @param nickName the modified value
     * @return a preparred statement with modified records or null if a SQLException is thrown
     */
    public PreparedStatement modifyRecord(Player player, String nickName) {
        final String uuid = player.getUniqueId().toString();
        final String realName = player.getName();
        final PreparedStatement replace = playerReplaceStatement();
        try {
            replace.setString(1, nickName);
            replace.setString(2, ChatColor.stripColor(nickName.toLowerCase()));
            replace.setString(3, uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return replace;
    }

    public PreparedStatement playerReplaceStatement() {
        try {
            return connection
                    .prepareStatement("UPDATE " + table + "\n" +
                            "SET Name = ?,\n" +
                            "RAWNICKNAME = ?\n" +
                            "WHERE UUID = ?;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Queries for the statement of a player
     * @return a preparedstatement of a player
     * @throws SQLException thrown when SQL server returns a warning or error
     */
    public PreparedStatement getPlayerStatement() throws SQLException {
        return connection
                .prepareStatement("INSERT INTO " + table + " (UUID,REALNAME,NAME,RAWNICKNAME)\n" +
                        "VALUES (?,?,?,?);");
    }

    /**
     * Sets the statement of a player record.
     * @param insert the query of a record
     * @param uuid the uuid of the player as a string
     * @param realName the real name of a player
     * @param nickname the nickname a player will be retrieving
     */
    public void setStatement(PreparedStatement insert, String uuid, String realName, String nickname) {
        try {
            insert.setString(1, uuid);
            insert.setString(2, realName);
            insert.setString(3, nickname);
            insert.setString(4, ChatColor.stripColor(nickname).toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a record of a player into the database
     * @param player the player being injected into the database
     * @throws SQLException thrown when SQL server returns a warning or error
     */
    public void addPlayerToDB(Player player) throws SQLException {
        final String uuid = player.getUniqueId().toString();
        final String realName = player.getName();
        final PreparedStatement insert = getPlayerStatement();
        setStatement(insert, uuid, realName, realName);
        insert.executeUpdate();

    }


}
