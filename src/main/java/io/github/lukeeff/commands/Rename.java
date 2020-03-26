package io.github.lukeeff.commands;

import com.mojang.authlib.GameProfile;
import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.database.SQLite;
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


    static VersionHandler versionHandler;
    ImprovedNames plugin;
    SQLite sql;
    String newName;
    private String playerOffline, tooLongName, usage, success;
    boolean selfPerm, otherPerm, customPerm;

    //TODO fix tab and prevent tab on colored namaes to prevent client crash
    //TODO clean up this class

    public Rename(ImprovedNames instance, SQLite sql, VersionHandler version, Color color) {
        this.versionHandler = version;
        this.sql = sql;
        plugin = instance;
        initializeConfigValues();

    }

    /**
     * Initializes config values from the config file
     */
    private void initializeConfigValues() {
        playerOffline = Utility.getConfigStringInColor("offline-message");
        tooLongName = Utility.getConfigStringInColor("name-too-long");
        usage = Utility.getConfigStringInColor("rename-usage");
        success = Utility.getConfigStringInColor("rename-success");

    }

    //TODO this method does WAY too much. Clean it up
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        selfPerm = commandSender.hasPermission("improvednames.rename.self");
        otherPerm = commandSender.hasPermission("improvednames.rename.others");
        customPerm = commandSender.hasPermission("improvednames.rename.custom");
        Player player = (Player) commandSender;
        Player sender = (Player) commandSender;
        if(selfPerm && strings.length > 0) {
            int index = strings.length - 1;
            newName = Color.messageToColor(getNewName(player, strings[index]));

            if (otherPerm && strings.length > 1) {
                player = getPlayer(strings[0]);
                if(player == null) {
                    sender.sendMessage(playerOffline);
                    return true;
                }
            }
            if (!customPerm && !isPlayerName(player, newName)) {
                return true;
            }

            sender.sendMessage(success + newName);
                renamePlayer(player, newName);
                updateName(player);
            return true;
        }
        player.sendMessage(usage);
        return true;
    }

    private boolean isPlayerName(Player player, String nickName) {
        String playerName = player.getName();
        String strippedNickName = new String(nickName);
        ChatColor.stripColor(strippedNickName);
        if (playerName.equals(strippedNickName)) {
            return true;
        }
        return false;
    }

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
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "setting name to: " + newName);
        player.setPlayerListName(newName + ChatColor.RESET);
        player.setDisplayName(newName + ChatColor.RESET);
    }

    private void usage(Player player) {
        player.sendMessage(usage);
    }

    /**
     * Will check if a name is valid for the game and return it if it is
     * @param player the player target
     * @param name the name
     * @return param player name if null, param player name if too long, the nickname if valid
     */
    private String getNewName(Player player, String name) { //TODO rename this method and split it up maybe
        if(name.equals(null)) {
            return player.getDisplayName();
        } else if (name.length() > 16) {
            player.sendMessage(player.getName());
            return player.getName();
        } else {
            return name;
        }

    }

    /**
     * Gets an online player through their name
     * @param name the name of the player
     * @return the player with the param name or null if the player doesn't exist
     */
    private Player getPlayer(String name) {
        Player targetPlayer = Bukkit.getPlayer(name);

        if(targetPlayer == null) {
            return null;
        } else {
            return targetPlayer;
        }


    }

    /**
     * Updates the nametag of a player. Required after changing username or a player will have to relog to see
     * nametag modifications
     * @param player the player who is having the nametag changed
     */
    private void updateName(Player player) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.equals(player)) {
                continue;
            }
            p.hidePlayer(plugin, player);
            p.showPlayer(plugin, player);
        }

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
