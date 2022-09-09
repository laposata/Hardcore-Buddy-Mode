package com.dreamtea.mixin;

import com.dreamtea.imixin.ISpectate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
  @Shadow @Final protected ServerPlayerEntity player;

  @Shadow public abstract boolean changeGameMode(GameMode gameMode);

  @Inject(method = "changeGameMode", at = @At("RETURN"))
  public void onGamemodeChange(GameMode gameMode, CallbackInfoReturnable<Boolean> cir){
    if(cir.getReturnValue() && this.player instanceof ISpectate spectator){
      spectator.getSpectateManager().trackDeadAndSpectate();
    }
  }
}
