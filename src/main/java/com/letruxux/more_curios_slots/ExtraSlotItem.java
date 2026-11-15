package com.letruxux.more_curios_slots;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExtraSlotItem extends Item {
    public static final String SLOT_TAG = "Slot";

    private final @Nullable String defaultSlot;

    public ExtraSlotItem(Properties properties, @Nullable String defaultSlot) {
        super(properties.rarity(Rarity.RARE));
        this.defaultSlot = defaultSlot;
    }

    public static @Nullable String getStoredSlot(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(SLOT_TAG, Tag.TAG_STRING)) {
            return null;
        }

        String slot = tag.getString(SLOT_TAG);
        return slot.isBlank() ? null : slot;
    }

    public @Nullable String getSlot(ItemStack stack) {
        String configured = getStoredSlot(stack);
        return configured != null ? configured : this.defaultSlot;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String configured = getStoredSlot(stack);

        if (configured != null) {
            return Component.translatable("item.more_curios_slots.extra_slot.named",
                    Utils.toDisplayName(configured));
        }

        return super.getName(stack);
    }

    private static void playFailSound(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PILLAGER_AMBIENT, player.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack,
                                @Nullable Level level,
                                @NotNull List<Component> components,
                                @NotNull TooltipFlag tooltipFlag) {
        String slot = this.getSlot(itemStack);

        if (slot == null) {
            components.add(Component.translatable("tooltip.more_curios_slots.no_slot")
                    .withStyle(ChatFormatting.RED));
            components.add(Component.translatable("tooltip.more_curios_slots.no_slot.hint")
                    .withStyle(ChatFormatting.DARK_GRAY));
        } else {
            components.add(Component.translatable("tooltip.more_curios_slots.adds_slot", slot)
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(itemStack);
        }

        String slot = this.getSlot(itemStack);

        if (slot == null) {
            player.displayClientMessage(Component.translatable("message.more_curios_slots.no_slot"), true);
            playFailSound(level, player);
            return InteractionResultHolder.fail(itemStack);
        }

        LazyOptional<ICuriosItemHandler> maybeInventory =
                CuriosApi.getCuriosHelper().getCuriosHandler(player);
        ICuriosItemHandler inventory = maybeInventory.orElse(null);

        if (inventory == null) {
            player.displayClientMessage(Component.translatable("message.more_curios_slots.no_curios"), true);
            playFailSound(level, player);
            return InteractionResultHolder.fail(itemStack);
        }

        Optional<ICurioStacksHandler> stacks = inventory.getStacksHandler(slot);

        if (!stacks.isPresent()) {
            player.displayClientMessage(Component.translatable("message.more_curios_slots.slot_unavailable", slot), true);
            playFailSound(level, player);
            return InteractionResultHolder.fail(itemStack);
        }

        player.displayClientMessage(Component.translatable("message.more_curios_slots.unlocked", slot), true);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        itemStack.shrink(1);
        stacks.get().addPermanentModifier(new AttributeModifier(
                UUID.randomUUID(),
                Utils.toDisplayName(slot),
                1,
                AttributeModifier.Operation.ADDITION
        ));

        return InteractionResultHolder.success(itemStack);
    }
}
