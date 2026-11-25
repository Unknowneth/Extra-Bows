package com.notunanancyowen.bows.mixin;

import com.notunanancyowen.bows.items.ModifiedRangedWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "com.notunanancyowen.darts.items.BlowpipeItem")
public class BlowpipeItemMixin implements ModifiedRangedWeapon {
    @Override public float getStrafeSpeed() {
        return 1F;
    }
    @Override public float getRangedDamage() {
        return 2F;
    }
}
