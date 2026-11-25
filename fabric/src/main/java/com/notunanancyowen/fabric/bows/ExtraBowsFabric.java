package com.notunanancyowen.fabric.bows;

import com.notunanancyowen.bows.ExtraBows;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class ExtraBowsFabric implements ModInitializer {
    public static boolean RANGED_WEAPON_TOOLTIPS = true;
    private static boolean SHOW_BOWS_IN_CREATIVE_MENU = true;
    @Override public void onInitialize() {
        String originalConfig = "#Exists to fix weird crashes with other mods when opening the creative menu\n"
                + "register_bows_to_creative_menu = " + SHOW_BOWS_IN_CREATIVE_MENU + "\n#Displays ranged damage and strafe speed\n"
                + "show_tiered_bow_and_crossbow_stats = " + RANGED_WEAPON_TOOLTIPS + "\n#Whether skeletons spawn with higher tier bows based on the helmets they wear\n"
                + "mobs_spawn_with_tiered_bows = " + ExtraBows.MOBS_SPAWN_WITH_TIERED_BOWS + "\n#Whether piglins always spawn with gold crossbows and pillager patrol leaders with stone crossbows\n"
                + "mobs_spawn_with_tiered_crossbows = " + ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS + "\n#Rebalances bow and crossbow attack behaviors to compensate for the newly added stuff\n"
                + "rebalance_mobs_with_tiered_bow_and_crossbow = " + ExtraBows.BALANCED_MOBS;
        long originalConfigSize = originalConfig.lines().count();
        try {
            File config = FabricLoader.getInstance().getConfigDir().resolve(ExtraBows.MOD_ID + "-common.toml").toFile();
            if(config.createNewFile()) try(FileWriter configWriter = new FileWriter(config)) {
                configWriter.write(originalConfig);
            }
            boolean rebuild;
            try(FileReader configReader = new FileReader(config)) {
                BufferedReader reader = new BufferedReader(configReader);
                StringBuilder bob = new StringBuilder();
                String configText;
                int tries = -1;
                while(++tries <= originalConfigSize && (configText = reader.readLine()) != null) {
                    bob.append(configText);
                    bob.append(System.lineSeparator());
                }
                bob.deleteCharAt(bob.length() - 1);
                reader.close();
                bob.toString().lines().forEach(configContent -> {
                    if(!configContent.startsWith("#") && configContent.contains(" = ")) {
                        String[] configValues = configContent.split(" = ", 2);
                        boolean result = configValues[1].equals("true");
                        switch (configValues[0]) {
                            case "register_bows_to_creative_menu" -> SHOW_BOWS_IN_CREATIVE_MENU = result;
                            case "show_tiered_bow_and_crossbow_stats" -> RANGED_WEAPON_TOOLTIPS = result;
                            case "mobs_spawn_with_tiered_bows" -> ExtraBows.MOBS_SPAWN_WITH_TIERED_BOWS = result;
                            case "mobs_spawn_with_tiered_crossbows" -> ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS = result;
                            case "rebalance_mobs_with_tiered_bow_and_crossbow" -> ExtraBows.BALANCED_MOBS = result;
                            default -> ExtraBows.LOGGER.info("Unknown config value: \"" + configValues[0] + "\"");
                        }
                    }
                });
                rebuild = bob.toString().lines().count() != originalConfigSize;
            }
            if(rebuild && config.delete() && config.createNewFile()) try(FileWriter configWriter = new FileWriter(config)) {
                originalConfig.lines().forEach(configContent -> {
                    if(configContent.startsWith("#") || !configContent.contains("=")) {
                        try {
                            configWriter.append(configContent);
                            if(originalConfig.lines().toList().indexOf(configContent) < originalConfigSize - 1) configWriter.append(System.lineSeparator());
                        }
                        catch (Throwable ignore) {
                        }
                    }
                    String[] configValues = configContent.split(" = ", 2);
                    if(!configValues[0].startsWith("#")) try {
                        configContent = configValues[0] + " = ";
                        switch (configValues[0]) {
                            case "register_bows_to_creative_menu" -> configContent += SHOW_BOWS_IN_CREATIVE_MENU;
                            case "show_tiered_bow_and_crossbow_stats" -> configContent += RANGED_WEAPON_TOOLTIPS;
                            case "mobs_spawn_with_tiered_bows" -> configContent += ExtraBows.MOBS_SPAWN_WITH_TIERED_BOWS;
                            case "mobs_spawn_with_tiered_crossbows" -> configContent += ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS;
                            case "rebalance_mobs_with_tiered_bow_and_crossbow" -> configContent += ExtraBows.BALANCED_MOBS;
                            default -> ExtraBows.LOGGER.info("Unknown config value: \"" + configValues[0] + "\"");
                        }
                        configWriter.append(configContent);
                        if(originalConfig.lines().toList().indexOf(configContent) < originalConfigSize - 1) configWriter.append(System.lineSeparator());
                    }
                    catch (Throwable ignore) {
                        ExtraBows.LOGGER.info("Failed to generate config");
                    }
                });
            }
        }
        catch (Throwable t) {
            ExtraBows.LOGGER.info("Failed to generate config: " + t.getLocalizedMessage());
        }
        ExtraBows.MAIT = FabricLoader.getInstance().isModLoaded("mob-ai-tweaks");
        ExtraBows.STONE_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "stone_bow"), ExtraBows.makeNewBow(ToolMaterials.STONE, 2.5F, 0.4F));
        ExtraBows.IRON_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "iron_bow"), ExtraBows.makeNewBow(ToolMaterials.IRON, 3F, 0.3F));
        ExtraBows.GOLDEN_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "golden_bow"), ExtraBows.makeNewBow(ToolMaterials.GOLD, 2F, 1F));
        ExtraBows.DIAMOND_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "diamond_bow"), ExtraBows.makeNewBow(ToolMaterials.DIAMOND, 3.5F, 0.2F));
        ExtraBows.NETHERITE_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "netherite_bow"), ExtraBows.makeNewBow(ToolMaterials.NETHERITE, 4F, 0.25F));
        ExtraBows.STONE_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "stone_crossbow"), ExtraBows.makeNewCrossbow(ToolMaterials.STONE, 4F, 0.4F));
        ExtraBows.IRON_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "iron_crossbow"), ExtraBows.makeNewCrossbow(ToolMaterials.IRON, 5F, 0.3F));
        ExtraBows.GOLDEN_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "golden_crossbow"), ExtraBows.makeNewCrossbow(ToolMaterials.GOLD, 3F, 1F));
        ExtraBows.DIAMOND_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "diamond_crossbow"), ExtraBows.makeNewCrossbow(ToolMaterials.DIAMOND, 6F, 0.2F));
        ExtraBows.NETHERITE_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "netherite_crossbow"), ExtraBows.makeNewCrossbow(ToolMaterials.NETHERITE, 7F, 0.25F));
        if(FabricLoader.getInstance().isModLoaded("copperagebackport")) {
            ExtraBows.COPPER_BOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "copper_bow"), ExtraBows.COPPER_BOW = ExtraBows.makeNewBow(new ToolMaterial() {
                @Override public int getDurability() {
                    return 190;
                }
                @Override public float getMiningSpeedMultiplier() {
                    return 5F;
                }
                @Override public float getAttackDamage() {
                    return 1F;
                }
                @Override public int getEnchantability() {
                    return 13;
                }
                @Override public int getMiningLevel() {
                    return 1;
                }
                @Override public Ingredient getRepairIngredient() {
                    return Ingredient.ofItems(Items.COPPER_INGOT);
                }
            }, 2.5F, 0.5F));
            ExtraBows.COPPER_CROSSBOW = Registry.register(Registries.ITEM, Identifier.of(ExtraBows.MOD_ID, "copper_crossbow"), ExtraBows.COPPER_CROSSBOW = ExtraBows.makeNewCrossbow(new ToolMaterial() {
                @Override public int getDurability() {
                    return 190;
                }
                @Override public float getMiningSpeedMultiplier() {
                    return 5F;
                }
                @Override public float getAttackDamage() {
                    return 1F;
                }
                @Override public int getEnchantability() {
                    return 13;
                }
                @Override public int getMiningLevel() {
                    return 1;
                }
                @Override public Ingredient getRepairIngredient() {
                    return Ingredient.ofItems(Items.COPPER_INGOT);
                }
            }, 4F, 0.5F));
        }
        if(SHOW_BOWS_IN_CREATIVE_MENU) ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
            itemGroup.addAfter(Items.BOW, ExtraBows.STONE_BOW);
            if(ExtraBows.COPPER_BOW != null) {
                itemGroup.addAfter(ExtraBows.STONE_BOW, ExtraBows.COPPER_BOW);
                itemGroup.addAfter(ExtraBows.COPPER_BOW, ExtraBows.IRON_BOW);
            }
            else itemGroup.addAfter(ExtraBows.STONE_BOW, ExtraBows.IRON_BOW);
            itemGroup.addAfter(ExtraBows.IRON_BOW, ExtraBows.GOLDEN_BOW);
            itemGroup.addAfter(ExtraBows.GOLDEN_BOW, ExtraBows.DIAMOND_BOW);
            itemGroup.addAfter(ExtraBows.DIAMOND_BOW, ExtraBows.NETHERITE_BOW);
            itemGroup.addAfter(Items.CROSSBOW, ExtraBows.STONE_CROSSBOW);
            if(ExtraBows.COPPER_CROSSBOW != null) {
                itemGroup.addAfter(ExtraBows.STONE_CROSSBOW, ExtraBows.COPPER_CROSSBOW);
                itemGroup.addAfter(ExtraBows.COPPER_CROSSBOW, ExtraBows.IRON_CROSSBOW);
            }
            else itemGroup.addAfter(ExtraBows.STONE_CROSSBOW, ExtraBows.IRON_CROSSBOW);
            itemGroup.addAfter(ExtraBows.IRON_CROSSBOW, ExtraBows.GOLDEN_CROSSBOW);
            itemGroup.addAfter(ExtraBows.GOLDEN_CROSSBOW, ExtraBows.DIAMOND_CROSSBOW);
            itemGroup.addAfter(ExtraBows.DIAMOND_CROSSBOW, ExtraBows.NETHERITE_CROSSBOW);
        });
        ExtraBows.init();
    }
}
