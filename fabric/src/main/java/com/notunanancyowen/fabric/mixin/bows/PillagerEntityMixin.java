package com.notunanancyowen.fabric.mixin.bows;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.ExtraBows;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin {
    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/IllagerEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;"))
    private EntityData changeToStoneCrossbow(PillagerEntity instance, ServerWorldAccess serverWorldAccess, LocalDifficulty localDifficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound nbtCompound, Operation<EntityData> original) {
        entityData = original.call(instance, serverWorldAccess, localDifficulty, spawnReason, entityData, nbtCompound);
        if(instance.isPatrolLeader() && instance.getMainHandStack().isOf(Items.CROSSBOW)) {
            var a = ExtraBows.STONE_CROSSBOW.getDefaultStack();
            if (!instance.getMainHandStack().getEnchantments().isEmpty() && a.getNbt() != null) a.getNbt().put("Enchantments", instance.getMainHandStack().getEnchantments());
            instance.setStackInHand(Hand.MAIN_HAND, a);
        }
        return entityData;
    }
}
