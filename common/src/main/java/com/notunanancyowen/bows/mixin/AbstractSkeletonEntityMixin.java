package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    @Shadow public abstract void updateAttackType();
    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"), order = 1002)
    private ItemConvertible becomeDifferentBow(ItemConvertible par1) {
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.IRON_HELMET)) return ExtraBows.IRON_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.GOLDEN_HELMET)) return ExtraBows.GOLDEN_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CHAINMAIL_HELMET)) return ExtraBows.STONE_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.DIAMOND_HELMET)) return ExtraBows.DIAMOND_BOW;
        return par1;
    }
    @WrapOperation(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack damageWeapon(AbstractSkeletonEntity instance, Hand hand, Operation<ItemStack> original) {
        ItemStack i = original.call(instance, hand);
        if(!(i.isDamageable() && i.getDamage() >= i.getMaxDamage() - 1)) i.damage(1, instance, getSlotForHand(hand));
        return i;
    }
    @Inject(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER))
    private void breakWeapon(LivingEntity target, float pullProgress, CallbackInfo ci, @Local(ordinal = 0) ItemStack i) {
        if(!(i.isDamageable() && i.getDamage() >= i.getMaxDamage() - 1) || getActiveHand() == null) return;
        i.damage(1, this, getSlotForHand(getActiveHand()));
        updateAttackType();
    }
    @Override public void lookAtEntity(Entity targetEntity, float maxYawChange, float maxPitchChange) {
        super.lookAtEntity(targetEntity, maxYawChange, maxPitchChange);
        if(!ExtraBows.MAIT && isUsingItem()) getLookControl().lookAt(targetEntity, maxYawChange, maxPitchChange);
    }
}
