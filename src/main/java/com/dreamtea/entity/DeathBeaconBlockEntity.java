package com.dreamtea.entity;

import com.dreamtea.imixin.IGrowBeams;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dreamtea.gamerules.DeathBeaconGamerule.DEATH_BEACON;
import static net.minecraft.block.Blocks.BEACON;

public class DeathBeaconBlockEntity extends BeaconBlockEntity {
  public static final String COLOR_KEY = "deathBeaconColor";
  public static final String SPAWN_TIME = "deathBeaconSpawnTime";
  List<BeamSegment> beamSegments = Lists.newArrayList();
  float[] color;
  Long spawnTime;
  public DeathBeaconBlockEntity(BlockPos pos) {
    super(new BlockPos(pos.getX(), pos.getY() - 32, pos.getZ()), BEACON.getDefaultState());
    color = new float[]{Random.create().nextFloat(),Random.create().nextFloat(),Random.create().nextFloat()};
    BeamSegment beam = new BeamSegment(color);
    beamSegments.add(beam);
    if(this.getWorld() == null) return;
    for(int i = pos.getY(); i < this.getWorld().getHeight(); i++){
      ((IGrowBeams)beam).increaseBeamHeight();
    }
    spawnTime = this.getWorld().getTime();
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    int[] colors256 = nbt.getIntArray(COLOR_KEY);
    this.color = new float[]{colors256[0]/256f,colors256[1]/256f,colors256[2]/256f};
    this.spawnTime = nbt.getLong(SPAWN_TIME);
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putLong(SPAWN_TIME, spawnTime);
    nbt.putIntArray(COLOR_KEY, new int[]{(int)(this.color[0] * 256), (int)(this.color[1] * 256), (int)(this.color[2] * 256)});
  }

  @Override
  @Nullable
  public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return null;
  }

  public boolean possiblyKill(){
    if(this.getWorld() == null) return false;
    int secsLoaded = this.getWorld().getGameRules().get(DEATH_BEACON).get();
    if(this.spawnTime + secsLoaded * 20L < this.getWorld().getTime()){
      this.markRemoved();
      return true;
    }
    return false;
  }
}
