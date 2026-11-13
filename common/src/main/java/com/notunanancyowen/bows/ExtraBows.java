package com.notunanancyowen.bows;

import net.minecraft.component.ComponentType;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.UnaryOperator;

public class ExtraBows {
	public static final String MOD_ID = "bows";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ComponentType<Float> RANGED_DAMAGE;
	public static ComponentType<Float> STRAFE_SPEED;
	public static BowItem makeNewBow(ToolMaterial toolMaterial, float rangedDamage, float strafeSpeed) {
		var components = new Item.Settings().maxDamage(toolMaterial.getDurability()).component(RANGED_DAMAGE, rangedDamage).component(STRAFE_SPEED, strafeSpeed);
		if(toolMaterial == ToolMaterials.NETHERITE) components.fireproof();
		return new BowItem(components) {
			@Override public boolean canRepair(ItemStack stack, ItemStack ingredient) {
				return super.canRepair(stack, ingredient) || toolMaterial.getRepairIngredient().test(ingredient);
			}
			@Override public int getEnchantability() {
				return toolMaterial.getEnchantability();
			}
		};
	}
	public static CrossbowItem makeNewCrossbow(ToolMaterial toolMaterial, float rangedDamage, float strafeSpeed) {
		var components = new Item.Settings().maxDamage(toolMaterial.getDurability()).component(RANGED_DAMAGE, rangedDamage).component(STRAFE_SPEED, strafeSpeed);
		if(toolMaterial == ToolMaterials.NETHERITE) components.fireproof();
		return new CrossbowItem(components) {
			@Override public boolean canRepair(ItemStack stack, ItemStack ingredient) {
				return super.canRepair(stack, ingredient) || toolMaterial.getRepairIngredient().test(ingredient);
			}
			@Override public int getEnchantability() {
				return toolMaterial.getEnchantability();
			}
		};
	}
	public static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator, boolean neoForge) {
		if(neoForge) return (builderOperator.apply(ComponentType.builder())).build();
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, (builderOperator.apply(ComponentType.builder())).build());
	}
	public static Item STONE_BOW = null;
	public static Item IRON_BOW = null;
	public static Item GOLDEN_BOW = null;
	public static Item DIAMOND_BOW = null;
	public static Item NETHERITE_BOW = null;
	public static Item STONE_CROSSBOW = null;
	public static Item IRON_CROSSBOW = null;
	public static Item GOLDEN_CROSSBOW = null;
	public static Item DIAMOND_CROSSBOW = null;
	public static Item NETHERITE_CROSSBOW = null;
	public static Item COPPER_BOW = null;
	public static Item COPPER_CROSSBOW = null;
	public static Item DRAGON_BOW = null;
	public static Item DRAGON_CROSSBOW = null;
	public static boolean MAIT = false;
	public static boolean NeoForge = false;
	public static void init() {
		LOGGER.info("Added some extra bows");
	}
}