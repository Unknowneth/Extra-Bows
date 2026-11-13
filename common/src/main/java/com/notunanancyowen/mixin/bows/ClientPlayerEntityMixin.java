package com.notunanancyowen.mixin.bows;

import com.mojang.authlib.GameProfile;
import com.notunanancyowen.items.ModifiedRangedWeapon;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow public Input input;
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.AFTER))
    private void removeMovementSpeedPenalty(CallbackInfo ci) {
        if(getActiveItem() != null && getActiveItem().getItem() instanceof ModifiedRangedWeapon f) {
            input.movementForward *= 5F * f.getStrafeSpeed();
            input.movementSideways *= 5F * f.getStrafeSpeed();
        }
    }
}
