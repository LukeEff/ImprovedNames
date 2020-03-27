package io.github.lukeeff.commands;

import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.scoreboard.ScoreboardCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearPrefix implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            String targetName = strings[0];
            Player targetPlayer = Bukkit.getPlayer(targetName);
            String uuid = targetPlayer.getUniqueId().toString();
            String groupName = SQLite.getPlayerName(uuid);
            SQLite.setPlayerPrefix(uuid, null);
            ScoreboardCore.setNewPlayerPrefix(groupName, ""); //TODO Just grab it from the database
            ScoreboardCore.updateScoreBoard();
            ((Player) commandSender).sendMessage(ChatColor.GREEN + "Success. Removed prefix from player " + groupName);
        }
        return true;
    }
}
