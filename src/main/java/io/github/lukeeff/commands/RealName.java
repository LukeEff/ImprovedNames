package io.github.lukeeff.commands;

import io.github.lukeeff.ImprovedNames;

import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.string_modification.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealName implements CommandExecutor {

    ImprovedNames plugin;
    SQLite sql;

    public RealName(ImprovedNames instance, SQLite sql) {
        plugin = instance;
        this.sql = plugin.sql;
    }

    //TODO deal with players that have identical nicknames
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) {
            Player player = (Player) commandSender;
            String nickName = Color.messageToColor(strings[0]);
            String trueName = sql.getPlayerFromNickname(nickName);
            player.sendMessage(ChatColor.AQUA + "Players with the nickname " + nickName + ": " + trueName);
        }
        return true;
    }
}
