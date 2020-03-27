package io.github.lukeeff.commands;

import io.github.lukeeff.config.Utility;
import io.github.lukeeff.database.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearName implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1) {
            String targetName = strings[0];
            Player targetPlayer = Bukkit.getPlayer(targetName);
            String uuid = targetPlayer.getUniqueId().toString();
            String realName = SQLite.getPlayerName(uuid);
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + realName);
            SQLite.setPlayerNickname(uuid, realName);
            Rename.changeNameTag(targetPlayer, realName);
            Rename.updateName(targetPlayer);
            Rename.setPlayerName(targetPlayer, realName);
            commandSender.sendMessage(Utility.getClearedNicknameSuccessMessage() + targetName);
        }
        return true;
    }
}
