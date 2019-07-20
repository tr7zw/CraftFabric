package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.mixin.ITextFormatMixin;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Formatting.class)
public class TextFormatMixin implements ITextFormatMixin {

    @Shadow
    @Final
    private char code;

    @Override
    public char getSectionSignCode() {
        return code;
    }
}
