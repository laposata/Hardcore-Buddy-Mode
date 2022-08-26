package com.dreamtea.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface ParticleCond {
  boolean shouldSpawn(ServerPlayerEntity player, Vec3d movement);
}
