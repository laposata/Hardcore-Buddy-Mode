package com.dreamtea;

import com.dreamtea.revive.ReviveItem;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class HardcoreBuddyModeData implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    fabricDataGenerator.addProvider(ReviveItem::new);
  }
}
