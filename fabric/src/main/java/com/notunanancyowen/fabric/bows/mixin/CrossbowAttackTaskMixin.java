package com.notunanancyowen.fabric.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowAttackTask.class)
public abstract class CrossbowAttackTaskMixin<E extends MobEntity & CrossbowUser, T extends LivingEntity> {
    @WrapOperation(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand changeToDetectAnyKindOfCrossbow(LivingEntity entity, Item item, Operation<Hand> original) {
        return original.call(entity, entity.getMainHandStack().getItem() instanceof CrossbowItem crossbowItem ? crossbowItem : item);
    }
    @WrapOperation(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/CrossbowUser;setCharging(Z)V", ordinal = 0))
    private void fixDoubleChargeBug(CrossbowUser instance, boolean b, Operation<Void> original, @Local(argsOnly = true) E entity) {
        if(!CrossbowItem.isCharged(entity.getActiveItem())) original.call(instance, b);
    }
}
