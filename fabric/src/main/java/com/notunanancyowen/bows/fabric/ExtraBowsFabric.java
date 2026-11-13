package com.notunanancyowen.bows.fabric;

import com.notunanancyowen.bows.ExtraBows;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public final class ExtraBowsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraBows.RANGED_DAMAGE = ExtraBows.register("bows:ranged_damage", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT), false);
        ExtraBows.STRAFE_SPEED = ExtraBows.register("bows:strafe_speed", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT), false);
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
                @Override public TagKey<Block> getInverseTag() {
                    return BlockTags.INCORRECT_FOR_STONE_TOOL;
                }
                @Override public int getEnchantability() {
                    return 13;
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
                @Override public TagKey<Block> getInverseTag() {
                    return BlockTags.INCORRECT_FOR_STONE_TOOL;
                }
                @Override public int getEnchantability() {
                    return 13;
                }
                @Override public Ingredient getRepairIngredient() {
                    return Ingredient.ofItems(Items.COPPER_INGOT);
                }
            }, 4F, 0.5F));
        }
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
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
        DefaultItemComponentEvents.MODIFY.register(m -> {
            if(FabricLoader.getInstance().isModLoaded("dragonloot")) {
                m.modify(Registries.ITEM.get(Identifier.of("dragonloot", "dragon_bow")), component -> component.add(ExtraBows.RANGED_DAMAGE, 4.5F).add(ExtraBows.STRAFE_SPEED, 0.3F));
                m.modify(Registries.ITEM.get(Identifier.of("dragonloot", "dragon_crossbow")), component -> component.add(ExtraBows.RANGED_DAMAGE, 8F).add(ExtraBows.STRAFE_SPEED, 0.3F));
            }
            m.modify(Items.BOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, ToolMaterials.WOOD.getDurability()).add(ExtraBows.RANGED_DAMAGE, 2F).add(ExtraBows.STRAFE_SPEED, 0.5F).build());
            m.modify(Items.CROSSBOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, ToolMaterials.WOOD.getDurability()).add(ExtraBows.RANGED_DAMAGE, 3F).add(ExtraBows.STRAFE_SPEED, 0.5F).build());
        });
        ExtraBows.MAIT = FabricLoader.getInstance().isModLoaded("mob-ai-tweaks");
        ExtraBows.init();
    }
}
