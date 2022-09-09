package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

import static com.dreamtea.gamerules.TotemDropConditionsGamerule.TOTEM_DROP_CONDITIONAL;
import static net.minecraft.item.Items.TOTEM_OF_UNDYING;

public class TotemDropOnDeathGamerule {

  public static final String TOTEM_DROP_ON_DEATH_RULE = "totemDropOnDeath";
  public static GameRules.Key<DoubleRule> TOTEM_DROP_ON_DEATH;

  public static void registerGamerule() {
    TOTEM_DROP_ON_DEATH = GameRuleRegistry.register(TOTEM_DROP_ON_DEATH_RULE, GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(0));
  }

  public static List<ItemStack> dropTotem(ServerPlayerEntity dieing){
    double drops = dieing.getWorld().getGameRules().get(TOTEM_DROP_ON_DEATH).get();
    float multiplier = 1f;
    multiplier *= TotemDropDistanceTraveledMinGamerule.conditionMet(dieing);
    multiplier *= TotemDropMinAgeGamerule.conditionMet(dieing);
    multiplier *= TotemDropMinTimeSinceLastDeathGamerule.conditionMet(dieing);
    switch(dieing.getWorld().getGameRules().get(TOTEM_DROP_CONDITIONAL).get()){
      case ALWAYS -> {
        multiplier = 1f;
      }
      case PROPORTIONAL -> {}
      case REQUIRED -> {
        if(multiplier != 1f){
          multiplier = 0;
        }
      }
    }
    drops *= multiplier;
    List<ItemStack> totems  = new ArrayList<>();
    for(;drops >= 1; drops --){
      totems.add(TOTEM_OF_UNDYING.getDefaultStack());
    }
    if(drops >= 0){
      float odds = Random.create().nextBetween(0, 1000)/ 1000f;
      if(odds <= drops){
        totems.add(TOTEM_OF_UNDYING.getDefaultStack());
      }
    }
    return totems;
  }


}
