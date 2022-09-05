package com.dreamtea.gamerules;

import com.dreamtea.entity.DeathBeaconBlockEntity;
import com.dreamtea.mixin.BeaconBlockEntityMixin;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;


public class DeathBeaconGamerule {

  public static final String DEATH_BEACON_RULE = "beaconBeamOnDeath";
  public static GameRules.Key<GameRules.IntRule> DEATH_BEACON;

  public static List<DeathBeaconBlockEntity> beacons = new ArrayList<>();

  public static void registerGamerule(){
    DEATH_BEACON = GameRuleRegistry.register(DEATH_BEACON_RULE, GameRules.Category.PLAYER, GameRuleFactory.createIntRule(0, 0, 300));
  }

  public static void summonBeacon(ServerPlayerEntity dieingPlayer){
    int secsLoaded = dieingPlayer.getWorld().getGameRules().get(DEATH_BEACON).get();
    if(secsLoaded > 0){
      DeathBeaconBlockEntity deathBeacon = new DeathBeaconBlockEntity(dieingPlayer.getBlockPos());
      WorldChunk chunk = (WorldChunk) dieingPlayer.getWorld().getChunk(dieingPlayer.getBlockPos());
      chunk.addBlockEntity(deathBeacon);
      beacons.add(deathBeacon);
    }
  }

  public static void tick(){
    for(DeathBeaconBlockEntity dbbe: beacons){
      dbbe.possiblyKill();
      Vec3d vec3Pos = new Vec3d(dbbe.getPos().getX(), dbbe.getPos().getY(), dbbe.getPos().getZ());
      if(dbbe.getWorld() != null && (dbbe.isRemoved()|| !dbbe.getWorld().getNonSpectatingEntities(PlayerEntity.class, Box.of(vec3Pos, 7,7,7)).isEmpty())){
        dbbe.markRemoved();
      }
      if(dbbe.isRemoved()){
        beacons.remove(dbbe);
      }
    }
  }

}
