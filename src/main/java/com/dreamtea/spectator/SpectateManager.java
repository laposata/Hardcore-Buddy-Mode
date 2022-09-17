package com.dreamtea.spectator;

import com.dreamtea.imixin.ISetDimensions;
import com.dreamtea.utils.ParticleSummoner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;


public class SpectateManager {

  private boolean deadSpectate;
  ParticleSummoner playerParticle;
  private static final String DEAD_SPECTATE_KEY = "deadSpectate";
  private static final String TOTEMS_DROPPED_KEY = "totemsDropped";
  private final PlayerEntity player;
  private SpectatorHitbox hitbox;
  private int totemsDropped = 0;

  public SpectateManager(PlayerEntity player){
    this.deadSpectate = true;
    this.player = player;
    playerParticle = ParticleSummoner.getRandomParticle();
    trackDeadAndSpectate();
    totemsDropped = 0;
  }

  public void dropTotem(){
    this.totemsDropped ++;
  }
  public int getDrops(){
    return this.totemsDropped;
  }
  public void exist(Vec3d move){
    if(isActive()){
      if(hitbox == null || hitbox.getRemovalReason() != null){
        trackDeadAndSpectate();
      }
      player.noClip = false;

      if(player instanceof ServerPlayerEntity sp){
        Vec3d offset = player.getRotationVector();
        offset = offset.multiply(1,0,1);
        offset = offset.normalize().multiply(-1.2);
        offset = offset.add(0, 1, 0);
        offset = offset.add(player.getX(), player.getY(), player.getZ());
        hitbox.teleportToEntity(offset);
        playerParticle.spawnParticles(sp.getWorld(), move, offset.getX(), offset.getY(), offset.getZ());
      }
    }
  }

  public void trackDeadAndSpectate(){
    if(this.hitbox != null){
      killHitbox();
      ((ISetDimensions)this.player).setDimensionOverride(null);
    }
    if(isActive()){
      ((ISetDimensions)this.player).setDimensionOverride(.2f,.8f, true);
      this.hitbox = new SpectatorHitbox(player,  (ServerWorld) this.player.getWorld());
    }
  }

  public void setDeadSpectate(boolean deadSpectate){
    if(deadSpectate && player instanceof ServerPlayerEntity){
      this.deadSpectate = true;
      trackDeadAndSpectate();
    } else if (player instanceof ServerPlayerEntity) {
      this.deadSpectate = false;
      trackDeadAndSpectate();
    }

  }

  public void killHitbox(){
    player.removeAllPassengers();
    if (hitbox != null) {
      hitbox.remove(Entity.RemovalReason.DISCARDED);
      this.hitbox.emitGameEvent(GameEvent.ENTITY_DIE);
      this.hitbox = null;
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
    nbt.putInt(TOTEMS_DROPPED_KEY, totemsDropped);
  }

  public void readSpectate(NbtCompound nbt){
    setDeadSpectate(!nbt.contains(DEAD_SPECTATE_KEY) || nbt.getBoolean(DEAD_SPECTATE_KEY));
    this.totemsDropped = nbt.contains(TOTEMS_DROPPED_KEY) ? nbt.getInt(TOTEMS_DROPPED_KEY) : 0;
  }
}
