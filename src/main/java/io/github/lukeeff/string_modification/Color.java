package io.github.lukeeff.string_modification;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Color implements Listener {

    private static ImprovedNames plugin;

    public Color(ImprovedNames instance) {
        plugin = instance;
    }

    @EventHandler
    private void detectSymbol(AsyncPlayerChatEvent event) {
        final String message = event.getMessage();
        if(containsColorSymbol(message)) {
            replaceMessage(event, messageToColor(message));
        }
    }

    /**
     * Checks to see if a message contains a color symbol
     * @param message the message being checked
     * @return true if the message contains the symbol
     */
    private boolean containsColorSymbol(String message) {
        String colorSymbol = getColorSymbol();
        return message.contains(colorSymbol);
    }

    /**
     * Sets the message sent by a player to a new message
     * @param event the event that is intercepting the message
     * @param newMessage the new message that will be sent
     */
    private void replaceMessage(AsyncPlayerChatEvent event, String newMessage) {
        event.setMessage(newMessage);
    }

    /**
     * Using the & symbol, this method will allow players to use color in commands.
     * @param message the string that will be converted to color
     * @return the colored string
     */
    public static String messageToColor(String message) {
        final char colorSymbol = getColorSymbol().charAt(0);
        final char[] messageChar = message.toCharArray();
        final StringBuilder newMessage = new StringBuilder();
        final int messageLength = messageChar.length;


        for(int i = 0; i < messageLength; i++) {
            Character c = messageChar[i];
            if(((c.equals(colorSymbol)) && (ChatColor.getByChar(messageChar[i + 1]) != null))) {
                i++;
                newMessage.append(ChatColor.getByChar(messageChar[i]));
            } else {
                newMessage.append(c);
            }
        }
        return newMessage.toString();
    }

    /**
     * Get a string from the config file
     * @param path the path to the string
     * @return a string that is the target of a path
     */
    public static String getConfigString(String path) {
        return Utility.getConfigString(path);
    }

    /**
     * Gets the color symbol from the config
     * @return the color symbol found in the config
     */
    private static String getColorSymbol() {
        final String path = Utility.colorPath;
        return getConfigString(path);
    }


}
