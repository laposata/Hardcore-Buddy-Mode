package com.dreamtea.mixin;

import com.dreamtea.imixin.IOverrideDimension;
import com.dreamtea.imixin.ISpectate;
import com.dreamtea.spectator.SpectateManager;
import com.dreamtea.spectator.SpectatorHitbox;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ISpectate {

  private SpectateManager spectator;

  @Inject(method = "<init>", at = @At("RETURN"))
  public void addSpectator(MinecraftServer server, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey, CallbackInfo ci){
    this.spectator = new SpectateManager((PlayerEntity) (Object) this);
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
  public void readOpSpectateNbt(NbtCompound nbt, CallbackInfo ci){
    spectator.readSpectate(nbt);
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
  public void writeOpSpectateNbt(NbtCompound nbt, CallbackInfo ci){
    spectator.writeSpectate(nbt);
  }

  @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setCameraEntity(Lnet/minecraft/entity/Entity;)V"))
  public void dontTakeOverHitbox(ServerPlayerEntity instance, Entity entity){
    if(!(entity instanceof SpectatorHitbox)){
      instance.setCameraEntity(entity);
    }
  }

//  @Override
//  public EntityDimensions getDimensions(EntityPose pose){
//    if(spectator.isActive()){
//      return new EntityDimensions(1f, 1f, true);
//    }
//    return super.getDimensions(pose);
//  }

  @Override
  public SpectateManager getSpectateManager(){
    return this.spectator;
  }
//
//  @Override
//  public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
//    Entity e = this;
//    if(e instanceof IOverrideDimension dims && dims.isDimensionOverridden()){
//      return dims.getOverrideEyeHeight();
//    }
//    return super.getActiveEyeHeight(pose, dimensions);
//  }

}
