package io.github.tr7zw.fabricbukkit.mixin.impl;

import io.github.tr7zw.fabricbukkit.mixin.IBlockRedstoneWireMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RedstoneWireBlock.class)
public abstract class BlockRedstoneWireMixin implements IBlockRedstoneWireMixin {

    @Shadow
    protected abstract int method_10486(int int_1, BlockState blockState_1);

    @Override
    public int getPower(int int_1, BlockState blockState_1) {
        return method_10486(int_1, blockState_1);
    }
}
