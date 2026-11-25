package com.notunanancyowen.fabric.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class ModelPredicateProviderRegistryAccessor {
    @Inject(method = "register(Lnet/minecraft/item/Item;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/item/ClampedModelPredicateProvider;)V", at = @At("TAIL"))
    private static void copyBowAndCrossbowPredicates(Item item, Identifier id, ClampedModelPredicateProvider provider, CallbackInfo ci) {
        if(Items.BOW.equals(item)) {
            if(ExtraBows.COPPER_BOW != null) ModelPredicateProviderRegistry.register(ExtraBows.COPPER_BOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.STONE_BOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.IRON_BOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.GOLDEN_BOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.DIAMOND_BOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.NETHERITE_BOW, id, provider);
        }
        if(Items.CROSSBOW.equals(item)) {
            if(ExtraBows.COPPER_CROSSBOW != null) ModelPredicateProviderRegistry.register(ExtraBows.COPPER_CROSSBOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.STONE_CROSSBOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.IRON_CROSSBOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.GOLDEN_CROSSBOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.DIAMOND_CROSSBOW, id, provider);
            ModelPredicateProviderRegistry.register(ExtraBows.NETHERITE_CROSSBOW, id, provider);
        }
    }
}
