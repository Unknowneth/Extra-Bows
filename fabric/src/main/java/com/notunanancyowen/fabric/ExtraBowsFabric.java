package com.notunanancyowen.fabric;

import com.notunanancyowen.ExtraBows;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ExtraBowsFabric implements ModInitializer {
    static {
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
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ExtraBows.MAIT = FabricLoader.getInstance().isModLoaded("mob-ai-tweaks");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
            itemGroup.addAfter(Items.BOW, ExtraBows.STONE_BOW);
            itemGroup.addAfter(ExtraBows.STONE_BOW, ExtraBows.IRON_BOW);
            itemGroup.addAfter(ExtraBows.IRON_BOW, ExtraBows.GOLDEN_BOW);
            itemGroup.addAfter(ExtraBows.GOLDEN_BOW, ExtraBows.DIAMOND_BOW);
            itemGroup.addAfter(ExtraBows.DIAMOND_BOW, ExtraBows.NETHERITE_BOW);
            itemGroup.addAfter(Items.CROSSBOW, ExtraBows.STONE_CROSSBOW);
            itemGroup.addAfter(ExtraBows.STONE_CROSSBOW, ExtraBows.IRON_CROSSBOW);
            itemGroup.addAfter(ExtraBows.IRON_CROSSBOW, ExtraBows.GOLDEN_CROSSBOW);
            itemGroup.addAfter(ExtraBows.GOLDEN_CROSSBOW, ExtraBows.DIAMOND_CROSSBOW);
            itemGroup.addAfter(ExtraBows.DIAMOND_CROSSBOW, ExtraBows.NETHERITE_CROSSBOW);
        });
        ExtraBows.init();
    }
}
