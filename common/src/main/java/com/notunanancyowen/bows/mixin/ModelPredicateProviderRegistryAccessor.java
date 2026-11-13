package com.notunanancyowen.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class ModelPredicateProviderRegistryAccessor {
    @Shadow private static void register(Item item, Identifier id, ClampedModelPredicateProvider provider) {

    }
    @Inject(method = "register(Lnet/minecraft/item/Item;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/item/ClampedModelPredicateProvider;)V", at = @At("TAIL"))
    private static void copyBowAndCrossbowPredicates(Item item, Identifier id, ClampedModelPredicateProvider provider, CallbackInfo ci) {
        if(item.equals(Items.BOW)) {
            if(ExtraBows.COPPER_BOW != null) register(ExtraBows.COPPER_BOW, id, provider);
            register(ExtraBows.STONE_BOW, id, provider);
            register(ExtraBows.IRON_BOW, id, provider);
            register(ExtraBows.GOLDEN_BOW, id, provider);
            register(ExtraBows.DIAMOND_BOW, id, provider);
            register(ExtraBows.NETHERITE_BOW, id, provider);
            if(ExtraBows.DRAGON_BOW != null) register(ExtraBows.DRAGON_BOW, id, provider);
        }
        if(item.equals(Items.CROSSBOW)) {
            if(ExtraBows.COPPER_CROSSBOW != null) register(ExtraBows.COPPER_CROSSBOW, id, provider);
            register(ExtraBows.STONE_CROSSBOW, id, provider);
            register(ExtraBows.IRON_CROSSBOW, id, provider);
            register(ExtraBows.GOLDEN_CROSSBOW, id, provider);
            register(ExtraBows.DIAMOND_CROSSBOW, id, provider);
            register(ExtraBows.NETHERITE_CROSSBOW, id, provider);
            if(ExtraBows.DRAGON_CROSSBOW != null) register(ExtraBows.DRAGON_CROSSBOW, id, provider);
        }
    }
}
