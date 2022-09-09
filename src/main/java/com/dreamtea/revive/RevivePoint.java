package com.dreamtea.revive;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public record RevivePoint(BlockPos point, float yaw, ServerWorld world) {

  public RevivePoint(ServerWorld world){
    this(world.getSpawnPos(), 0, world);
  }

  public RevivePoint location(BlockPos pos){
    return new RevivePoint(pos, this.yaw, this.world);
  }

  public RevivePoint yaw(float yaw){
    return new RevivePoint(this.point, yaw, this.world);
  }
  public void respawnPlayer(ServerPlayerEntity player){
    player.changeGameMode(GameMode.SURVIVAL);
    player.setWorld(world);
    player.refreshPositionAndAngles(point, yaw, 0);
  }
}
