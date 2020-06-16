package io.github.craftfabric.craftfabric.mixin.impl.entity;

import io.github.craftfabric.craftfabric.entity.CraftLivingEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin<E extends CraftLivingEntity> extends EntityMixin<E> {
}
