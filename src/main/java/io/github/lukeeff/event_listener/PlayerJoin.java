package io.github.lukeeff.event_listener;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.commands.Rename;
import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.scoreboard.ScoreboardCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoin implements Listener {

    ImprovedNames plugin;
    SQLite sql;

    public PlayerJoin(ImprovedNames instance) {
        plugin = instance;
        sql = plugin.sql;
    }

    /**
     * Instructions called when a player joins the server
     * @param event the event that is being listened to
     * @throws SQLException thrown when SQL server returns a warning or error
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        final Player player = event.getPlayer();
        final String uuid = player.getUniqueId().toString();
        addPlayerToDatabase(uuid, player.getName());
        final String realName = sql.getPlayerName(uuid);
        final String nickName = sql.getPlayerNickname(uuid);
        final String prefix = sql.getPrefix(uuid);
        final String suffix = sql.getSuffix(uuid);
        ScoreboardCore.updateScoreBoard();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Pre/Suf: " + prefix + suffix);
        handlePlayerGroup(realName, nickName, prefix, suffix);

        event.setJoinMessage(ChatColor.BLUE + nickName + ChatColor.BLUE + " joined the game");
        setPlayerName(player, nickName);
    }

    private void handlePlayerGroup(String realName, String nickname, String prefix, String suffix) {
        if(ScoreboardCore.teamExists(realName)) {
            return;
        } else {
            ScoreboardCore.createNewPlayerGroup(realName, prefix, suffix);
            ScoreboardCore.addPlayerToGroup(realName, nickname);
        }
    }

    private void addPlayerToDatabase(String uuid, String name) {
        if(!sql.inDatabase(uuid)) {
            sql.addPlayer(uuid, name, name, null, null);
    }}

    /**
     * Set the player name to the nickname stored in the database
     * @param player the player being effected
     * @param nickName the name from the sql database
     */
    private void setPlayerName(Player player, String nickName) {
        Rename.changeNameTag(player, nickName);
        Rename.setPlayerName(player, nickName);
    }

}
