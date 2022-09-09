package com.dreamtea.mixin;

import com.dreamtea.gamerules.DeathBeaconGamerule;
import com.dreamtea.gamerules.DeathChunkloadGamerule;
import com.dreamtea.gamerules.TotemDropOnDeathGamerule;
import com.dreamtea.imixin.ISpectate;
import com.dreamtea.spectator.SpectateManager;
import com.dreamtea.spectator.SpectatorHitbox;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ISpectate {

  private SpectateManager spectator;

  @Shadow @Final
  public ServerPlayerInteractionManager interactionManager;

  public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
    super(world, pos, yaw, gameProfile, publicKey);
  }

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

  @Inject(method = "onDisconnect", at = @At("RETURN"))
  public void onDisconnectRemoveSpectate(CallbackInfo ci){
    spectator.killHitbox();
  }

  @Inject(method = "onDeath", at = @At("RETURN"))
  public void onDeathAbideByRules(DamageSource damageSource, CallbackInfo ci){
    ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    List<ItemStack> drops = TotemDropOnDeathGamerule.dropTotem(player);
    for(ItemStack drop: drops){
      ((ServerPlayerEntity)(Object) this).dropStack(drop);
    }
//    DeathChunkloadGamerule.keepChunksLoaded(player);
//    DeathBeaconGamerule.summonBeacon(player);
  }

  @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
  public void dontSpectate(Entity target, CallbackInfo ci){
    if(this.interactionManager.getGameMode() == GameMode.SPECTATOR && target instanceof SpectatorHitbox){
      ci.cancel();
    }
  }
  @Override
  public EntityDimensions getDimensions(EntityPose pose){
    if(spectator.isActive()){
      return new EntityDimensions(1f, 1f, true);
    }
    return super.getDimensions(pose);
  }

  @Override
  public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
    if(spectator != null && spectator.isActive()){
      return -1;
    }
    return super.getActiveEyeHeight(pose, dimensions);
  }


  @Override
  public SpectateManager getSpectateManager(){
    return this.spectator;
  }

}
