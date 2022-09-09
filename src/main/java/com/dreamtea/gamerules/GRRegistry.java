package com.dreamtea.gamerules;

import com.dreamtea.entity.DeathBeaconBlockEntity;

public class GRRegistry {
  public static void register(){
    DeadReviveLocalGamerule.registerGamerule();
    TotemDropOnDeathGamerule.registerGamerule();
    TotemDropMinAgeGamerule.registerGamerule();
    TotemDropDistanceTraveledMinGamerule.registerGamerule();
    TotemDropMinTimeSinceLastDeathGamerule.registerGamerule();
    TotemDropConditionsGamerule.registerGamerule();
    //DeathChunkloadGamerule.registerGamerule();
    //DeathBeaconGamerule.registerGamerule();
  }
}
