package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkeletonEntityModel.class)
public abstract class SkeletonEntityModelMixin {
    @WrapOperation(method = "animateModel(Lnet/minecraft/entity/mob/MobEntity;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), require = 0)
    private boolean allowModdedBows(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.getItem() instanceof BowItem;
    }
    @WrapOperation(method = "setAngles(Lnet/minecraft/entity/mob/MobEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), require = 0)
    private boolean disallowModdedBows(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.getItem() instanceof BowItem;
    }
}
