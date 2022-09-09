package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;

public class TotemDropMinAgeGamerule {

  public static final String TOTEM_DROP_ON_DEATH_TOTAL_TIME_RULE = "totemMinMinutes";
  public static GameRules.Key<DoubleRule> TOTEM_DROP_ON_DEATH_TOTAL_TIME;

  public static void registerGamerule() {
    TOTEM_DROP_ON_DEATH_TOTAL_TIME = GameRuleRegistry.register(TOTEM_DROP_ON_DEATH_TOTAL_TIME_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(0, 0));
  }

  /**
   * Checks if the player has been alive long enough based on the game rule
   * @param spe the player being checked
   * @return returns a float value between 0 and 1, 1 means the players has lived long enough
   * any other value represents the portion of the gamerule they have lived
   */
  public static float conditionMet(ServerPlayerEntity spe){
    float playerAge = spe.age / 20f / 60f;
    double minMinutes = spe.getWorld().getGameRules().get(TOTEM_DROP_ON_DEATH_TOTAL_TIME).get();
    if(minMinutes == 0) {
      return 1f;
    }
    return (float) Math.min(1f, playerAge / minMinutes);
  }

}
