package com.dreamtea.spectator;

import com.dreamtea.revive.Revive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class SpectatorHitbox extends ArmorStandEntity {
  private final Entity entity;

  public SpectatorHitbox(Entity entity, ServerWorld world) {
    super(world, entity.getX(), entity.getY(), entity.getZ());
    this.entity = entity;
    create();

  }

  private void create(){
    this.setInvisible(true);
    this.calculateDimensions();
    this.setPosition(entity.getX(), entity.getY(), entity.getZ());
    this.setNoGravity(true);
    this.setCustomName(Text.of("*"+entity.getDisplayName().getString()+"*"));
    this.setSilent(true);
    this.setCustomNameVisible(true);
    this.setNoDrag(true);
    this.setInvulnerable(true);
    world.spawnEntity(this);
  }

  public void teleportToEntity(Vec3d offset){
    if(offset == null){
      offset = new Vec3d(0,0,0);
    }
    if(entity.getWorld() instanceof ServerWorld serverWorld
      && (World)serverWorld != this.getWorld()){
      this.moveToWorld(serverWorld);
    }
    this.setPosition(entity.getX() + offset.x, entity.getY() + offset.y, entity.getZ() + offset.z);
    this.updatePositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());

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

  @Override
  public EntityDimensions getDimensions(EntityPose pose){
    return new EntityDimensions(2f, -2f, true);
  }

  @Override
  public boolean shouldDropLoot(){
    return false;
  }
}
