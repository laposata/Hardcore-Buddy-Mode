package com.dreamtea.revive;

import com.dreamtea.gamerules.DeadReviveLocalGamerule;
import com.dreamtea.imixin.ISpectate;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.Optional;

import static com.dreamtea.gamerules.DeadReviveLocalGamerule.DEATH_REVIVE_LOCATION;
import static net.minecraft.item.Items.TOTEM_OF_UNDYING;

public class Revive {

  public static boolean revivePlayer(Entity reviving, ServerPlayerEntity savior){
    if(reviving.isSpectator() && reviving instanceof ISpectate spect && spect.getSpectateManager().getDeadSpectate()){
      if(!savior.isSpectator() && savior.getMainHandStack().isOf(TOTEM_OF_UNDYING)){
        if(reviving instanceof ServerPlayerEntity deadPlayer){
          if(savior.getServer() != null){
            respawnPlayer(deadPlayer, savior, savior.getServer().getPlayerManager());
            deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 900, 2), savior);
            deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 900), savior);
            deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 900), savior);
            deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 4), savior);
            deadPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100, 4), savior);
            deadPlayer.getWorld().spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, reviving.getX(), reviving.getY() + 1, reviving.getZ(), 1, 0, 0, 0, 2);
            return true;
          }
        }
      }
    }
    return false;
  }

  public static void respawnPlayer(ServerPlayerEntity player, ServerPlayerEntity savior, PlayerManager manager){
    DeadReviveLocalGamerule.locals local = player.getWorld().getGameRules().get(DEATH_REVIVE_LOCATION).get();
    RevivePoint point = respawnPos(player);
    switch (local) {
      case SELF -> {
        point = new RevivePoint(player.getBlockPos(), 0f, player.getWorld());
      }
      case REVIVER -> {
        point = new RevivePoint(savior.getBlockPos(), 0f, player.getWorld());
      }
    }
    point.respawnPlayer(player);
    player.world.sendEntityStatus(player, (byte)35);
    Criteria.USED_TOTEM.trigger(savior, savior.getMainHandStack());
    savior.incrementStat(Stats.USED.getOrCreateStat(savior.getMainHandStack().getItem()));
    savior.getMainHandStack().decrement(1);
  }

  public static RevivePoint respawnPos(ServerPlayerEntity player){
    ServerWorld spawnWorld = player.server.getWorld(player.getSpawnPointDimension());
    spawnWorld = spawnWorld != null ? spawnWorld : player.server.getOverworld();
    BlockPos blockPos = player.getSpawnPointPosition();
    blockPos = blockPos != null ? blockPos : spawnWorld.getSpawnPos();
    Optional<Vec3d> respawnPosition = PlayerEntity.findRespawnPosition(spawnWorld, blockPos, player.getSpawnAngle(), player.isSpawnForced(), false);
    spawnWorld = respawnPosition.isPresent() ? spawnWorld : player.server.getOverworld();
    RevivePoint revivePoint = new RevivePoint(spawnWorld);
    if (respawnPosition.isPresent()) {
      float g;
      BlockState blockState = spawnWorld.getBlockState(blockPos);
      boolean usingRespawnAnchor = blockState.isOf(Blocks.RESPAWN_ANCHOR);
      Vec3d vec3d = (Vec3d)respawnPosition.get();
      if (blockState.isIn(BlockTags.BEDS) || usingRespawnAnchor) {
        Vec3d vec3d2 = Vec3d.ofBottomCenter(blockPos).subtract(vec3d).normalize();
        g = (float) MathHelper.wrapDegrees(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875 - 90.0);
      } else {
        g = player.getSpawnAngle();
      }
      player.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, g, 0.0f);
      revivePoint = new RevivePoint(new BlockPos(vec3d), g, spawnWorld);
      player.setHealth(player.getHealth());
      if (usingRespawnAnchor) {
        player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0f, 1.0f, spawnWorld.getRandom().nextLong()));
      }
    }
    return revivePoint;
  }
}
