package io.github.craftfabric.craftfabric.accessor.entity.player;

import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerAbilities.class)
public interface PlayerAbilitiesAccessor {

    @Accessor("invulnerable")
    boolean isInvulnerable();

    @Accessor("invulnerable")
    void setInvulnerable(boolean invulnerable);

    @Accessor("flying")
    boolean isFlying();

    @Accessor("flying")
    void setFlying(boolean flying);

    @Accessor("allowFlying")
    boolean allowsFlight();

    @Accessor("allowFlying")
    void setAllowsFlight(boolean allowFlight);

    @Accessor("flySpeed")
    float getFlySpeed();

    @Accessor("flySpeed")
    void setFlySpeed(float flySpeed);

    @Accessor("walkSpeed")
    float getWalkSpeed();

    @Accessor("walkSpeed")
    void setWalkSpeed(float walkSpeed);

}
