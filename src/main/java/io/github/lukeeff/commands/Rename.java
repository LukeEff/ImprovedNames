package io.github.lukeeff.commands;

import com.mojang.authlib.GameProfile;
import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.database.SQLite;
import io.github.lukeeff.scoreboard.ScoreboardCore;
import io.github.lukeeff.string_modification.Color;
import io.github.lukeeff.version.VersionHandler;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class Rename implements CommandExecutor {

    private static VersionHandler versionHandler;
    private static ImprovedNames plugin;
    private SQLite sql;
    private String playerOffline, tooLongName, usage, success, invalidNickname;
    private final String SELFPERM = "improvednames.rename.self";
    private final String OTHERPERM = "improvednames.rename.others";
    private final String CUSTOMPERM = "improvednames.rename.others";

    //TODO fix tab and prevent tab on colored namaes to prevent client crash
    //TODO clean up this class

    public Rename(ImprovedNames instance, SQLite sql, VersionHandler version) {
        this.versionHandler = version;
        this.sql = sql;
        plugin = instance;
        initializeConfigValues();
    }

    /**
     * Initializes config values from the config file
     */
    private void initializeConfigValues() {
        playerOffline = Utility.getOfflineMessage();
        tooLongName = Utility.getTooLongNameMessage();
        usage = Utility.getRenameUsageMessage();
        success = Utility.getRenameSuccessMessage();
        invalidNickname = Utility.getInvalidNicknameMessage();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender.hasPermission(SELFPERM) && strings.length > 0) {
            final int index = strings.length - 1;
            Player target = decidePlayer(commandSender, strings[0]);
            if(target == null) return true;
            String realTargetName = SQLite.getPlayerFromNickname(target.getName());
            String nickname = decideNickname(commandSender, strings[index], realTargetName); //Get nickname based off of permission
            if(nickname != null) {
                renamePlayer(target, nickname);
                updateName(target);
                commandSender.sendMessage(success + nickname);
            }
        } else commandSender.sendMessage(usage);
        return true;

    }

    /**
     * Check if a player is a valid target for a rename
     * @param commandSender the player sending the command
     * @param playerName the target player name
     * @return null if player doesn't exist, null if player tries changing the name of
     * another player without permission
     */
    private Player decidePlayer(CommandSender commandSender, String playerName) {

        if (commandSender.hasPermission(OTHERPERM) && playerName != null) {
            Player target = Bukkit.getPlayer(playerName);
            if(target == null) commandSender.sendMessage(playerOffline);
            return target; //Null if player not online
        } else {
            return (Player) commandSender;
        }
    }

    /**
     * Checks if a nickname is valid and returns the name if it is
     * @param commandSender the player sending the command
     * @param nickname the nickname of the target
     * @param realTargetName the name of the target
     * @return the name if it's valid in respect to permissions and client
     */
    private String decideNickname(CommandSender commandSender, String nickname, String realTargetName) {
        nickname = Color.messageToColor(nickname);
        if (nickname.length() > 16) {
            commandSender.sendMessage(tooLongName);
            return null; //Crashes client when name longer than 16
        } else if (isPlayerName(realTargetName, nickname) || commandSender.hasPermission(CUSTOMPERM)) {
            return nickname; //Valid nickname
        } else {
            commandSender.sendMessage(invalidNickname);
          return null; //Player tried to make custom nickname without permission
        }

    }

    /**
     * Check if a nickname is the same as the real player name
     * @param realTargetName the real name of the player
     * @param nickname the nickname
     * @return true if they are equal
     */
    private boolean isPlayerName(String realTargetName, String nickname) {
        String strippedName = SQLite.rawify(realTargetName);
        String strippedNickname = SQLite.rawify(nickname);
        return strippedName.equals(strippedNickname);
    }

    /**
     * Rename a player
     * @param player the player being renamed
     * @param newName the new name
     */
    private void renamePlayer(Player player, String newName) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "registering name to: " + newName);
        setPlayerName(player, newName);
        registerNickname(player, newName);
        changeNameTag(player, newName);
    }

    /**
     * Change the name tag of a player
     * @param player the player target
     * @param newName the new name tag
     */
    public static void changeNameTag(Player player, String newName) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        GameProfile profile = p.getProfile();
        versionHandler.setNameTag(profile, newName);
    }

    /**
     * Set the player display name and PlayerList name
     * @param player the player target
     * @param newName the new name
     */
    public static void setPlayerName(Player player, String newName) {
        String uuid = player.getUniqueId().toString();
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "setting name to: " + newName);
        ScoreboardCore.addPlayerToGroup(SQLite.getPlayerName(uuid), newName);
        player.setPlayerListName(newName + ChatColor.RESET);
        player.setDisplayName(newName + ChatColor.RESET);
    }

    /**
     * Updates the nametag of a player. Required after changing username or a player will have to relog to see
     * nametag modifications
     * @param player the player who is having the nametag changed
     */
    public static void updateName(Player player) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.equals(player)) {
                continue;
            }
            p.hidePlayer(plugin, player);
            p.showPlayer(plugin, player);
        }
        ScoreboardCore.updateScoreBoard();
    }

    /**
     * Registers the param nickname with the param player in the sql database
     * @param player the target player
     * @param nickname the new nickname
     */
    private void registerNickname(Player player, String nickname) {
        String uuid = player.getUniqueId().toString();
        sql.setPlayerNickname(uuid, nickname);
    }

}
