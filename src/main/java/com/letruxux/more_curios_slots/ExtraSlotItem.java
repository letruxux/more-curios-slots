package com.letruxux.more_curios_slots;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExtraSlotItem extends Item {
    private final String SlotNameString;

    private boolean isSlotInCurios() {
        TagKey<Item> tag = ItemTags.create(new ResourceLocation("curios", SlotNameString)); // replace "planks" with your tag
        List<Item> itemsInTag = ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(item -> item.builtInRegistryHolder().is(tag))
                .toList();
        return !itemsInTag.isEmpty();
    }

    public ExtraSlotItem(Properties properties, String slot) {
        super(properties.rarity(Rarity.RARE));
        this.SlotNameString = slot;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack,
                                @Nullable Level level,
                                @NotNull List<Component> components,
                                @NotNull TooltipFlag tooltipFlag) {
        components.add(
                Component.literal("Adds +1 %s slot to your player. Permanent!"
                                .formatted(this.SlotNameString))
                        .withStyle(ChatFormatting.GRAY)
        );

        if (level != null && level.isClientSide()) {
            boolean slotExists = isSlotInCurios();
            if (!slotExists) {
                components.add(
                        Component.literal("This slot is not available in your modpack! It will be useless.")
                                .withStyle(ChatFormatting.RED)
                );
            }
        }

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!isSlotInCurios()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal(String.format("There is no mod in this modpack that supports the %s slot. You can't unlock this.", this.SlotNameString)).withStyle(ChatFormatting.RED), true);
            } else {
                player.playSound(SoundEvents.VILLAGER_HURT);
            }

            return super.use(level, player, hand);
        }

        if (!level.isClientSide) {
            player.displayClientMessage(Component.literal(String.format("You just unlocked +1 %s slot!", this.SlotNameString)), true);
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                    20, 1));
        } else {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
        }

        itemStack.shrink(1);

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            inventory.addPermanentSlotModifier(this.SlotNameString, UUID.randomUUID(), String.format("Extra %s Slot", Utils.toTitleCase(this.SlotNameString)), 1, AttributeModifier.Operation.ADDITION);
        });

        return super.use(level, player, hand);
    }
}
