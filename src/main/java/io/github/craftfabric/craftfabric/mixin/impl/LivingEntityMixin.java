package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.mixin.ILivingEntityMixin;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityMixin {

    @Shadow
    public abstract boolean method_6039();

    @Override
    public boolean isBlocking() {
        return method_6039();
    }
}
