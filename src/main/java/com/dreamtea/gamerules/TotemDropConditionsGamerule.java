package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;

import static com.dreamtea.gamerules.TotemDropConditionsGamerule.DropConditionals.PROPORTIONAL;

public class TotemDropConditionsGamerule {

  public enum DropConditionals{
    IGNORE,
    PROPORTIONAL,
    REQUIRED
  }
  public static final String TOTEM_DROP_CONDITIONAL_RULE = "totemDropCondition";
  public static GameRules.Key<EnumRule<DropConditionals>> TOTEM_DROP_CONDITIONAL;

  public static void registerGamerule() {
    TOTEM_DROP_CONDITIONAL = GameRuleRegistry.register(TOTEM_DROP_CONDITIONAL_RULE, GameRules.Category.PLAYER, GameRuleFactory.createEnumRule(PROPORTIONAL, DropConditionals.values()));
  }

}
