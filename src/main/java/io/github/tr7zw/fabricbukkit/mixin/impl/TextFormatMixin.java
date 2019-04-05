package io.github.tr7zw.fabricbukkit.mixin.impl;

import io.github.tr7zw.fabricbukkit.mixin.ITextFormatMixin;
import net.minecraft.text.TextFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextFormat.class)
public class TextFormatMixin implements ITextFormatMixin {

    @Shadow
    @Final
    private char sectionSignCode;

    @Override
    public char getSectionSignCode() {
        return sectionSignCode;
    }
}
