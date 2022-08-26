package com.dreamtea.spectator;

import com.dreamtea.utils.ParticleSummoner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;


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
    this.hitbox = SpectatorHitbox.summon(player);
    this.hitbox.remove(Entity.RemovalReason.KILLED);
    trackDeadAndSpectate();
  }

  public void exist(Vec3d move){
    if(isActive()){
      hitbox.teleportToEntity();
      player.noClip = false;
      if(player instanceof ServerPlayerEntity sp){
        playerParticle.spawnParticles(sp.getWorld(), move, hitbox.getX(), hitbox.getY(), hitbox.getZ());
      }
    }
  }

  public void trackDeadAndSpectate(){
    if(isActive()){
      this.hitbox.remove(Entity.RemovalReason.KILLED);
      this.hitbox = SpectatorHitbox.summon(this.player);
    } else {
      this.hitbox.remove(Entity.RemovalReason.KILLED);
    }
  }

  public void setDeadSpectate(boolean deadSpectate){
    if(deadSpectate && player instanceof ServerPlayerEntity){
      this.deadSpectate = true;
    } else if (player instanceof ServerPlayerEntity) {
      this.deadSpectate = false;
    }
    trackDeadAndSpectate();
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
