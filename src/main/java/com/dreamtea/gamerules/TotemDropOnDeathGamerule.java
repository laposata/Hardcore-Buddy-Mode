package com.dreamtea.gamerules;

import com.dreamtea.imixin.ISpectate;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dreamtea.gamerules.DeathPenaltyGamerule.TOTEM_REPEAT_REDUCTION;
import static com.dreamtea.gamerules.TotemDefaultDropChanceGamerule.TOTEM_DROP_ON_DEATH_CHANCE;
import static com.dreamtea.gamerules.TotemDropConditionsGamerule.TOTEM_DROP_CONDITIONAL;
import static com.dreamtea.gamerules.WorldSpecificDropGamerules.END_DROP_MODIFIER;
import static com.dreamtea.gamerules.WorldSpecificDropGamerules.NETHER_DROP_MODIFIER;
import static com.dreamtea.gamerules.WorldSpecificDropGamerules.OVERWORLD_DROP_MODIFIER;
import static com.dreamtea.gamerules.WorldSpecificDropGamerules.OVERWORLD_DROP_MODIFIER_RULE;
import static net.minecraft.item.Items.TOTEM_OF_UNDYING;

public class TotemDropOnDeathGamerule {

  public static final String TOTEM_DROP_ON_DEATH_RULE = "totemDropOnDeath";
  public static GameRules.Key<EnumRule<DropOnDeath>> TOTEM_DROP_ON_DEATH;

  enum DropOnDeath{
    /**
     * If there is a chance for a totem to drop it drops
     */
    ALWAYS,
    /**
     * If there is a chance for a totem to drop it will drop at that frequency
     */
    PROBABLE,
    /**
     * A totem will only drop if it is at 100% odds to drop
     */
    CERTAIN
  }
  public static void registerGamerule() {
    TOTEM_DROP_ON_DEATH = GameRuleRegistry.register(TOTEM_DROP_ON_DEATH_RULE, GameRules.Category.PLAYER, GameRuleFactory.createEnumRule(DropOnDeath.PROBABLE));
  }

  private static float getChance(ServerPlayerEntity dieing){
    float multiplier = 1f;
    multiplier *= TotemDropDistanceTraveledMinGamerule.conditionMet(dieing);
    multiplier *= TotemDropMinAgeGamerule.conditionMet(dieing);
    multiplier *= TotemDropMinTimeSinceLastDeathGamerule.conditionMet(dieing);
    int totemsDropped = 0;
    switch(dieing.getWorld().getGameRules().get(TOTEM_DROP_CONDITIONAL).get()){
      case IGNORE -> {
        multiplier = 1f;
      }
      case PROPORTIONAL -> {}
      case REQUIRED -> {
        if(multiplier != 1f){
          multiplier = 0;
        }
      }
    }
    if(dieing instanceof ISpectate spectator){
      totemsDropped = spectator.getSpectateManager().getDrops();
    }
    multiplier -= dieing.getWorld().getGameRules().get(TOTEM_REPEAT_REDUCTION).get() * totemsDropped;
    RegistryKey<World> dimensionType = dieing.getWorld().getRegistryKey();
    if(Objects.equals(dimensionType, World.OVERWORLD)){
      multiplier *= dieing.getWorld().getGameRules().get(OVERWORLD_DROP_MODIFIER).get();
    }
    if(Objects.equals(dimensionType, World.NETHER)){
      multiplier *= dieing.getWorld().getGameRules().get(NETHER_DROP_MODIFIER).get();
    }
    if(Objects.equals(dimensionType, World.END)){
      multiplier *= dieing.getWorld().getGameRules().get(END_DROP_MODIFIER).get();
    }
    return multiplier;
  }


  public static List<ItemStack> dropTotem(ServerPlayerEntity dieing){
    double drops = dieing.getWorld().getGameRules().get(TOTEM_DROP_ON_DEATH_CHANCE).get();
    float multiplier = getChance(dieing);
    DropOnDeath rounder = dieing.getWorld().getGameRules().get(TOTEM_DROP_ON_DEATH).get();
    drops *= multiplier;
    List<ItemStack> totems  = new ArrayList<>();
    for(;drops >= 1; drops --){
      totems.add(TOTEM_OF_UNDYING.getDefaultStack());
    }
    if(drops >= 0 && rounder != DropOnDeath.CERTAIN){
      float odds = Random.create().nextBetween(0, 1000)/ 1000f;
      if(odds <= drops || rounder == DropOnDeath.ALWAYS){
        totems.add(TOTEM_OF_UNDYING.getDefaultStack());
      }
    }
    if(dieing instanceof ISpectate spectator){
      for(ItemStack i: totems){
        spectator.getSpectateManager().dropTotem();
      }
    }
    return totems;
  }


}
