package com.notunanancyowen.items;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;

public class ModifiedBow extends BowItem implements ModifiedRangedWeapon {
    private final ToolMaterial material;
    private final float rangedDamage;
    private final float strafeSpeed;
    public ModifiedBow(ToolMaterial material, float rangedDamage, float strafeSpeed, Settings settings) {
        super(material == ToolMaterials.NETHERITE ? settings.maxDamage(material.getDurability()).fireproof() : settings.maxDamage(material.getDurability()));
        this.rangedDamage = rangedDamage;
        this.strafeSpeed = strafeSpeed;
        this.material = material;
    }
    @Override public float getRangedDamage() {
        return rangedDamage;
    }
    @Override public float getStrafeSpeed() {
        return strafeSpeed;
    }
    @Override public int getEnchantability() {
        return material.getEnchantability();
    }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }
}
