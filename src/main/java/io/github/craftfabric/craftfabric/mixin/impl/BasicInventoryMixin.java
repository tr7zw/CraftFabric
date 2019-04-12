package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.mixin.IBasicInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BasicInventory.class)
public class BasicInventoryMixin implements IBasicInventory {

    @Shadow
    private DefaultedList<ItemStack> stackList;

    @Override
    public DefaultedList<ItemStack> getContent() {
        return stackList;
    }

}
