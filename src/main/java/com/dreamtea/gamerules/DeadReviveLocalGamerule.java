package com.dreamtea.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;

import static com.dreamtea.gamerules.DeadReviveLocalGamerule.locals.SPAWN;

public class DeadReviveLocalGamerule {
  public static final String DEATH_REVIVE_LOCATION_RULE = "reviveLocation";
  public static GameRules.Key<EnumRule<locals>> DEATH_REVIVE_LOCATION;

  public enum locals{
    SELF,
    REVIVER,
    SPAWN
  }
  public static void registerGamerule(){
    DEATH_REVIVE_LOCATION = GameRuleRegistry.register(DEATH_REVIVE_LOCATION_RULE,
      GameRules.Category.PLAYER,
      GameRuleFactory.createEnumRule(SPAWN, locals.values())
    );
  }

  public static boolean respawnPlayer(ServerPlayerEntity player, ServerPlayerEntity savior, PlayerManager manager){
    locals local = player.getWorld().getGameRules().get(DEATH_REVIVE_LOCATION).get();
    switch (local) {
      case SELF -> {
        return true;
      }
      case REVIVER -> {
        player.teleport(savior.getX(), savior.getY(), savior.getZ());
        return true;
      }
      case SPAWN -> {
        manager.respawnPlayer(player, true);
        return true;
      }
    }
    return false;
  }
}
