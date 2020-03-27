package io.github.lukeeff.commands;

import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.scoreboard.ScoreboardCore;
import io.github.lukeeff.string_modification.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Prefix implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[0]);
            final String targetuuid = target.getUniqueId().toString();
            final String targetName = SQLite.getPlayerName(targetuuid);
            final String prefix = Color.messageToColor(strings[1]);

            SQLite.setPlayerPrefix(targetuuid, prefix);
            ScoreboardCore.setNewPlayerPrefix(targetName, prefix);
            ScoreboardCore.updateScoreBoard();
            ((Player) commandSender).sendMessage(ChatColor.GREEN + "New prefix: " + prefix);
            //TODO show bossbar of prefix/suffix and show in tab

        }


        return true;
    }
}
