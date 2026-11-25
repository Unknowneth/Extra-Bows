package com.notunanancyowen.fabric.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowAttackGoal.class)
public abstract class CrossbowAttackGoalMixin<T extends HostileEntity & CrossbowUser> {
    @Shadow @Final private T actor;
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand changeToDetectAnyKindOfCrossbow(LivingEntity entity, Item item, Operation<Hand> original) {
        return original.call(entity, entity.getMainHandStack().getItem() instanceof CrossbowItem crossbowItem ? crossbowItem : item);
    }
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/CrossbowUser;setCharging(Z)V", ordinal = 0))
    private void fixDoubleChargeBug(CrossbowUser instance, boolean b, Operation<Void> original) {
        if(!CrossbowItem.isCharged(actor.getActiveItem())) original.call(instance, b);
    }
}
