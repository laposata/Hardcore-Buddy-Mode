package com.dreamtea.revive;

import com.dreamtea.gamerules.DeadReviveLocalGamerule;
import com.dreamtea.imixin.ISpectate;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.GameMode;

import static net.minecraft.item.Items.TOTEM_OF_UNDYING;

public class Revive {

  public static boolean revivePlayer(Entity reviving, ServerPlayerEntity savior){
    if(reviving.isSpectator() && reviving instanceof ISpectate spect && spect.getSpectateManager().getDeadSpectate()){
      if(!savior.isSpectator() && savior.getMainHandStack().isOf(TOTEM_OF_UNDYING)){
        reviving.world.sendEntityStatus(reviving, (byte)35);
        Criteria.USED_TOTEM.trigger(savior, savior.getMainHandStack());
        savior.incrementStat(Stats.USED.getOrCreateStat(savior.getMainHandStack().getItem()));
        savior.getMainHandStack().decrement(1);
        if(reviving instanceof ServerPlayerEntity deadPlayer){
          deadPlayer.changeGameMode(GameMode.SURVIVAL);
          deadPlayer.clearStatusEffects();
          deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 900, 2), savior);
          deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 900), savior);
          deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 900), savior);
          deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 4), savior);
          deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100, 4), savior);
          deadPlayer.getWorld().spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, reviving.getX(), reviving.getY() + 2, reviving.getZ(), 1, 0, 0, 0, 2);
          if(savior.getServer() != null
            && DeadReviveLocalGamerule.respawnPlayer(deadPlayer, savior, savior.getServer().getPlayerManager())){
            return true;
          }
        }
        return true;
      }
    }
    return false;
  }
}
