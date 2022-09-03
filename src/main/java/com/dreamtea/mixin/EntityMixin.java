package com.dreamtea.mixin;

import com.dreamtea.imixin.ISetDimensions;
import com.dreamtea.imixin.ISpectate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements ISetDimensions {

  @Shadow private EntityDimensions dimensions;
  @Shadow @Final
  private EntityType<?> type;
  @Shadow private float standingEyeHeight;

  @Shadow public abstract double getX();

  @Shadow public abstract double getY();

  @Shadow public abstract double getZ();

  EntityDimensions overrideDimensions;


  @Inject(method = "move", at= @At("HEAD"))
  public void trackHitboxOnMove(MovementType movementType, Vec3d movement, CallbackInfo ci){
    if(this instanceof ISpectate p){
      p.getSpectateManager().exist(movement);
    }
  }

  @Override
  public float getOverrideEyeHeight(){
    return overrideDimensions != null? -.1f : -1;
  }

  @Override
  public void setDimensionOverride(float width, float height, boolean fixed){
    overrideDimensions = new EntityDimensions(width, height, fixed);
    this.dimensions = overrideDimensions;
    this.standingEyeHeight = getOverrideEyeHeight();
  }

  @Override
  public void setDimensionOverride(EntityDimensions overriding){
    overrideDimensions = overriding;
    if(overriding == null){
      this.dimensions = this.type.getDimensions();
    } else {
      this.dimensions = overrideDimensions;
    }
    this.standingEyeHeight = this.dimensions.height *.85f;
  }

  @Inject(method = "getDimensions", at= @At("HEAD"), cancellable = true)
  public void setDimensionAsNeeded(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir){
    if(overrideDimensions != null){
      this.dimensions = overrideDimensions;
      cir.setReturnValue(overrideDimensions);
    }
  }

  @Inject(method = "calculateBoundingBox", at = @At("HEAD"), cancellable = true)
  public void calcBoundingBoxProperly(CallbackInfoReturnable<Box> cir){
    setDimensionOverride(this.overrideDimensions);
    if(((Entity)(Object)this) instanceof ServerPlayerEntity sp && sp.interactionManager != null){
      if(sp.isSpectator()){
        cir.setReturnValue(this.dimensions.getBoxAt(this.getX(), this.getY() + 1, this.getZ()));
      }
    }
  }
}

