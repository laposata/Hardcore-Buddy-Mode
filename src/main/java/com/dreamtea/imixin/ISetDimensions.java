package com.dreamtea.imixin;

import net.minecraft.entity.EntityDimensions;

public interface ISetDimensions {
  float getOverrideEyeHeight();

  void setDimensionOverride(float width, float height, boolean fixed);

  void setDimensionOverride(EntityDimensions overriding);
}
