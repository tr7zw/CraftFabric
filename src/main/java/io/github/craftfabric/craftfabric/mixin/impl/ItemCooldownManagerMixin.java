package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.mixin.IItemCooldownManagerMixin;
import net.minecraft.entity.player.ItemCooldownManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemCooldownManager.class)
public class ItemCooldownManagerMixin implements IItemCooldownManagerMixin {

    @Shadow
    private int tick;

    @Override
    public int getTick() {
        return tick;
    }

}
