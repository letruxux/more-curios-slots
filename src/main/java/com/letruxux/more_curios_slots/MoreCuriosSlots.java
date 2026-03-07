package com.letruxux.more_curios_slots;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod(MoreCuriosSlots.MODID)
public class MoreCuriosSlots {

    public static final String MODID = "more_curios_slots";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

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
            "body",
            "spellstone"
    );

    public static final List<RegistryObject<ExtraSlotItem>> EXTRA_SLOT_ITEMS =
            ALL_CURIO_NAMES.stream()
                    .map(slot -> ITEMS.register(
                            String.format("extra_%s_slot", slot),
                            () -> new ExtraSlotItem(new Item.Properties(), slot)
                    ))
                    .toList();

    public static CreativeModeTab TAB;

    public MoreCuriosSlots(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        /*TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.literal("More Curios Slots"))
                .icon(() -> EXTRA_SLOT_ITEMS.get(0).get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    for (RegistryObject<ExtraSlotItem> extraSlotItem : EXTRA_SLOT_ITEMS) {
                        output.accept(extraSlotItem.get());
                    }
                }).build();*/

        ITEMS.register(modEventBus);
    }
}
