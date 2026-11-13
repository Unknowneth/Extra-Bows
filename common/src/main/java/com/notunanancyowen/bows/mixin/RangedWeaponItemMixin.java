package com.notunanancyowen.bows.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin extends Item {
    public RangedWeaponItemMixin(Settings settings) {
        super(settings);
    }
    @Inject(method = "getEnchantability", at = @At("HEAD"), cancellable = true)
    private void overrideEnchantablityForWoodenRangedWeapons(CallbackInfoReturnable<Integer> cir) {
        var item = getDefaultStack().getItem();
        if(item == Items.BOW || item == Items.CROSSBOW) cir.setReturnValue(ToolMaterials.WOOD.getEnchantability());
    }
}
