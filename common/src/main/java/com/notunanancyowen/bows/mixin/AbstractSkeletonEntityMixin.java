package com.notunanancyowen.bows.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notunanancyowen.bows.ExtraBows;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    @Shadow public abstract void updateAttackType();
    @Shadow @Final private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;
    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "initGoals", at = @At("TAIL"))
    private void lookAtTargetFix(CallbackInfo ci) {
        if(ExtraBows.BALANCED_MOBS && !ExtraBows.MAIT) goalSelector.add(4, new Goal() {
            @Override public boolean canStart() {
                return bowAttackGoal.canStart();
            }
            @Override public boolean shouldRunEveryTick() {
                return bowAttackGoal.shouldRunEveryTick();
            }
            @Override public void tick() {
                if(getTarget() != null) getLookControl().lookAt(getTarget(), 30F, 30F);
            }
            @Override public void start() {
                getMoveControl().strafeTo(1F, 0F);
            }
            @Override public void stop() {
                getMoveControl().strafeTo(0F, 0F);
            }
        });
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void becomeDifferentBow(AbstractSkeletonEntity instance, EquipmentSlot slot, ItemStack stack, Operation<Void> original) {
        if(!ExtraBows.MOBS_SPAWN_WITH_TIERED_BOWS) original.call(instance, slot, stack);
        else if(ExtraBows.COPPER_BOW != null && Registries.ITEM.get(Identifier.of("minecraft", "copper_helmet")) instanceof ArmorItem a && getEquippedStack(EquipmentSlot.HEAD).isOf(a)) original.call(instance, slot, ExtraBows.COPPER_BOW.getDefaultStack());
        else if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.IRON_HELMET)) original.call(instance, slot, ExtraBows.IRON_BOW.getDefaultStack());
        else if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.GOLDEN_HELMET)) original.call(instance, slot, ExtraBows.GOLDEN_BOW.getDefaultStack());
        else if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CHAINMAIL_HELMET)) original.call(instance, slot, ExtraBows.STONE_BOW.getDefaultStack());
        else if(getEquippedStack(EquipmentSlot.HEAD).isOf(Items.DIAMOND_HELMET)) original.call(instance, slot, ExtraBows.DIAMOND_BOW.getDefaultStack());
        else original.call(instance, slot, stack);
    }
    @WrapOperation(method = "updateAttackType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean isHoldingAnyBow(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || isHolding(i -> i.getItem() instanceof BowItem);
    }
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack damageWeapon(AbstractSkeletonEntity instance, Hand hand, Operation<ItemStack> original) {
        ItemStack i = original.call(instance, hand);
        if(!ExtraBows.BALANCED_MOBS) return i;
        i.damage(1, this, b -> {
            b.updateAttackType();
            b.sendToolBreakStatus(hand);
        });
        if(i.getCount() == 0) updateAttackType();
        return i;
    }
    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true, require = 0)
    private void canUseModdedBows(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cir) {
        if(weapon instanceof BowItem) cir.setReturnValue(true);
    }
}
