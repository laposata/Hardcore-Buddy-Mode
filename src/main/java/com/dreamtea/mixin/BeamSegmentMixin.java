package com.dreamtea.mixin;

import com.dreamtea.imixin.IGrowBeams;
import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeaconBlockEntity.BeamSegment.class)
public abstract class BeamSegmentMixin implements IGrowBeams {

  @Shadow protected abstract void increaseHeight();

  @Override
  public void increaseBeamHeight() {
    increaseHeight();
  }
}
