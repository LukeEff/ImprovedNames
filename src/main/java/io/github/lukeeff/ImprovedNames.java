package io.github.lukeeff;

import io.github.lukeeff.commands.RealName;
import io.github.lukeeff.commands.Rename;
import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.event_listener.PlayerJoin;
import io.github.lukeeff.string_modification.Color;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.version.VersionHandler;
import io.github.lukeeff.version.VersionPointer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ImprovedNames extends JavaPlugin {

    private Color color;
    public FileConfiguration config;
    public VersionHandler versionHandler;
    public SQLite sql;

    @Override
    public void onEnable() {
        Utility.plugin = this;
        VersionPointer.handleVersion(this);
        initializeConfig();
        sql = new SQLite(this);
        color = new Color(this);

    this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    this.getCommand("rename").setExecutor(new Rename(this, sql, versionHandler
    , color));
    this.getCommand("realname").setExecutor(new RealName(this, sql));

    }

    @Override
    public void onDisable() { }

        //TODO Improve this
    private void initializeConfig() {
        loadConfig();
    }

    private void loadConfig() {
        saveDefaultConfig();
        config = this.getConfig();

    }
}
