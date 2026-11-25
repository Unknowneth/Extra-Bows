package com.notunanancyowen.fabric.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Items.class)
public abstract class ItemRegistryMixin {
    @Shadow @Final public static Item BOW;
    @Shadow @Final public static Item CROSSBOW;
    static {
        BOW = overrideItem(BOW, ExtraBows.makeNewBow(ToolMaterials.WOOD, 2F, 0.5F));
        CROSSBOW = overrideItem(CROSSBOW, ExtraBows.makeNewCrossbow(ToolMaterials.WOOD, 3F, 0.5F));
    }
    @Unique private static Item overrideItem(Item toOverride, Item newItem){
        return Registry.register(Registries.ITEM, Registries.ITEM.getRawId(toOverride), Registries.ITEM.getId(toOverride).getPath(), newItem);
    }
}