package com.notunanancyowen.mixin.bows;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BowAttackGoal.class)
public abstract class BowAttackGoalMixin {
    @Shadow @Final private HostileEntity actor;
    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/MoveControl;strafeTo(FF)V"))
    private void changeStrafeSpeedToCompensate(Args args) {
        if(args.get(0) instanceof Float f) args.set(0, f * 2F);
        if(args.get(1) instanceof Float f) args.set(1, f * 2F);
    }
    @Inject(method = "stop", at = @At("TAIL"))
    private void slowDownWhenStopped(CallbackInfo ci) {
        actor.getMoveControl().strafeTo(0F, 0F);
    }
}
