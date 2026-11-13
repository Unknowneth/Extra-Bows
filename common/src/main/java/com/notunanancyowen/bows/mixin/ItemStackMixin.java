package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();
    @Shadow public abstract ComponentMap getComponents();
    @Inject(method = "isOf", at = @At("HEAD"), cancellable = true)
    private void countAsMyBows(Item item, CallbackInfoReturnable<Boolean> cir) {
        if(item == Items.BOW && ((ExtraBows.DRAGON_BOW != null && getItem() == ExtraBows.DRAGON_BOW) || (ExtraBows.COPPER_BOW != null && getItem() == ExtraBows.COPPER_BOW) || getItem() == ExtraBows.STONE_BOW || getItem() == ExtraBows.IRON_BOW || getItem() == ExtraBows.GOLDEN_BOW || getItem() == ExtraBows.DIAMOND_BOW || getItem() == ExtraBows.NETHERITE_BOW)) cir.setReturnValue(true);
        if(item == Items.CROSSBOW && ((ExtraBows.DRAGON_CROSSBOW != null && getItem() == ExtraBows.DRAGON_CROSSBOW) || (ExtraBows.COPPER_CROSSBOW != null && getItem() == ExtraBows.COPPER_CROSSBOW) || getItem() == ExtraBows.STONE_CROSSBOW || getItem() == ExtraBows.IRON_CROSSBOW || getItem() == ExtraBows.GOLDEN_CROSSBOW || getItem() == ExtraBows.DIAMOND_CROSSBOW || getItem() == ExtraBows.NETHERITE_CROSSBOW)) cir.setReturnValue(true);
    }
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", ordinal = 6))
    private void correctlySortItemDescriptionsForNeoForge(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        if(list.isEmpty() || !ExtraBows.NeoForge) return;
        int i, j = 0;
        int k = 0;
        for(i = 0; i < list.size(); i++) if(list.get(i).contains(Text.translatable("item.modifiers." + AttributeModifierSlot.HAND.asString()))) if(j == 0) k = j = i;
        else {
            j = i;
            break;
        }
        if(j > 0 && j == i) {
            list.remove(j - 1);
            list.remove(j - 1);
            list.add(k + 1, list.remove(j));
            list.add(k + 1, list.remove(j));
        }
    }
    @Inject(method = "appendAttributeModifiersTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifier(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V"))
    private void expandItemTooltips(Consumer<Text> textConsumer, @Nullable PlayerEntity player, CallbackInfo ci, @Local MutableBoolean mutableBoolean, @Local AttributeModifierSlot attributeModifierSlot) {
        if(attributeModifierSlot != AttributeModifierSlot.HAND || ExtraBows.NeoForge) return;
        float rangedDamage = 0F;
        float strafeSpeed = 0F;
        if(getComponents().get(ExtraBows.RANGED_DAMAGE) instanceof Float f) rangedDamage = f;
        if(getComponents().get(ExtraBows.STRAFE_SPEED) instanceof Float f) strafeSpeed = f;
        if(rangedDamage == 0F || strafeSpeed == 0F) return;
        mutableBoolean.setFalse();
        textConsumer.accept(ScreenTexts.EMPTY);
        textConsumer.accept(Text.translatable("item.modifiers." + attributeModifierSlot.asString()).formatted(Formatting.GRAY));
        if(rangedDamage > 0F) textConsumer.accept(Text.translatable("attributes.bows.ranged_damage", AttributeModifiersComponent.DECIMAL_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
        if(strafeSpeed > 0F) textConsumer.accept(Text.translatable("attributes.bows.strafe_speed", AttributeModifiersComponent.DECIMAL_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
    }
}