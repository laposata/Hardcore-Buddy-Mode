package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class TotemDefaultDropChanceGamerule {
  public static final String TOTEM_DROP_ON_DEATH_CHANCE_RULE = "totemDropOnDeathChance";
  public static GameRules.Key<DoubleRule> TOTEM_DROP_ON_DEATH_CHANCE;

  public static void registerGamerule() {
    TOTEM_DROP_ON_DEATH_CHANCE = GameRuleRegistry.register(TOTEM_DROP_ON_DEATH_CHANCE_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1));
  }
}
