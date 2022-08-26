package com.dreamtea.imixin;

import com.mojang.authlib.GameProfile;

public interface IManageDeadSpectators {
  boolean willDeadSpectate(GameProfile profile);
  boolean setDeadSpectate(GameProfile profile, boolean op);
}
