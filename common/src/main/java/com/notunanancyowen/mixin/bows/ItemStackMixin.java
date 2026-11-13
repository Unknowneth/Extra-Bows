package com.notunanancyowen.mixin.bows;

import com.notunanancyowen.ExtraBows;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();
    @Inject(method = "isOf", at = @At("HEAD"), cancellable = true)
    private void countAsMyBows(Item item, CallbackInfoReturnable<Boolean> cir) {
        if(item == Items.BOW && (getItem() == ExtraBows.STONE_BOW || getItem() == ExtraBows.IRON_BOW || getItem() == ExtraBows.GOLDEN_BOW || getItem() == ExtraBows.DIAMOND_BOW || getItem() == ExtraBows.NETHERITE_BOW)) cir.setReturnValue(true);
        if(item == Items.CROSSBOW && (getItem() == ExtraBows.STONE_CROSSBOW || getItem() == ExtraBows.IRON_CROSSBOW || getItem() == ExtraBows.GOLDEN_CROSSBOW || getItem() == ExtraBows.DIAMOND_CROSSBOW || getItem() == ExtraBows.NETHERITE_CROSSBOW)) cir.setReturnValue(true);
    }
}