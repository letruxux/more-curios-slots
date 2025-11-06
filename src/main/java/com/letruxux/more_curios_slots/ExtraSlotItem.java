package com.letruxux.more_curios_slots;

import net.minecraft.ChatFormatting;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.UUID;

public class ExtraSlotItem extends Item {
    private final String SlotNameString;

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

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        if (!level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);

            var curiosInventory = CuriosApi.getCuriosInventory(player);

            if (curiosInventory.isPresent() && CuriosApi.getSlots(level).containsKey(this.SlotNameString)) {
                player.displayClientMessage(Component.literal(String.format("You just unlocked +1 %s slot!", this.SlotNameString)), true);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                        20, 1));
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
                itemStack.shrink(1);
                curiosInventory.ifPresent(inventory -> inventory.addPermanentSlotModifier(this.SlotNameString, UUID.randomUUID(), String.format("Extra %s Slot", Utils.toTitleCase(this.SlotNameString)), 1, AttributeModifier.Operation.ADDITION));
            } else {
                player.displayClientMessage(Component.literal("Curios inventory not found, are you sure you have any curios-supported mods?"), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PILLAGER_AMBIENT, player.getSoundSource(), 1.0F, 1.0F);
            }
        }

        return super.use(level, player, hand);
    }
}
