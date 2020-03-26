package io.github.lukeeff.database;

abstract class SQLiteSyntax {

    //TODO Not priority, but this has a LOT of room for improvement.
    //This is a waste of memory... improve in the future

    //SQLite driver
    String driver = "jdbc:sqlite:";

    //These values will be based on what I want the database to contain
    public static final String DATABASENAME = "iname";
    protected static final String DATABASEFOLDERNAME = "sqlite";
    public static final String TABLE = "player_data";

    //Columns
    public static final String UUID = "UUID";
    public static final String REALNAME = "REALNAME";
    public static final String NICKNAME = "NAME";
    public static final String RAWNICKNAME = "RAWNICKNAME";
    public static final String PREFIX = "PREFIX";
    public static final String SUFFIX = "SUFFIX";

    //Syntax
    protected final String VARCHAR = "varChar";
    protected final String PRIMARYKEY = "PRIMARY KEY";
    protected final String NOTNULL = "NOT NULL";
    protected final String CREATETABLENEWLINE = " (\n";

    protected final String NEWLINE = "\n";
    protected final String S = " ";
    protected final String C = ",";
    protected final String COMMANEWLINE = ",\n";
    protected final String FINISHTABLE = "\n);";

    //For inserting new player into table
    protected final String VALUES = ")\nVALUES ";
    protected final String WILD = "(?,?,?,?,?,?);";
    protected final String INSERTPLAYER = "INSERT INTO " + TABLE + " (" +
            UUID + C + REALNAME + C + NICKNAME + C + RAWNICKNAME +
            C + PREFIX + C + SUFFIX + VALUES + WILD;

    //For updating
    protected final String UPDATE = "UPDATE " + TABLE + NEWLINE + "SET ";

    //Table creation
    protected final String CREATETABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + CREATETABLENEWLINE;
    protected final String PRIMARYCOLUMN = UUID + S + VARCHAR + S + PRIMARYKEY;
    protected final String SECONDCOLUMN = columnSQLSyntax(REALNAME, VARCHAR, NOTNULL);
    protected final String THIRDCOLUMN = columnSQLSyntax(NICKNAME, VARCHAR, NOTNULL);
    protected final String FOURTHCOLUMN = columnSQLSyntax(RAWNICKNAME, VARCHAR, NOTNULL);
    protected final String FIFTHCOLUMN = columnSQLSyntax(PREFIX, VARCHAR, null);
    protected final String SIXTHCOLUMN = columnSQLSyntax(SUFFIX, VARCHAR, null);
    protected final String CREATETABLETASK = CREATETABLE + PRIMARYCOLUMN + SECONDCOLUMN + THIRDCOLUMN + FOURTHCOLUMN
            + FIFTHCOLUMN + SIXTHCOLUMN + FINISHTABLE;

    protected String columnSQLSyntax(String name, String type, String notNull) {
         return COMMANEWLINE + name + S + type + S + notNull;
    }

    protected String selectSyntax(String fromColumn, String whereColumn) {
        return "SELECT " + fromColumn + " FROM " + TABLE + " WHERE " + whereColumn + " = ?";
    }

    protected String updateSyntax(String column) {
        return UPDATE + column + " = ?\nWHERE " + UUID + " = ?;";
    }


}