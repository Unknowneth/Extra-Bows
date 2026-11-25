package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowAttackGoal.class)
public abstract class BowAttackGoalMixin {
    @Shadow private int cooldown;
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/MoveControl;strafeTo(FF)V"))
    private void changeStrafeSpeedToCompensate(MoveControl instance, float forward, float sideways, Operation<Void> original) {
        if(ExtraBows.BALANCED_MOBS) {
            forward *= 2F;
            sideways *= 2F;
        }
        original.call(instance, forward, sideways);
    }
    @Inject(method = "start", at = @At("HEAD"))
    private void delayRangedAttack(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS) cooldown = 20;
    }
}
