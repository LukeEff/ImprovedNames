package io.github.lukeeff.event_listener;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.commands.Rename;
import io.github.lukeeff.database.SQLite;
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
        sql.checkPlayerData(player);
        final String nickName = sql.getPlayerNickname(uuid);
        event.setJoinMessage(ChatColor.BLUE + nickName + ChatColor.BLUE + " joined the game");
        setPlayerName(player, nickName);
    }

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
