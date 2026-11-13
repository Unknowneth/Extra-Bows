package com.notunanancyowen.mixin.bows;

import com.notunanancyowen.items.ModifiedRangedWeapon;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @Shadow private double damage;
    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void setDamageFromComponents(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, CallbackInfo ci) {
        if(owner.getActiveItem().getItem() instanceof ModifiedRangedWeapon f) damage = f.getRangedDamage();
        else if(owner.getMainHandStack().getItem() instanceof ModifiedRangedWeapon f) damage = f.getRangedDamage();
        else if(owner.getOffHandStack().getItem() instanceof ModifiedRangedWeapon f) damage = f.getRangedDamage();
        else if(owner.getMainHandStack().getItem() == Items.CROSSBOW) damage = 3F;
        else if(owner.getOffHandStack().getItem() == Items.CROSSBOW) damage = 3F;
    }
}
