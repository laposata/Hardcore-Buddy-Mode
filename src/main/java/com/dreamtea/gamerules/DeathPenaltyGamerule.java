package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class DeathPenaltyGamerule {
  public static final String TOTEM_REPEAT_REDUCTION_RULE = "totemRepeatReduction";
  public static GameRules.Key<DoubleRule> TOTEM_REPEAT_REDUCTION;

  public static void registerGamerule() {
    TOTEM_REPEAT_REDUCTION = GameRuleRegistry.register(TOTEM_REPEAT_REDUCTION_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(.3));
  }
}
