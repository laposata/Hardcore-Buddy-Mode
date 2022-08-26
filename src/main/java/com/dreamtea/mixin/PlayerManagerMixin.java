package com.dreamtea.mixin;

import com.dreamtea.imixin.IManageDeadSpectators;
import com.dreamtea.imixin.ISpectate;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements IManageDeadSpectators {

  @Shadow @Final private Map<UUID, ServerPlayerEntity> playerMap;

  @Shadow protected abstract void savePlayerData(ServerPlayerEntity player);

  @Override
  public boolean willDeadSpectate(GameProfile profile) {
    ISpectate player = (ISpectate) playerMap.getOrDefault(profile.getId(), null);
    return player != null && player.getSpectateManager().getDeadSpectate();
  }

  @Override
  public boolean setDeadSpectate(GameProfile profile, boolean deadSpectate) {
    ISpectate player = (ISpectate) playerMap.getOrDefault(profile.getId(), null);
    if(player != null && player.getSpectateManager().getDeadSpectate() != deadSpectate){
      player.getSpectateManager().setDeadSpectate(deadSpectate);
      savePlayerData(playerMap.get(profile.getId()));
      return true;
    }
    return false;
  }

  @Inject(method = "respawnPlayer", at = @At("RETURN"))
  public void setAliveOnRespawn(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir){
    if(player instanceof ISpectate spectate){
      spectate.getSpectateManager().setDeadSpectate(alive);
    }
  }
}
