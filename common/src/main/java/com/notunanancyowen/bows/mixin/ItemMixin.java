package com.notunanancyowen.bows.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract ItemStack getDefaultStack();
    @Inject(method = "canRepair", at = @At("HEAD"), cancellable = true)
    private void overrideRepairMaterialForWoodenRangedWeapons(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        var item = getDefaultStack().getItem();
        if(item == Items.BOW || item == Items.CROSSBOW) cir.setReturnValue(ToolMaterials.WOOD.getRepairIngredient().test(ingredient));
    }
}
