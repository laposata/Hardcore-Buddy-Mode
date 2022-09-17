package com.dreamtea.mixin;

import com.dreamtea.imixin.ISpectate;
import com.dreamtea.spectator.SpectatorHitbox;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends LivingEntity {

  protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public boolean isInvisibleTo(PlayerEntity player) {
    if(((ArmorStandEntity)(Object)this).isInvisible()){
      return true;
    }
    return super.isInvisibleTo(player);
  }
}
