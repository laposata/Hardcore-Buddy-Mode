package com.dreamtea.mixin;

import com.dreamtea.imixin.ISpectate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

  @Inject(method = "move", at= @At("HEAD"))
  public void trackHitboxOnMove(MovementType movementType, Vec3d movement, CallbackInfo ci){
    if(this instanceof ISpectate p){
      p.getSpectateManager().exist(movement);
    }
  }
}
