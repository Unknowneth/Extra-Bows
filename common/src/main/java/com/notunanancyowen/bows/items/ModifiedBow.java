package com.notunanancyowen.bows.items;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;

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
