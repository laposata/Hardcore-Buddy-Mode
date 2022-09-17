package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;

public class TotemDropDistanceTraveledMinGamerule {

  public static final String TOTEM_DROP_ON_DEATH_DISTANCE_RULE = "totemMinDistance";
  public static GameRules.Key<GameRules.IntRule> TOTEM_DROP_ON_DEATH_DISTANCE;

  public static void registerGamerule() {
    TOTEM_DROP_ON_DEATH_DISTANCE = GameRuleRegistry.register(TOTEM_DROP_ON_DEATH_DISTANCE_RULE, GameRules.Category.PLAYER, GameRuleFactory.createIntRule(0, 0));
  }

  /**
   * Checks if the player has been alive long enough based on the game rule
   * @param spe the player being checked
   * @return returns a float value between 0 and 1, 1 means the players has lived long enough
   * any other value represents the portion of the gamerule they have lived
   */
  public static float conditionMet(ServerPlayerEntity spe){
    float dist = spe.distanceTraveled;
    double minDist = spe.getWorld().getGameRules().get(TOTEM_DROP_ON_DEATH_DISTANCE).get();
    if(minDist == 0) {
      return 1f;
    }
    return (float) Math.min(1f, dist / minDist);
  }

}
