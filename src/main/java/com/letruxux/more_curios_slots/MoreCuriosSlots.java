package com.letruxux.more_curios_slots;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

@Mod(MoreCuriosSlots.MODID)
public class MoreCuriosSlots {
  public static final String MODID = "more_curios_slots";

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(NeoForgeRegistries.ITEMS, MODID);

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
      .create(Registries.CREATIVE_MODE_TAB, MODID);

  public static final List<String> ALL_CURIO_NAMES = List.of(
      "curio",
      "head",
      "feet",
      "hands",
      "charm",
      "belt",
      "ring",
      "back",
      "bracelet",
      "necklace",
      "body"
  // "calendar",
  );

  public static final List<DeferredRegister.Item<ExtraSlotItem>> EXTRA_SLOT_ITEMS = ALL_CURIO_NAMES.stream()
      .map(slot -> ITEMS.registerItem(
          String.format("extra_%s_slot", slot),
          (properties) -> new ExtraSlotItem(properties, slot),
          new Item.Properties()))
      .toList();

  public static final Supplier<CreativeModeTab> MORE_CURIOS_SLOTS_TAB = CREATIVE_MODE_TABS
      .register("more_curios_slots_tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.COMBAT)
          .title(Component.literal("More Curios Slots"))
          .icon(() -> EXTRA_SLOT_ITEMS.get(0).get().getDefaultInstance())
          .displayItems((parameters, output) -> {
            for (DeferredRegister.Item<ExtraSlotItem> extraSlotItem : EXTRA_SLOT_ITEMS) {
              output.accept(extraSlotItem.get());
            }
          }).build());

  public MoreCuriosSlots(FMLJavaModLoadingContext context) {
    IEventBus modEventBus = context.getModEventBus();

    ITEMS.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);
  }
}