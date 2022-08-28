package com.dreamtea.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ParticleSummoner {
  void spawnParticles(ServerWorld world, Vec3d move, double x, double y, double z);

  public static void spawnParticles(ServerPlayerEntity player, ParticleSummoner summoner){
    summoner.spawnParticles(player.getWorld(), null, player.getX(), player.getY(), player.getZ());
  }
  List<ParticleSummoner> particles = new ArrayList<>();

  static ParticleSummoner getRandomParticle() {
    return particles.get((int) (Math.random() * particles.size()));
  }

  private static ParticleSummoner gen(ParticleEffect effect, int count, double dx, double dy, double dz, double speed){
    ParticleSummoner genned = ((world, move, x, y, z) -> world.spawnParticles(effect, x, y, z, count, dx, dy, dz, speed));
    particles.add(genned);
    return genned;
  }

  private static ParticleSummoner gen(ParticleEffect effect, int count){
    return gen(effect, count, .2, .2,.2,0);
  }
  
  private static ParticleSummoner gen(ParticleEffect effect){
    return gen(effect, 10);
  }



  private static ParticleSummoner genDust(float r, float g, float b, float size){
    if(r > 1 || g > 1 ||b > 1 || r < 0 || g < 0 || b < 0){
      throw new IllegalArgumentException(String.format("RGB values must all be between 0 and 1, provided (%s, %s, %s)", r, g, b));
    }
    return gen(new DustParticleEffect(new Vec3f(r, g, b), size));
  }

  private static ParticleSummoner genDust(float r, float g, float b){
    return genDust(r, g, b,3);
  }

  public static final ParticleSummoner CLOUD = gen(ParticleTypes.CLOUD, 2 );
  public static final ParticleSummoner GLOW_SQUID_INK = gen(ParticleTypes.GLOW_SQUID_INK, 2);
  public static final ParticleSummoner GLOW = gen(ParticleTypes.GLOW, 20);
  public static final ParticleSummoner END_ROD = gen(ParticleTypes.END_ROD, 10 ,.1,.1,.1, 0.01);
  //public static final ParticleSummoner EXPLOSION = gen(ParticleTypes.EXPLOSION, 1 ,0,0,0, 0);
  public static final ParticleSummoner ENCHANT = gen(ParticleTypes.ENCHANT, 20);
  public static final ParticleSummoner SOULS = gen(ParticleTypes.SOUL, 5);
  public static final ParticleSummoner SNEEZE = gen(ParticleTypes.SNEEZE, 5);
  public static final ParticleSummoner WITCH = gen(ParticleTypes.WITCH);
  public static final ParticleSummoner SCRAPE = gen(ParticleTypes.SCRAPE);
  public static final ParticleSummoner WAX_OFF = gen(ParticleTypes.WAX_OFF, 5);
  public static final ParticleSummoner WAX_ON = gen(ParticleTypes.WAX_ON, 5);
  public static final ParticleSummoner SKULK_CHARGE = gen(ParticleTypes.SCULK_CHARGE_POP);
  public static final ParticleSummoner SKULK_SOUL = gen(ParticleTypes.SCULK_SOUL, 5);
  public static final ParticleSummoner SOUL_FLAME = gen(ParticleTypes.SOUL_FIRE_FLAME);
  public static final ParticleSummoner ENCHANTED_HIT = gen(ParticleTypes.ENCHANTED_HIT);
  public static final ParticleSummoner FLAME = gen(ParticleTypes.FLAME);
  public static final ParticleSummoner DRAGONS_BREATH = gen(ParticleTypes.DRAGON_BREATH);
  public static final ParticleSummoner HONEY = gen(ParticleTypes.DRIPPING_HONEY);
  public static final ParticleSummoner LAVA = gen(ParticleTypes.DRIPPING_LAVA);
  public static final ParticleSummoner WATER = gen(ParticleTypes.DRIPPING_WATER);
  public static final ParticleSummoner CRYING_OBSIDIAN = gen(ParticleTypes.DRIPPING_OBSIDIAN_TEAR);
  public static final ParticleSummoner LOVE = gen(ParticleTypes.HEART, 5);
  public static final ParticleSummoner RAINBOW_DUST = gen(new DustParticleEffect(new Vec3f(5, 5, 5), 3));
  public static final ParticleSummoner RED_DUST = genDust(1,0,0);
  public static final ParticleSummoner BlUE_DUST = genDust(0,0,1);
  public static final ParticleSummoner GREEN_DUST = genDust(0,1,0);
  public static final ParticleSummoner YELLOW_DUST = genDust(1,1,0);
  public static final ParticleSummoner CYAN_DUST = genDust(0,1,1);
  public static final ParticleSummoner MAGENTA_DUST = genDust(1,0,1);
  public static final ParticleSummoner WHITE_DUST = genDust(1,1,1);
  public static final ParticleSummoner BLACK_DUST = genDust(0,0,0);

}
