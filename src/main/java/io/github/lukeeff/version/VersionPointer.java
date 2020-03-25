package io.github.lukeeff.version;

import io.github.lukeeff.ImprovedNames;
import io.github.lukeeff.config.Utility;
import io.github.lukeeff.version.v1_15.Handler_1_15_R1;
import io.github.lukeeff.version.v1_15.Handler_1_15_R2;
import org.bukkit.Bukkit;

public class VersionPointer {

    static ImprovedNames plugin;
    static String wrongVersionPath;

    private static void instantiateValues(ImprovedNames instance) {
        plugin = instance;
        wrongVersionPath = Utility.wrongVersionPath;
    }

    /**
     * Handles the version of the server to ensure the correct NMS version is ran
     */
    public static void handleVersion(ImprovedNames instance) {
        instantiateValues(instance);
        final String version = getVersion();
        switch(version) {
            case "v1_15_R1":
                plugin.versionHandler = new Handler_1_15_R1();
                break;
            case "v1_15_R2":
                plugin.versionHandler = new Handler_1_15_R2();
                break;
            default:
                incompatibleVersion();
        }
    }

    /**
     * Called when an incompatible version of the game is coupled with this plugin and will disable the plugin.
     */
    private static void incompatibleVersion() {
        final String wrongVersionMessage = Utility.getConfigStringInColor(wrongVersionPath);
        Bukkit.getConsoleSender().sendMessage(wrongVersionMessage);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    /**
     * Gets the version of the server as the following "vX_Y_RZ" with x y z being version numbers. For example,
     * version 1.15.1 would return "v1_15_R1"
     * @return the version of the server.
     */
    private static String getVersion() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }



}
