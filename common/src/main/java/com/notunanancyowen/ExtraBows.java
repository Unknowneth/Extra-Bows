package com.notunanancyowen;

import com.notunanancyowen.items.ModifiedBow;
import com.notunanancyowen.items.ModifiedCrossbow;
import net.minecraft.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtraBows {
	public static final String MOD_ID = "bows";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static BowItem makeNewBow(ToolMaterial toolMaterial, float rangedDamage, float strafeSpeed) {
		return new ModifiedBow(toolMaterial, rangedDamage, strafeSpeed, new Item.Settings());
	}
	public static CrossbowItem makeNewCrossbow(ToolMaterial toolMaterial, float rangedDamage, float strafeSpeed) {
		return new ModifiedCrossbow(toolMaterial, rangedDamage, strafeSpeed, new Item.Settings());
	}
	public static Item STONE_BOW;
	public static Item IRON_BOW;
	public static Item GOLDEN_BOW;
	public static Item DIAMOND_BOW;
	public static Item NETHERITE_BOW;
	public static Item STONE_CROSSBOW;
	public static Item IRON_CROSSBOW;
	public static Item GOLDEN_CROSSBOW;
	public static Item DIAMOND_CROSSBOW;
	public static Item NETHERITE_CROSSBOW;
	public static boolean MAIT = false;
	public static void init() {
		LOGGER.info("Added some extra bows");
	}
}