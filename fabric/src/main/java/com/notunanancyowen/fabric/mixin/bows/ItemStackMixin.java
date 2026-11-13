package com.notunanancyowen.fabric.mixin.bows;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.sugar.Local;
import com.notunanancyowen.items.ModifiedRangedWeapon;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();
    @Shadow public abstract NbtList getEnchantments();
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;isEmpty()Z"))
    private void appendRangedDamage(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list, @Local Multimap<EntityAttribute, EntityAttributeModifier> multimap, @Local EquipmentSlot equipmentSlot) {
        if(player != null && equipmentSlot == EquipmentSlot.MAINHAND && multimap.isEmpty() && getItem() instanceof ModifiedRangedWeapon f) {
            double d = f.getRangedDamage();
            if(!getEnchantments().isEmpty()) for(int i = 0; i < getEnchantments().size(); i++) if(getEnchantments().getCompound(i).getString("id").equals("minecraft:power")) {
                d += getEnchantments().getCompound(i).getShort("lvl") * 0.5 + 0.5;
                break;
            }
            list.add(ScreenTexts.EMPTY);
            list.add(Text.translatable("item.modifiers." + equipmentSlot.getName()).formatted(Formatting.GRAY));
            list.add(ScreenTexts.space().append(Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(d))).formatted(Formatting.DARK_GREEN));
            list.add(ScreenTexts.space().append(Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(f.getStrafeSpeed()))).formatted(Formatting.DARK_GREEN));
        }
    }
    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 6, shift = At.Shift.AFTER))
    private void appendRangedDamage(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list, @Local EquipmentSlot equipmentSlot) {
        if(player != null && equipmentSlot == EquipmentSlot.MAINHAND && getItem() instanceof ModifiedRangedWeapon f) {
            double d = f.getRangedDamage();
            if(!getEnchantments().isEmpty()) for(int i = 0; i < getEnchantments().size(); i++) if(getEnchantments().getCompound(i).getString("id").equals("minecraft:power")) {
                d += getEnchantments().getCompound(i).getShort("lvl") * 0.5 + 0.5;
                break;
            }
            list.add(ScreenTexts.space().append(Text.translatable("attributes.bows.ranged_damage", ItemStack.MODIFIER_FORMAT.format(d))).formatted(Formatting.DARK_GREEN));
            list.add(ScreenTexts.space().append(Text.translatable("attributes.bows.strafe_speed", ItemStack.MODIFIER_FORMAT.format(f.getStrafeSpeed()))).formatted(Formatting.DARK_GREEN));
        }
    }
}