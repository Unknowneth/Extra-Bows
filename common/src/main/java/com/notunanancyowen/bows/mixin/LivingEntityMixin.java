package com.notunanancyowen.bows.mixin;

import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getActiveItem();
    @Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);
    @Inject(method = "setCurrentHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;)V"))
    private void slowDownOnBowPull(Hand hand, CallbackInfo ci) {
        if((LivingEntity)(Object)this instanceof PlayerEntity) return;
        if(getActiveItem() != null && getActiveItem().get(ExtraBows.STRAFE_SPEED) instanceof Float f && f != 1F) {
            var m = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            Identifier id = Identifier.of(ExtraBows.MOD_ID, "strafe_speed_penalty");
            if(m != null) {
                m.removeModifier(id);
                m.addTemporaryModifier(new EntityAttributeModifier(id, (1F - f) * -m.getBaseValue(), EntityAttributeModifier.Operation.ADD_VALUE));
            }
        }
    }
    @Inject(method = "clearActiveItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setLivingFlag(IZ)V"))
    private void unSlowDown(CallbackInfo ci) {
        if((LivingEntity)(Object)this instanceof PlayerEntity) return;
        var m = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if(m != null) m.removeModifier(Identifier.of(ExtraBows.MOD_ID, "strafe_speed_penalty"));
    }
}
