package io.github.lukeeff.version.v1_15;

import com.mojang.authlib.GameProfile;
import io.github.lukeeff.version.VersionHandler;

import java.lang.reflect.Field;

public class Handler_1_15_R2 implements VersionHandler {


    @Override
    public void setNameTag(GameProfile profile, String nickname) {

        try {
            Field nameField = profile.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile, nickname);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
