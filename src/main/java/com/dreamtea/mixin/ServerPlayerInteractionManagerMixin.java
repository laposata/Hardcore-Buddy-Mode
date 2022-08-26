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

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
  @Shadow @Final protected ServerPlayerEntity player;

  @Inject(method = "setGameMode", at = @At("RETURN"))
  public void onGamemodeChange(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci){
    ((ISpectate)this.player).getSpectateManager().trackDeadAndSpectate();
  }
}
