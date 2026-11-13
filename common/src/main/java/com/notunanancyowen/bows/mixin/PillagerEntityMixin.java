package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin {
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
    private ItemConvertible changeToStoneCrossbow(ItemConvertible par1) {
        return ((PillagerEntity)(Object)this).isCaptain() ? ExtraBows.STONE_CROSSBOW : par1;
    }
    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/IllagerEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;"))
    private EntityData changeToStoneCrossbow(PillagerEntity instance, ServerWorldAccess serverWorldAccess, LocalDifficulty localDifficulty, SpawnReason spawnReason, EntityData entityData, Operation<EntityData> original) {
        entityData = original.call(instance, serverWorldAccess, localDifficulty, spawnReason, entityData);
        if(instance.isCaptain() && instance.getMainHandStack().isOf(Items.CROSSBOW)) {
            var a = ExtraBows.STONE_CROSSBOW.getDefaultStack();
            if (instance.getMainHandStack().get(DataComponentTypes.ENCHANTMENTS) instanceof ItemEnchantmentsComponent i) a.set(DataComponentTypes.ENCHANTMENTS, i);
            instance.setStackInHand(Hand.MAIN_HAND, a);
        }
        return entityData;
    }
    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true, require = 0)
    private void canUseModdedCrossbows(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if(weapon instanceof CrossbowItem) cir.setReturnValue(true);
    }
}
