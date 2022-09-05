package com.dreamtea.mixin;

import com.dreamtea.entity.DeathBeaconBlockEntity;
import com.dreamtea.gamerules.DeathBeaconGamerule;
import com.dreamtea.gamerules.DeathChunkloadGamerule;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

  @Inject(method = "tick", at = @At("HEAD"))
  public void tickDeadChunks(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
//    DeathChunkloadGamerule.tick((ServerWorld) (Object) this);
//    DeathBeaconGamerule.tick();
  }
}
