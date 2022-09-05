package com.dreamtea.gamerules;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.tick.TickScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeathChunkloadGamerule{

  public static final String DEATH_CHUNKS_LOAD_RULE = "deathChunksStayLoaded";
  public static GameRules.Key<GameRules.IntRule> DEATH_CHUNKS_LOAD;
  public static Map<ChunkPos, Long> chunksLoaded = new LinkedHashMap<>();
  public static void registerGamerule(){
    DEATH_CHUNKS_LOAD = GameRuleRegistry.register(DEATH_CHUNKS_LOAD_RULE, GameRules.Category.PLAYER, GameRuleFactory.createIntRule(0, 0, 300));
  }

  public static void keepChunksLoaded(ServerPlayerEntity dieingPlayer){
    int secsLoaded = dieingPlayer.getWorld().getGameRules().get(DEATH_CHUNKS_LOAD).get();
    if(secsLoaded > 0){
      ChunkPos center = dieingPlayer.getChunkPos();
      for(int x = -1; x <= 1; x++){
        for(int z = -1; z <= 1; z++){
          if(startLoading(center.x + x, center.z +z, dieingPlayer.getWorld().getTime())){
            setForceLoad(dieingPlayer.getWorld(), center.x + x, center.z + z, true);
          }
        }
      }
    }
  }

  private static boolean startLoading(int x, int z, Long time){
    return chunksLoaded.put(new ChunkPos(x, z), time) == null;
  }

  private static void setForceLoad(ServerWorld world,int x, int z, boolean forceOn){
    world.setChunkForced(x, z, forceOn);
  }

  private static void setForceLoad(ServerWorld world, ChunkPos chunk, boolean forceOn){
    world.setChunkForced(chunk.x, chunk.z, forceOn);
  }

  public static void tick(ServerWorld world){
    for(Map.Entry<ChunkPos, Long> chunk: chunksLoaded.entrySet()){
      if(chunk.getValue() + world.getGameRules().get(DEATH_CHUNKS_LOAD).get() * 20L < world.getTime()){
        chunksLoaded.remove(chunk.getKey());
        setForceLoad(world, chunk.getKey(), false);
      }
    }
  }

}
