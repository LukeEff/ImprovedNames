package io.github.lukeeff.scoreboard;

import io.github.lukeeff.config.Utility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class ScoreboardCore {

    private static Scoreboard scoreboard;
    private Team group;
    private String groupName;
    private String groupPrefix;
    private String groupSuffix;

    public ScoreboardCore() {
        initializeScoreboard();
    }

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }

    public static boolean teamExists(String teamName) {
        return scoreboard.getTeam(teamName) != null;
    }

    private void initializeScoreboard() {
        ScoreboardManager board = Bukkit.getScoreboardManager();
        scoreboard = board.getNewScoreboard();
        initializeConfigGroups(getGroupsFromConfig());
        printTeamNames();
    }

    private List<List<String>> getGroupsFromConfig() {
        return (List<List<String>>) Utility.getGroupList();
    }

    private void printTeamNames() {
        for(Team team : scoreboard.getTeams()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Group name: " + team.getName());
        }

    }

    /**
     * Sets a prefix to a group
     */
    private void setPrefixToGroup() {
        try {
            group.setPrefix(groupPrefix);
        } catch (IllegalArgumentException invalidPrefix) {
            sendInvalidTeamConfigMessage();
            //We just won't set a prefix!
        }
    }

    /**
     * Sets a suffix to a group
     */
    private void setSuffixToGroup() {
        try {
            group.setSuffix(groupSuffix);
        } catch (IllegalArgumentException invalidPrefix) {
            sendInvalidTeamConfigMessage();
            //We just won't set a suffix!
        }
    }

    /**
     * Registers teams from config
     * @param groups the groups from config being registered
     */
    private void initializeConfigGroups(List<List<String>> groups) {

        for(List<String> groupElements : groups) {
            setTeamAttributes(groupElements);
            registerTeam();
            setPrefixToGroup();
            setSuffixToGroup();
        }
    }

    /**
     * Initialize a brand new group for a player
     * @param playerName the name of the player that has a custom prefix
     * @param groupPrefix the prefix of the group
     * @param groupSuffix the suffix of the group
     */
    public static void createNewPlayerGroup(String playerName, String groupPrefix, String groupSuffix) {
        Team group = scoreboard.registerNewTeam(playerName);
        setPrefix(group, groupPrefix);
        setSuffix(group, groupSuffix);
        group.addEntry(playerName);
    }

    public static void updateScoreBoard() {
        for (Player online: Bukkit.getOnlinePlayers()) {
            online.setScoreboard(scoreboard);
        }
    }

    public static void addPlayerToGroup(String groupName, String name) {
        Team group = scoreboard.getTeam(groupName);
        group.addEntry(name);
    }

    public static void setNewPlayerPrefix(String playerName, String prefix) {
        Team group = scoreboard.getTeam(playerName);
        setPrefix(group, prefix);
    }

    public static void setNewPlayerSuffix(String playerName, String suffix) {
        Team group = scoreboard.getTeam(playerName);
        setSuffix(group, suffix);

    }



    private static void setPrefix(Team group, String prefix) {
        try {
            group.setPrefix(prefix);
        } catch (IllegalArgumentException invalidPrefix) {
            //We just won't set a prefix!
        }
    }

    private static void setSuffix(Team group, String suffix) {
        try {
            group.setSuffix(suffix);
        } catch (IllegalArgumentException invalidPrefix) {
            //We just won't set a suffix!
        }
    }

    private void setTeamAttributes(List<String> elements) {
        try {
            groupName = elements.get(0);
            groupPrefix = elements.get(1);
            groupSuffix = elements.get(2);
        } catch (ArrayIndexOutOfBoundsException e) {
            //Thrown when player didn't specify a prefix, suffix, or name.
            sendInvalidTeamConfigMessage();
        }
    }

    private void sendInvalidTeamConfigMessage() {
        Bukkit.getConsoleSender().sendMessage(Utility.getInvalidTeamConfigMessage());
    }

    private void registerTeam() {
        try {
            group = scoreboard.registerNewTeam(groupName.toLowerCase());
        } catch (IllegalArgumentException invalidName) {
            sendInvalidTeamConfigMessage();
            //invalidName.printStackTrace();
        }
    }


}
