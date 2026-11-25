package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @WrapOperation(method = "isChargedCrossbow", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), require = 0)
    private static boolean isAlsoChargedCrossbow(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.getItem() instanceof CrossbowItem;
    }
    @WrapOperation(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), require = 0)
    private static boolean isAlsoCrossbow(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || (item == Items.CROSSBOW && instance.getItem() instanceof CrossbowItem);
    }
    @WrapOperation(method = "getUsingItemHandRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), require = 0)
    private static boolean isAlsoBowOrCrossbow(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || (item == Items.BOW && instance.getItem() instanceof BowItem) || (item == Items.CROSSBOW && instance.getItem() instanceof CrossbowItem);
    }
}
