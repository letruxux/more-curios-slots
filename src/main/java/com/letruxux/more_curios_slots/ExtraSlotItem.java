package com.letruxux.more_curios_slots;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExtraSlotItem extends Item {
    private final @Nullable String defaultSlot;

    public ExtraSlotItem(Properties properties, @Nullable String defaultSlot) {
        super(properties.rarity(Rarity.RARE));
        this.defaultSlot = defaultSlot;
    }

    public static @Nullable String getComponentSlot(ItemStack stack) {
        String slot = stack.get(ModComponents.SLOT.get());
        return slot == null || slot.isBlank() ? null : slot;
    }


    public @Nullable String getSlot(ItemStack stack) {
        String configured = getComponentSlot(stack);
        return configured != null ? configured : this.defaultSlot;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String configured = getComponentSlot(stack);

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
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        String slot = this.getSlot(stack);

        if (slot == null) {
            tooltipComponents.add(Component.translatable("tooltip.more_curios_slots.no_slot")
                    .withStyle(ChatFormatting.RED));
            tooltipComponents.add(Component.translatable("tooltip.more_curios_slots.no_slot.hint")
                    .withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.more_curios_slots.adds_slot", slot)
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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

        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

        if (curiosInventory.isEmpty()) {
            player.displayClientMessage(Component.translatable("message.more_curios_slots.no_curios"), true);
            playFailSound(level, player);
            return InteractionResultHolder.fail(itemStack);
        }

        ICuriosItemHandler inventory = curiosInventory.get();

        if (!inventory.getCurios().containsKey(slot)) {
            player.displayClientMessage(Component.translatable("message.more_curios_slots.slot_unavailable", slot), true);
            playFailSound(level, player);
            return InteractionResultHolder.fail(itemStack);
        }

        player.displayClientMessage(Component.translatable("message.more_curios_slots.unlocked", slot), true);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        itemStack.shrink(1);
        inventory.addPermanentSlotModifier(slot,
                ResourceLocation.fromNamespaceAndPath(MoreCuriosSlots.MODID, UUID.randomUUID().toString()),
                1, AttributeModifier.Operation.ADD_VALUE);

        return InteractionResultHolder.success(itemStack);
    }
}
