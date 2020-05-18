package io.github.craftfabric.craftfabric.accessor.inventory;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BasicInventory.class)
public interface BasicInventoryAccessor {
    @Accessor
    DefaultedList<ItemStack> getStackList();
}
