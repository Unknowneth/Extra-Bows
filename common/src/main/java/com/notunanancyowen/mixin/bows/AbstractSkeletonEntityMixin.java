package com.notunanancyowen.mixin.bows;

import com.notunanancyowen.ExtraBows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
    private ItemConvertible becomeDifferentBow(ItemConvertible par1) {
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.IRON_HELMET)) return ExtraBows.IRON_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.GOLDEN_HELMET)) return ExtraBows.GOLDEN_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CHAINMAIL_HELMET)) return ExtraBows.STONE_BOW;
        if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.DIAMOND_HELMET)) return ExtraBows.DIAMOND_BOW;
        return par1;
    }
    @Override public void lookAtEntity(Entity targetEntity, float maxYawChange, float maxPitchChange) {
        super.lookAtEntity(targetEntity, maxYawChange, maxPitchChange);
        if(!ExtraBows.MAIT && isUsingItem()) getLookControl().lookAt(targetEntity, maxYawChange, maxPitchChange);
    }
}
