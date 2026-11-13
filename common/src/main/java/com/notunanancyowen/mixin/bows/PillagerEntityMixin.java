package com.notunanancyowen.mixin.bows;

import com.notunanancyowen.ExtraBows;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin {
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
    private ItemConvertible changeToStoneCrossbow(ItemConvertible par1) {
        return ((PillagerEntity)(Object)this).isPatrolLeader() ? ExtraBows.STONE_CROSSBOW : par1;
    }
    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true, require = 0)
    private void canUseModdedCrossbows(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if(weapon instanceof CrossbowItem) cir.setReturnValue(true);
    }
}
