package com.notunanancyowen.forge.bows;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExtraBows.MOD_ID)
public class ExtraBowsCommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec.BooleanValue CREATIVE_MENU = BUILDER.comment("Exists to fix weird crashes with other mods when opening the creative menu").define("register_bows_to_creative_menu", true);
    static final ForgeConfigSpec.BooleanValue TOOLTIPS = BUILDER.comment("Displays ranged damage and strafe speed").define("show_tiered_bow_and_crossbow_stats", true);
    static final ForgeConfigSpec.BooleanValue MOBS_WITH_BOWS = BUILDER.comment("Whether skeletons spawn with higher tier bows based on the helmets they wear").define("mobs_spawn_with_tiered_bows", true);
    static final ForgeConfigSpec.BooleanValue MOBS_WITH_CROSSBOWS = BUILDER.comment("Whether piglins always spawn with gold crossbows and pillager patrol leaders with stone crossbows").define("mobs_spawn_with_tiered_crossbows", true);
    static final ForgeConfigSpec.BooleanValue BALANCED_MOBS = BUILDER.comment("Rebalances bow and crossbow attack behaviors to compensate for the newly added stuff").define("rebalance_mobs_with_tiered_bow_and_crossbow", true);
    static final ForgeConfigSpec SPEC = BUILDER.build();
}
