package io.github.lukeeff;

import io.github.lukeeff.commands.*;
import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.event_listener.PlayerJoin;
import io.github.lukeeff.scoreboard.ScoreboardCore;
import io.github.lukeeff.string_modification.Color;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.version.VersionHandler;
import io.github.lukeeff.version.VersionPointer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

//TODO Use protocollib for nametags instead of modifying gameprofile

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
        initializeScoreboard();
    this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    this.getServer().getPluginManager().registerEvents(new Color(this), this);
    this.getCommand("rename").setExecutor(new Rename(this, sql, versionHandler));
    this.getCommand("realname").setExecutor(new RealName(this, sql));
    this.getCommand("prefix").setExecutor(new Prefix());
    this.getCommand("suffix").setExecutor(new Suffix());
    this.getCommand("clearname").setExecutor(new ClearName());
    this.getCommand("clearprefix").setExecutor(new ClearPrefix());
    this.getCommand("clearsuffix").setExecutor(new ClearSuffix());

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

    private void initializeScoreboard() {
        new ScoreboardCore();
    }

}
