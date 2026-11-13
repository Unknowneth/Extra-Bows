package com.notunanancyowen.mixin.bows;

import com.notunanancyowen.items.ModifiedRangedWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getActiveItem();
    @Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);
    @Inject(method = "setCurrentHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;emitGameEvent(Lnet/minecraft/world/event/GameEvent;)V"))
    private void slowDownOnBowPull(Hand hand, CallbackInfo ci) {
        if((LivingEntity)(Object)this instanceof PlayerEntity) return;
        if(getActiveItem() != null && (getActiveItem().getItem() instanceof ModifiedRangedWeapon || getActiveItem().getItem() == Items.BOW || getActiveItem().getItem() == Items.CROSSBOW)) {
            var m = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if(m != null) {
                UUID id = null;
                for(var n : m.getModifiers()) if(n.getName().equals("strafe_speed_penalty")) {
                    id = n.getId();
                    break;
                }
                if(id != null) m.removeModifier(id);
                m.addTemporaryModifier(new EntityAttributeModifier("strafe_speed_penalty", (getActiveItem().getItem() instanceof ModifiedRangedWeapon f ? 1F - f.getStrafeSpeed() : 0.5F) * -m.getBaseValue(), EntityAttributeModifier.Operation.ADDITION));
            }
        }
    }
    @Inject(method = "clearActiveItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setLivingFlag(IZ)V"))
    private void unSlowDown(CallbackInfo ci) {
        if((LivingEntity)(Object)this instanceof PlayerEntity) return;
        var m = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if(m != null) {
            UUID id = null;
            for(var n : m.getModifiers()) if(n.getName().equals("strafe_speed_penalty")) {
                id = n.getId();
                break;
            }
            if(id != null) m.removeModifier(id);
        }
    }
}
