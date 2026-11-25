package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinEntityModel.class)
public abstract class PiglinEntityModelMixin {
    @WrapOperation(method = "setAngles(Lnet/minecraft/entity/mob/MobEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V"), require = 0)
    private void leftHandFix1(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed, Operation<Void> original, @Local(argsOnly = true) MobEntity mob) {
        original.call(holdingArm, otherArm, head, (mob.getActiveHand() == Hand.MAIN_HAND) != mob.isLeftHanded());
    }
    @WrapOperation(method = "setAngles(Lnet/minecraft/entity/mob/MobEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;charge(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/entity/LivingEntity;Z)V"), require = 0)
    private void leftHandFix2(ModelPart holdingArm, ModelPart pullingArm, LivingEntity actor, boolean rightArmed, Operation<Void> original) {
        if(actor instanceof MobEntity mob) original.call(holdingArm, pullingArm, actor, (mob.getActiveHand() == Hand.MAIN_HAND) != mob.isLeftHanded());
    }
}
