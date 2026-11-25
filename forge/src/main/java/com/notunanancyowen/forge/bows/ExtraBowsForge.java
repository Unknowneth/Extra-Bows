package com.notunanancyowen.forge.bows;

import com.notunanancyowen.bows.ExtraBows;
import com.notunanancyowen.bows.items.ModifiedRangedWeapon;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ExtraBows.MOD_ID)
public final class ExtraBowsForge {
    //I FUCKING HATE FORGE SO MUCH SMFH
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraBows.MOD_ID);
    static {
        ITEMS.register("stone_bow", () -> ExtraBows.STONE_BOW = ExtraBows.makeNewBow(ToolMaterials.STONE, 2.5F, 0.4F));
        ITEMS.register("iron_bow", () -> ExtraBows.IRON_BOW = ExtraBows.makeNewBow(ToolMaterials.IRON, 3F, 0.3F));
        ITEMS.register("golden_bow", () -> ExtraBows.GOLDEN_BOW = ExtraBows.makeNewBow(ToolMaterials.GOLD, 2F, 1F));
        ITEMS.register("diamond_bow", () -> ExtraBows.DIAMOND_BOW = ExtraBows.makeNewBow(ToolMaterials.DIAMOND, 3.5F, 0.2F));
        ITEMS.register("netherite_bow", () -> ExtraBows.NETHERITE_BOW = ExtraBows.makeNewBow(ToolMaterials.NETHERITE, 4F, 0.25F));
        ITEMS.register("stone_crossbow", () -> ExtraBows.STONE_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.STONE, 2.5F, 0.4F));
        ITEMS.register("iron_crossbow", () -> ExtraBows.IRON_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.IRON, 3F, 0.3F));
        ITEMS.register("golden_crossbow", () -> ExtraBows.GOLDEN_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.GOLD, 2F, 1F));
        ITEMS.register("diamond_crossbow", () -> ExtraBows.DIAMOND_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.DIAMOND, 3.5F, 0.2F));
        ITEMS.register("netherite_crossbow", () -> ExtraBows.NETHERITE_CROSSBOW = ExtraBows.makeNewCrossbow(ToolMaterials.NETHERITE, 4F, 0.25F));
    }
    public ExtraBowsForge(FMLJavaModLoadingContext context) {
        ExtraBows.init();
        ExtraBows.MAIT = ModList.get().isLoaded("mob-ai-tweaks");
        MinecraftForge.EVENT_BUS.register(this);
        ITEMS.register(context.getModEventBus());
        if(ModList.get().isLoaded("copperagebackport")) {
            if(ExtraBows.COPPER_BOW == null) ITEMS.register("copper_bow", () -> ExtraBows.COPPER_BOW = ExtraBows.makeNewBow(new ToolMaterial() {
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
            if(ExtraBows.COPPER_CROSSBOW == null) ITEMS.register("copper_crossbow", () -> ExtraBows.COPPER_CROSSBOW = ExtraBows.makeNewCrossbow(new ToolMaterial() {
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
        context.getContainer().addConfig(new ModConfig(ModConfig.Type.COMMON, ExtraBowsCommonConfig.SPEC, context.getContainer()));
        context.getModEventBus().addListener(this::syncConfigToCommonSource);
        context.getModEventBus().addListener(this::buildContents);
        MinecraftForge.EVENT_BUS.addListener(this::bowStats);
        if(!ExtraBows.MAIT) MinecraftForge.EVENT_BUS.addListener(this::entityMovementFixer);
    }
    private void syncConfigToCommonSource(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ExtraBows.BALANCED_MOBS = ExtraBowsCommonConfig.BALANCED_MOBS.get();
            ExtraBows.MOBS_SPAWN_WITH_TIERED_BOWS = ExtraBowsCommonConfig.MOBS_WITH_BOWS.get();
            ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS = ExtraBowsCommonConfig.MOBS_WITH_CROSSBOWS.get();
        });
    }
    private void entityMovementFixer(LivingEntityUseItemEvent event) {
        if(!ExtraBows.MAIT && ExtraBowsCommonConfig.BALANCED_MOBS.get() && event.getEntity() instanceof MobEntity m && m.getTarget() != null) if(event.getItem().isOf(Items.CROSSBOW) && event.getItem().getItem() instanceof CrossbowItem c) {
            m.lookAtEntity(m.getTarget(), 30F, 30F);
            m.getMoveControl().strafeTo(m.getTarget().distanceTo(m) < c.getRange() * 0.5 ? -1F : -0.01F, 0F);
            m.getNavigation().stop();
        }
    }
    private void bowStats(final ItemTooltipEvent tooltips) {
        if(!ExtraBowsCommonConfig.TOOLTIPS.get() && tooltips == null) return;
        ItemStack item = tooltips.getItemStack();
        if(item.getItem() == null) return;
        if(item.getItem() instanceof ModifiedRangedWeapon || item.getItem() == Items.BOW || item.getItem() == Items.CROSSBOW) {
            var t = tooltips.getToolTip();
            boolean advanced = tooltips.getEntity() != null;
            float rangedDamage = advanced && item.getItem() == Items.BOW ? 2F : advanced && item.getItem() == Items.CROSSBOW ? 3F : item.getItem() instanceof ModifiedRangedWeapon m ? m.getRangedDamage() : 0F;
            if(!item.getEnchantments().isEmpty()) for(int i = 0; i < item.getEnchantments().size(); i++) if(item.getEnchantments().getCompound(i).getString("id").equals("minecraft:power")) {
                rangedDamage += (float)(item.getEnchantments().getCompound(i).getShort("lvl") * 0.5 + 0.5);
                break;
            }
            float strafeSpeed = advanced && item.getItem() == Items.BOW || advanced && item.getItem() == Items.CROSSBOW ? 0.5F : item.getItem() instanceof ModifiedRangedWeapon m ? m.getStrafeSpeed() : 0.2F;
            if(rangedDamage == 0F || strafeSpeed == 0F) return;
            int i = 0;
            for(int j = 0; j < t.size(); j++) if(t.get(j).contains(Text.translatable("item.modifiers.mainhand"))) {
                i = j;
                break;
            }
            if(t.size() == 1) if(i == 0) {
                t.add(ScreenTexts.EMPTY);
                t.add(Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY));
                if(rangedDamage > 0F) t.add(Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
                if(strafeSpeed > 0F) t.add(Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
                return;
            }
            else {
                if(rangedDamage > 0F) t.add(i + 1, Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
                if(strafeSpeed > 0F) t.add(i + 1, Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
                return;
            }
            if(i == 0) {
                advanced &= tooltips.getFlags().isAdvanced();
                for(i = t.size() - 1; i >= 0 && (t.get(i).contains(Text.translatable("item.disabled")) || (advanced && item.getNbt() != null && t.get(i).contains(Text.translatable("item.nbt_tags", item.getNbt().getKeys().size()))) || (advanced && t.get(i).contains(Text.literal(Registries.ITEM.getId(item.getItem()).toString()))) || (advanced && item.isDamaged() && t.get(i).contains(Text.translatable("item.durability", item.getMaxDamage() - item.getDamage(), item.getMaxDamage()))) || t.get(i).contains(Text.translatable("item.canPlace")) || t.get(i).contains(Text.translatable("item.canBreak")) || t.get(i).contains(Text.translatable("item.unbreakable"))); i--) if((t.get(i).contains(Text.translatable("item.canPlace")) || t.get(i).contains(Text.translatable("item.canBreak"))) && i > 1) i--;
                i++;
                if(strafeSpeed > 0F) t.add(i, Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
                if(rangedDamage > 0F) t.add(i, Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
                t.add(i, Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY));
                t.add(i, ScreenTexts.EMPTY);
                return;
            }
            if(strafeSpeed > 0F) t.add(i + 1, Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(strafeSpeed)).formatted(Formatting.DARK_GREEN));
            if(rangedDamage > 0F) t.add(i + 1, Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(rangedDamage)).formatted(Formatting.DARK_GREEN));
        }
    }
    private void buildContents(final BuildCreativeModeTabContentsEvent itemGroup) {
        if(ExtraBowsCommonConfig.CREATIVE_MENU.get() && itemGroup.getTabKey() == ItemGroups.COMBAT) try {
            itemGroup.getEntries().putAfter(Items.BOW.getDefaultStack(), ExtraBows.STONE_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            if(ExtraBows.COPPER_BOW != null) {
                itemGroup.getEntries().putAfter(ExtraBows.STONE_BOW.getDefaultStack(), ExtraBows.COPPER_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                itemGroup.getEntries().putAfter(ExtraBows.COPPER_BOW.getDefaultStack(), ExtraBows.IRON_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
            else itemGroup.getEntries().putAfter(ExtraBows.STONE_BOW.getDefaultStack(), ExtraBows.IRON_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.IRON_BOW.getDefaultStack(), ExtraBows.GOLDEN_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.GOLDEN_BOW.getDefaultStack(), ExtraBows.DIAMOND_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.DIAMOND_BOW.getDefaultStack(), ExtraBows.NETHERITE_BOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(Items.CROSSBOW.getDefaultStack(), ExtraBows.STONE_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            if(ExtraBows.COPPER_CROSSBOW != null) {
                itemGroup.getEntries().putAfter(ExtraBows.STONE_CROSSBOW.getDefaultStack(), ExtraBows.COPPER_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                itemGroup.getEntries().putAfter(ExtraBows.COPPER_CROSSBOW.getDefaultStack(), ExtraBows.IRON_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
            else itemGroup.getEntries().putAfter(ExtraBows.STONE_CROSSBOW.getDefaultStack(), ExtraBows.IRON_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.IRON_CROSSBOW.getDefaultStack(), ExtraBows.GOLDEN_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.GOLDEN_CROSSBOW.getDefaultStack(), ExtraBows.DIAMOND_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            itemGroup.getEntries().putAfter(ExtraBows.DIAMOND_CROSSBOW.getDefaultStack(), ExtraBows.NETHERITE_CROSSBOW.getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }
        catch (Throwable t) {
            //FUCK IT, WE BALL
            ExtraBows.LOGGER.info(t.getMessage(), t);
        }
    }
    @SubscribeEvent public void onServerStarting(ServerStartingEvent event) {
    }
    @Mod.EventBusSubscriber(modid = ExtraBows.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent public static void movePlayer(final MovementInputUpdateEvent move) {
            var p = move.getEntity();
            if(p != null && !p.hasVehicle() && p.isUsingItem() && p.getActiveItem() != null && (p.getActiveItem().getItem() == Items.BOW || p.getActiveItem().getItem() == Items.CROSSBOW)) {
                move.getInput().movementForward *= 2.5F;
                move.getInput().movementSideways *= 2.5F;
            }
        }
        @SubscribeEvent public static void registerBowAnimations(final FMLClientSetupEvent clientSetupEvent) {
            clientSetupEvent.enqueueWork(() -> {
                makeCustomBow(ExtraBows.STONE_BOW);
                makeCustomBow(ExtraBows.IRON_BOW);
                makeCustomBow(ExtraBows.GOLDEN_BOW);
                makeCustomBow(ExtraBows.DIAMOND_BOW);
                makeCustomBow(ExtraBows.NETHERITE_BOW);
                makeCustomCrossbow(ExtraBows.STONE_CROSSBOW);
                makeCustomCrossbow(ExtraBows.IRON_CROSSBOW);
                makeCustomCrossbow(ExtraBows.GOLDEN_CROSSBOW);
                makeCustomCrossbow(ExtraBows.DIAMOND_CROSSBOW);
                makeCustomCrossbow(ExtraBows.NETHERITE_CROSSBOW);
                if(ExtraBows.COPPER_BOW != null) makeCustomBow(ExtraBows.COPPER_BOW);
                if(ExtraBows.COPPER_CROSSBOW != null) makeCustomCrossbow(ExtraBows.COPPER_CROSSBOW);
                registered = true;
            });
        }
        private static boolean registered = false;
        @SubscribeEvent public static void forceRegisterBowAnimations(final TickEvent.ClientTickEvent s) {
            if(registered) return;
            registered = true;
            makeCustomBow(ExtraBows.STONE_BOW);
            makeCustomBow(ExtraBows.IRON_BOW);
            makeCustomBow(ExtraBows.GOLDEN_BOW);
            makeCustomBow(ExtraBows.DIAMOND_BOW);
            makeCustomBow(ExtraBows.NETHERITE_BOW);
            makeCustomCrossbow(ExtraBows.STONE_CROSSBOW);
            makeCustomCrossbow(ExtraBows.IRON_CROSSBOW);
            makeCustomCrossbow(ExtraBows.GOLDEN_CROSSBOW);
            makeCustomCrossbow(ExtraBows.DIAMOND_CROSSBOW);
            makeCustomCrossbow(ExtraBows.NETHERITE_CROSSBOW);
            if(ExtraBows.COPPER_BOW != null) makeCustomBow(ExtraBows.COPPER_BOW);
            if(ExtraBows.COPPER_CROSSBOW != null) makeCustomCrossbow(ExtraBows.COPPER_CROSSBOW);
        }
        private static void makeCustomBow(Item item) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "pull"), (stack, world, entity, seed) -> entity == null || entity.getActiveItem() != stack ? 0.0F : (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F);
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
        }
        private static void makeCustomCrossbow(Item item) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "pull"), (stack, world, entity, seed) -> entity == null || CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / CrossbowItem.getPullTime(stack));
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
            ModelPredicateProviderRegistry.register(item, Identifier.of("minecraft", "firework"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) && CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
        }
    }
}
