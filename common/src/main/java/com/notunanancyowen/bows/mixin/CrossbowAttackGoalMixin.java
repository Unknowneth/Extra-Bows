package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackGoal.class)
public abstract class CrossbowAttackGoalMixin {
    @Unique private int crossbowAttackDelay = 0;
    @Shadow protected abstract boolean isUncharged();
    @Shadow private int chargedTicksLeft;
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(Lnet/minecraft/entity/Entity;D)Z"))
    private boolean changePathingSpeedToCompensate(EntityNavigation instance, Entity entity, double speed, Operation<Boolean> original) {
        return original.call(instance, entity, isUncharged() || !ExtraBows.BALANCED_MOBS ? speed : speed * 2.0);
    }
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I"))
    private int forcePullTimeToBeZero(ItemStack stack, Operation<Integer> original) {
        return CrossbowItem.isCharged(stack) ? 0 : original.call(stack);
    }
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/LookControl;lookAt(Lnet/minecraft/entity/Entity;FF)V", shift = At.Shift.AFTER), cancellable = true)
    private void delayCrossbow(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS && crossbowAttackDelay > 0) {
            crossbowAttackDelay--;
            ci.cancel();
        }
    }
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void addCrossbowDelay(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS) crossbowAttackDelay = 6;
    }
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/CrossbowUser;setCharging(Z)V"))
    private void tweakCrossbowCooldown(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS && chargedTicksLeft > 0) chargedTicksLeft -= 10;
    }
}
