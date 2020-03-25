package io.github.lukeeff.database;

abstract class SQLiteSyntax {

    protected final String createTable = "CREATE TABLE IF NOT EXISTS ";
    protected final String newLine = "\n";
    protected final String createTableNewLine = " (\n";
    protected final String space = " ";
    protected final String commaNewLine = ",\n";
    protected final String concludeCreateTable = "\n);";
}