package com.notunanancyowen.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin {
    @ModifyArg(method = "makeInitialWeapon", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V", ordinal = 0))
    private ItemConvertible changeToGoldCrossbow(ItemConvertible par1) {
        return ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS ? ExtraBows.GOLDEN_CROSSBOW : par1;
    }
    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true, require = 0)
    private void canUseModdedCrossbows(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if(weapon instanceof CrossbowItem) cir.setReturnValue(true);
    }
    @Inject(method = "isCharging", at = @At("HEAD"), cancellable = true)
    private void cancelCharging(CallbackInfoReturnable<Boolean> cir) {
        if(CrossbowItem.isCharged(PiglinEntity.class.cast(this).getActiveItem())) cir.setReturnValue(false);
    }
    @Inject(method = "postShoot", at = @At("HEAD"))
    private void fixBodyRotation(CallbackInfo ci) {
        PiglinEntity me = PiglinEntity.class.cast(this);
        me.getMoveControl().strafeTo(-1F, 0F);
        if(me.getTarget() != null) me.lookAtEntity(me.getTarget(), 90F, 30F);
    }
}
