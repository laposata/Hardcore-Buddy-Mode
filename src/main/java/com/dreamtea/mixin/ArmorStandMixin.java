package com.dreamtea.mixin;

import com.dreamtea.spectator.SpectatorHitbox;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public class ArmorStandMixin {

  @Inject(method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;", at = @At("RETURN"), cancellable = true)
  public void getDimensionsWhenHitbox(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir){
    if((ArmorStandEntity)(Object)this instanceof SpectatorHitbox spectatorHitbox){
      cir.setReturnValue( spectatorHitbox.getDimensions(null));
    }
  }
}
