package io.github.craftfabric.craftfabric.accessor.util;

import net.minecraft.client.util.TextFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextFormat.class)
public interface FormattingAccessor {
    @Accessor
    char getCode();
}
