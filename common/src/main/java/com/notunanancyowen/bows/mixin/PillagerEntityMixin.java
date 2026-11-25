package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin {
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
    private ItemConvertible changeToStoneCrossbow(ItemConvertible par1) {
        return ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS && PillagerEntity.class.cast(this).isPatrolLeader() ? ExtraBows.STONE_CROSSBOW : par1;
    }
    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/IllagerEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;"))
    private EntityData changeToStoneCrossbow(PillagerEntity instance, ServerWorldAccess serverWorldAccess, LocalDifficulty localDifficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound nbtCompound, Operation<EntityData> original) {
        entityData = original.call(instance, serverWorldAccess, localDifficulty, spawnReason, entityData, nbtCompound);
        if(ExtraBows.MOBS_SPAWN_WITH_TIERED_CROSSBOWS && instance.isPatrolLeader() && instance.getMainHandStack().isOf(Items.CROSSBOW)) {
            var a = ExtraBows.STONE_CROSSBOW.getDefaultStack();
            if (!instance.getMainHandStack().getEnchantments().isEmpty() && a.getNbt() != null) a.getNbt().put("Enchantments", instance.getMainHandStack().getEnchantments());
            instance.setStackInHand(Hand.MAIN_HAND, a);
        }
        return entityData;
    }
    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true, require = 0)
    private void canUseModdedCrossbows(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if(weapon instanceof CrossbowItem) cir.setReturnValue(true);
    }
    @Inject(method = "isCharging", at = @At("HEAD"), cancellable = true)
    private void cancelCharging(CallbackInfoReturnable<Boolean> cir) {
        if(CrossbowItem.isCharged(PillagerEntity.class.cast(this).getActiveItem())) cir.setReturnValue(false);
    }
    @Inject(method = "postShoot", at = @At("HEAD"))
    private void fixBodyRotation(CallbackInfo ci) {
        PillagerEntity me = PillagerEntity.class.cast(this);
        me.getMoveControl().strafeTo(-1F, 0F);
        if(me.getTarget() != null) me.lookAtEntity(me.getTarget(), 90F, 30F);
    }
}
