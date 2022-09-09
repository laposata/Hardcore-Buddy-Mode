package com.dreamtea.gamerules;

import com.dreamtea.revive.RevivePoint;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
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
import net.minecraft.world.GameRules;

import java.util.Optional;

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
}
