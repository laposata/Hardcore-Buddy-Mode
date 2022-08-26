package com.dreamtea.spectator;

import com.dreamtea.revive.Revive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class SpectatorHitbox extends ArmorStandEntity {
  private final Entity entity;
  private boolean active;

  private SpectatorHitbox(Entity entity) {
    super(EntityType.ARMOR_STAND, entity.getWorld());
    this.entity = entity;
    active = false;
    create();
  }

  private void create(){
      //standin.setInvisible(true);
      this.setNoGravity(true);
      this.setCustomName(entity.getDisplayName());
      this.setSilent(true);
      this.setCustomNameVisible(true);
      this.unsetRemoved();
  }

  public void teleportToEntity(){
    if(entity.getWorld() instanceof ServerWorld serverWorld){
      this.moveToWorld(serverWorld);
    }
    this.teleport(entity.getX(), entity.getY(), entity.getZ());
  }

  @Override
  public boolean shouldSave(){
    return false;
  }

  @Override
  public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
    if(player instanceof ServerPlayerEntity serverPlayer && Revive.revivePlayer(this.entity, serverPlayer)){
      return ActionResult.PASS;
    }
    return ActionResult.FAIL;
  }

  @Override
  public void kill() {}

  public static SpectatorHitbox summon(Entity entity){
      SpectatorHitbox hitbox = new SpectatorHitbox(entity);
      entity.getWorld().spawnEntity(hitbox);
      hitbox.teleportToEntity();
      return hitbox;
  }

  @Override
  public EntityDimensions getDimensions(EntityPose pose){
    return new EntityDimensions(1f, 1f, true);
  }

}
