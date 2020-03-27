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

public class Suffix implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[0]);
            final String targetuuid = target.getUniqueId().toString();
            final String targetName = SQLite.getPlayerName(targetuuid);
            final String suffix = Color.messageToColor(strings[1]);
            SQLite.setPlayerSuffix(targetuuid, suffix);
            ScoreboardCore.setNewPlayerSuffix(targetName, suffix);
            //ScoreboardCore.addPlayerToGroup(targetName, suffix);
            ScoreboardCore.updateScoreBoard();
            ((Player) commandSender).sendMessage(ChatColor.GREEN + "New suffix: " + suffix);
            //TODO show bossbar of prefix/suffix and show in tab

        }
        return true;
    }
}
