package io.github.lukeeff.version;

import com.mojang.authlib.GameProfile;

public interface VersionHandler {

    public void setNameTag(GameProfile profile, String nickname);

}
