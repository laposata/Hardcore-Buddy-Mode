package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class WorldSpecificDropGamerules {
  public static final String OVERWORLD_DROP_MODIFIER_RULE = "totemOverworldDrop";
  public static GameRules.Key<DoubleRule> OVERWORLD_DROP_MODIFIER;

  public static final String NETHER_DROP_MODIFIER_RULE = "totemNetherDrop";
  public static GameRules.Key<DoubleRule> NETHER_DROP_MODIFIER;

  public static final String END_DROP_MODIFIER_RULE = "totemEndDrop";
  public static GameRules.Key<DoubleRule> END_DROP_MODIFIER;
  public static void registerGamerule() {
    OVERWORLD_DROP_MODIFIER = GameRuleRegistry.register(OVERWORLD_DROP_MODIFIER_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1));
    NETHER_DROP_MODIFIER = GameRuleRegistry.register(NETHER_DROP_MODIFIER_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1));
    END_DROP_MODIFIER = GameRuleRegistry.register(END_DROP_MODIFIER_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(0));
  }


}
