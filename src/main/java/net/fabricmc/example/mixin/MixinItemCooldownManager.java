package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;

@Mixin(ItemCooldownManager.class)
public class MixinItemCooldownManager {

    @Inject(at = @At("HEAD"), method = "isCooldown", cancellable = true)
    public boolean isCooldown(Item item, CallbackInfoReturnable<Boolean> ci) {
	System.out.println("overwriding cooldown");
	ci.setReturnValue(false);
	return false;
    }

    @Inject(at = @At("HEAD"), method = "getCooldownProgress", cancellable = true)
    public float getCooldownProgress(Item item_1, float float_1, CallbackInfoReturnable<Float> ci) {
	ci.setReturnValue(0f);
	return 0;
    }

}
