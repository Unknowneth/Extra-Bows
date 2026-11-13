package com.notunanancyowen.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow public Input input;
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V", shift = At.Shift.AFTER))
    private void removeMovementSpeedPenalty(CallbackInfo ci) {
        if(((ClientPlayerEntity)(Object)this).getActiveItem().getComponents().get(ExtraBows.STRAFE_SPEED) instanceof Float f) {
            input.movementForward *= 5F * f;
            input.movementSideways *= 5F * f;
        }
    }
}
