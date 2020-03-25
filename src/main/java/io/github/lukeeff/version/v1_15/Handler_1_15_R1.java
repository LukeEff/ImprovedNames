package io.github.lukeeff.version.v1_15;

import com.mojang.authlib.GameProfile;
import io.github.lukeeff.version.VersionHandler;

import java.lang.reflect.Field;

public class Handler_1_15_R1 implements VersionHandler {

    /**
     * Changes the name tag of the player to a nickname
     * @param profile the GameProfile of the player that is having the name changed
     * @param nickname the name that will be replacing the current tag
     */
    @Override
    public void setNameTag(GameProfile profile, String nickname) {

        try {
            Field nameField = profile.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile, nickname);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
