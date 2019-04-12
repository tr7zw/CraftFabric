package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.mixin.IPlayerEntityMixin;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntityMixin {

    @Shadow
    protected abstract void closeGui();

    @Shadow
    public abstract int method_7349();

    @Override
    public void closeInventory() {
        closeGui();
    }

    @Override
    public int getExpToLevel() {
        return method_7349();
    }
}
