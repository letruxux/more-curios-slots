package com.letruxux.more_curios_slots;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public final class ModComponents {
  public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
      DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MoreCuriosSlots.MODID);


  public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SLOT =
      DATA_COMPONENTS.register("slot", () -> DataComponentType.<String>builder()
          .persistent(Codec.STRING)
          .networkSynchronized(ByteBufCodecs.STRING_UTF8)
          .build());

  private ModComponents() {}
}
