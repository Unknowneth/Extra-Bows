package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackTask.class)
public abstract class CrossbowAttackTaskMixin {
    @Unique private int crossbowAttackDelay = 0;
    @Shadow private int chargingCooldown;
    @WrapOperation(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I"))
    private int forcePullTimeToBeZero(ItemStack stack, Operation<Integer> original) {
        return CrossbowItem.isCharged(stack) ? 0 : original.call(stack);
    }
    @Inject(method = "tickState", at = @At("HEAD"), cancellable = true)
    private void delayCrossbow(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS && crossbowAttackDelay > 0) {
            crossbowAttackDelay--;
            ci.cancel();
        }
    }
    @Inject(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void addCrossbowDelay(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS) crossbowAttackDelay = 6;
    }
    @Inject(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/CrossbowUser;setCharging(Z)V"))
    private void tweakCrossbowCooldown(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS && chargingCooldown > 0) chargingCooldown -= 10;
    }
}
