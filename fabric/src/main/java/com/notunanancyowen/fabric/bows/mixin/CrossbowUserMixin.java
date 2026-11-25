package com.notunanancyowen.fabric.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowUser.class)
public interface CrossbowUserMixin {
    @WrapOperation(method = "shoot(Lnet/minecraft/entity/LivingEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand changeToDetectAnyKindOfCrossbow(LivingEntity entity, Item item, Operation<Hand> original) {
        return original.call(entity, entity.getMainHandStack().getItem() instanceof CrossbowItem crossbowItem ? crossbowItem : item);
    }
}
