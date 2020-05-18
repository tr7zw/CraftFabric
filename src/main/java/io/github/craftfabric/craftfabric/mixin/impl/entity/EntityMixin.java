package io.github.craftfabric.craftfabric.mixin.impl.entity;

import io.github.craftfabric.craftfabric.entity.CraftEntity;
import io.github.craftfabric.craftfabric.link.CraftLink;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin<E extends CraftEntity> implements CraftLink<E> {
    protected CraftEntity craftHandler;

    @Override
    public E getCraftHandler() {
        return (E) this.craftHandler;
    }
}
