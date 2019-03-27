package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class MixinEntity {

    // Example for fields

    /*
     * @Shadow protected Random rand;
     * 
     * @Shadow public boolean isAirBorne;
     * 
     * @Shadow public double motionX;
     * 
     * @Shadow public double motionY;
     * 
     * @Shadow public double motionZ;
     * 
     * @Shadow public boolean onGround;
     */
}
