package com.dreamtea.revive;

import com.dreamtea.utils.TagUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.impl.item.ItemExtensions;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

import static com.dreamtea.HardcoreBuddyMode.NAMESPACE;

public class  ReviveItem extends FabricTagProvider.ItemTagProvider {

  public static TagKey<Item> REVIVE_ITEMS = TagUtils.createItemTag(new Identifier(NAMESPACE, "revive_item"));

  public static final List<Item> reviveDefaults = List.of(Items.TOTEM_OF_UNDYING);

  public ReviveItem(FabricDataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void generateTags() {
    getOrCreateTagBuilder(REVIVE_ITEMS).add(reviveDefaults.toArray(Item[]::new));
  }

}
