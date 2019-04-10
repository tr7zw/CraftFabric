package io.github.tr7zw.fabricbukkit.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public interface IBasicInventory {

	public DefaultedList<ItemStack> getContent();
	
}
