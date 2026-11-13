package com.notunanancyowen.bows.neoforge;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.*;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ExtraBows.MOD_ID)
public final class ExtraBowsNeoForge {
    public ExtraBowsNeoForge(IEventBus modEventBus) {
        ExtraBows.init();
        ExtraBows.NeoForge = true;
        ExtraBows.MAIT = ModList.get().isLoaded("mob-ai-tweaks");
        modEventBus.addListener(this::registerItems);
        modEventBus.addListener(this::buildContents);
        modEventBus.addListener(this::modifyComponents);
        NeoForge.EVENT_BUS.addListener(this::modifyDescription);
    }
    private void registerItems(final RegisterEvent event) {
        event.register(RegistryKeys.DATA_COMPONENT_TYPE, registry -> {
            registry.register(Identifier.of(ExtraBows.MOD_ID, "ranged_damage"), ExtraBows.RANGED_DAMAGE = ExtraBows.register("bows:ranged_damage", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT), true));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "strafe_speed"), ExtraBows.STRAFE_SPEED = ExtraBows.register("bows:strafe_speed", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT), true));
        });
        event.register(RegistryKeys.ITEM, registry -> {
            registry.register(Identifier.of(ExtraBows.MOD_ID, "stone_bow"), ExtraBows.STONE_BOW = ExtraBows.makeNewBow(ToolMaterials.STONE, 2.5F, 0.4F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "iron_bow"), ExtraBows.IRON_BOW = ExtraBows.makeNewBow(ToolMaterials.IRON, 3F, 0.3F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "golden_bow"), ExtraBows.GOLDEN_BOW = ExtraBows.makeNewBow(ToolMaterials.GOLD, 2F, 1F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "diamond_bow"), ExtraBows.DIAMOND_BOW = ExtraBows.makeNewBow(ToolMaterials.DIAMOND, 3.5F, 0.2F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "netherite_bow"), ExtraBows.NETHERITE_BOW = ExtraBows.makeNewBow(ToolMaterials.NETHERITE, 4F, 0.25F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "stone_crossbow"), ExtraBows.STONE_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.STONE, 4F, 0.4F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "iron_crossbow"), ExtraBows.IRON_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.IRON, 5F, 0.3F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "golden_crossbow"), ExtraBows.GOLDEN_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.GOLD, 3F, 1F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "diamond_crossbow"), ExtraBows.DIAMOND_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.DIAMOND, 6F, 0.2F));
            registry.register(Identifier.of(ExtraBows.MOD_ID, "netherite_crossbow"), ExtraBows.NETHERITE_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.NETHERITE, 7F, 0.25F));
            if(ModList.get().isLoaded("copperagebackport")) {
                if(ExtraBows.COPPER_BOW == null) registry.register(Identifier.of(ExtraBows.MOD_ID, "copper_bow"), ExtraBows.COPPER_BOW = ExtraBows.makeNewBow(new ToolMaterial() {
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
                if(ExtraBows.COPPER_CROSSBOW == null) registry.register(Identifier.of(ExtraBows.MOD_ID, "copper_crossbow"), ExtraBows.COPPER_CROSSBOW = ExtraBows.makeNewCrossbow(new ToolMaterial() {
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
            if(ModList.get().isLoaded("ender_dragon_loot")) {
                if(ExtraBows.DRAGON_BOW == null) registry.register(Identifier.of("ender_dragon_loot", "dragon_bow"), ExtraBows.DRAGON_BOW = ExtraBows.makeNewBow(new ToolMaterial() {
                    @Override public int getDurability() {
                        return 2785;
                    }
                    @Override public float getMiningSpeedMultiplier() {
                        return 11F;
                    }
                    @Override public float getAttackDamage() {
                        return 5F;
                    }
                    @Override public TagKey<Block> getInverseTag() {
                        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
                    }
                    @Override public int getEnchantability() {
                        return 20;
                    }
                    @Override public Ingredient getRepairIngredient() {
                        return Ingredient.ofItems(Registries.ITEM.get(Identifier.of("ender_dragon_loot", "dragon_scale")));
                    }
                }, 4.5F, 0.3F));
                if(ExtraBows.DRAGON_CROSSBOW == null) registry.register(Identifier.of("ender_dragon_loot", "dragon_crossbow"), ExtraBows.DRAGON_CROSSBOW = ExtraBows.makeNewCrossbow(new ToolMaterial() {
                    @Override public int getDurability() {
                        return 2785;
                    }
                    @Override public float getMiningSpeedMultiplier() {
                        return 11F;
                    }
                    @Override public float getAttackDamage() {
                        return 5F;
                    }
                    @Override public TagKey<Block> getInverseTag() {
                        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
                    }
                    @Override public int getEnchantability() {
                        return 20;
                    }
                    @Override public Ingredient getRepairIngredient() {
                        return Ingredient.ofItems(Registries.ITEM.get(Identifier.of("ender_dragon_loot", "dragon_scale")));
                    }
                }, 8F, 0.3F));
            }
        });
    }
    private void buildContents(final BuildCreativeModeTabContentsEvent itemGroup) {
        if(itemGroup.getTabKey() == ItemGroups.COMBAT) {
            itemGroup.insertAfter(Items.BOW.getDefaultStack(), ExtraBows.STONE_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            if(ExtraBows.COPPER_BOW != null) {
                itemGroup.insertAfter(ExtraBows.STONE_BOW.getDefaultStack(), ExtraBows.COPPER_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                itemGroup.insertAfter(ExtraBows.COPPER_BOW.getDefaultStack(), ExtraBows.IRON_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
            else itemGroup.insertAfter(ExtraBows.STONE_BOW.getDefaultStack(), ExtraBows.IRON_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.IRON_BOW.getDefaultStack(), ExtraBows.GOLDEN_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.GOLDEN_BOW.getDefaultStack(), ExtraBows.DIAMOND_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.DIAMOND_BOW.getDefaultStack(), ExtraBows.NETHERITE_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(Items.CROSSBOW.getDefaultStack(), ExtraBows.STONE_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            if(ExtraBows.COPPER_CROSSBOW != null) {
                itemGroup.insertAfter(ExtraBows.STONE_CROSSBOW.getDefaultStack(), ExtraBows.COPPER_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                itemGroup.insertAfter(ExtraBows.COPPER_CROSSBOW.getDefaultStack(), ExtraBows.IRON_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
            else itemGroup.insertAfter(ExtraBows.STONE_CROSSBOW.getDefaultStack(), ExtraBows.IRON_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.IRON_CROSSBOW.getDefaultStack(), ExtraBows.GOLDEN_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.GOLDEN_CROSSBOW.getDefaultStack(), ExtraBows.DIAMOND_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.insertAfter(ExtraBows.DIAMOND_CROSSBOW.getDefaultStack(), ExtraBows.NETHERITE_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
    private void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.BOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, ToolMaterials.WOOD.getDurability()).add(ExtraBows.RANGED_DAMAGE, 2F).add(ExtraBows.STRAFE_SPEED, 0.5F).build());
        event.modify(Items.CROSSBOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, ToolMaterials.WOOD.getDurability()).add(ExtraBows.RANGED_DAMAGE, 3F).add(ExtraBows.STRAFE_SPEED, 0.5F).build());
    }
    private void modifyDescription(final AddAttributeTooltipsEvent tooltips) {
        float rangedDamage = 0F;
        float strafeSpeed = 0F;
        if(!tooltips.getStack().isEmpty()) {
            if(tooltips.getStack().getComponents().get(ExtraBows.RANGED_DAMAGE) instanceof Float f) rangedDamage = f;
            if(tooltips.getStack().getComponents().get(ExtraBows.STRAFE_SPEED) instanceof Float f) strafeSpeed = f;
        }
        if(rangedDamage == 0F || strafeSpeed == 0F) return;
        tooltips.addTooltipLines(ScreenTexts.EMPTY);
        tooltips.addTooltipLines(Text.translatable("item.modifiers." + AttributeModifierSlot.HAND.asString()).formatted(Formatting.GRAY));
        if(rangedDamage > 0F) tooltips.addTooltipLines(Text.translatable("attributes.bows.ranged_damage", AttributeModifiersComponent.DECIMAL_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
        if(strafeSpeed > 0F) tooltips.addTooltipLines(Text.translatable("attributes.bows.strafe_speed", AttributeModifiersComponent.DECIMAL_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
    }
}
