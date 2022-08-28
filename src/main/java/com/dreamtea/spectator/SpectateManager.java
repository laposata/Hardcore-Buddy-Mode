package com.dreamtea.spectator;

import com.dreamtea.imixin.IOverrideDimension;
import com.dreamtea.utils.ParticleSummoner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;


public class SpectateManager {

  private boolean deadSpectate;
  ParticleSummoner playerParticle;
  private static final String DEAD_SPECTATE_KEY = "deadSpectate";
  private final PlayerEntity player;
  private SpectatorHitbox hitbox;
  public SpectateManager(PlayerEntity player){
    this.deadSpectate = true;
    this.player = player;
    playerParticle = ParticleSummoner.getRandomParticle();
  }

  public void exist(MovementType movementType, Vec3d move){
    if(isActive()){
      if(hitbox == null && (movementType != MovementType.SELF && move.lengthSquared() < .0001)){
        this.hitbox = new SpectatorHitbox(this.player, (ServerWorld) this.player.getWorld());
      }
      if((hitbox != null && hitbox.getRemovalReason() != null) || move.lengthSquared() > .1){
        discardHitbox();
      }
      player.noClip = false;
      if(player instanceof ServerPlayerEntity sp){
        playerParticle.spawnParticles(sp.getWorld(), move, sp.getX(), sp.getY() + .5, sp.getZ());
      }
    }
  }

  public UUID getHitboxUUID(){
    if(hitbox == null){
      return null;
    }
    return this.hitbox.getUuid();
  }


  public void discardHitbox(){
    if(this.hitbox != null){
      this.hitbox.discard();
    }
    this.hitbox = null;
  }

  public void setDeadSpectate(boolean deadSpectate){
    if(deadSpectate && player instanceof ServerPlayerEntity){
      this.deadSpectate = true;
    } else if (player instanceof ServerPlayerEntity) {
      this.deadSpectate = false;
    }
  }

  public boolean isActive(){
    return getDeadSpectate() && player.isSpectator();
  }

  public boolean getDeadSpectate(){
    return this.deadSpectate;
  }

  public void writeSpectate(NbtCompound nbt){
    nbt.putBoolean(DEAD_SPECTATE_KEY, deadSpectate);
  }

  public void readSpectate(NbtCompound nbt){
    setDeadSpectate(!nbt.contains(DEAD_SPECTATE_KEY) || nbt.getBoolean(DEAD_SPECTATE_KEY));
  }
}
