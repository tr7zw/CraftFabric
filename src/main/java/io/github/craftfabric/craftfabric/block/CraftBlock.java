package io.github.craftfabric.craftfabric.block;

import com.destroystokyo.paper.block.BlockSoundGroup;
import io.github.craftfabric.craftfabric.link.CraftLink;
import io.github.craftfabric.craftfabric.CraftMagicNumbers;
import io.github.craftfabric.craftfabric.inventory.CraftItemStack;
import io.github.craftfabric.craftfabric.world.CraftWorld;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LightType;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CraftBlock implements Block {
    private final net.minecraft.world.World world;
    private final BlockPos position;

    public CraftBlock(net.minecraft.world.World world, BlockPos position) {
        this.world = world;
        this.position = position.toImmutable();
    }

    public static CraftBlock at(net.minecraft.world.World world, BlockPos position) {
        return new CraftBlock(world, position);
    }

    // Utility methods

    public static BlockFace notchToBlockFace(Direction notch) {
        if (notch == null) {
            return BlockFace.SELF;
        }
        switch (notch) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.EAST;
            default:
                return BlockFace.SELF;
        }
    }

    public static Direction blockFaceToNotch(BlockFace face) {
        switch (face) {
            case DOWN:
                return Direction.DOWN;
            case UP:
                return Direction.UP;
            case NORTH:
                return Direction.NORTH;
            case SOUTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.WEST;
            case EAST:
                return Direction.EAST;
            default:
                return null;
        }
    }

    private static PistonMoveReaction notchToPistonMoveReaction(PistonBehavior notch) {
        switch (notch) {
            default:
            case NORMAL:
                return PistonMoveReaction.MOVE;
            case DESTROY:
                return PistonMoveReaction.BREAK;
            case BLOCK:
                return PistonMoveReaction.BLOCK;
            case IGNORE:
                return PistonMoveReaction.IGNORE;
            case PUSH_ONLY:
                return PistonMoveReaction.PUSH_ONLY;
        }
    }

    public static Biome biomeBaseToBiome(net.minecraft.world.biome.Biome base) {
        if (base == null) {
            return null;
        }
        return Biome.valueOf(Registry.BIOME.getId(base).getPath().toUpperCase(java.util.Locale.ENGLISH));
    }

    public static net.minecraft.world.biome.Biome biomeToBiomeBase(Biome biome) {
        if (biome == null) {
            return null;
        }
        return Registry.BIOME.get(new Identifier(biome.name().toLowerCase(java.util.Locale.ENGLISH)));
    }

    private net.minecraft.block.Block getNMSBlock() {
        return getNMS().getBlock();
    }

    public net.minecraft.block.BlockState getNMS() {
        return world.getBlockState(position);
    }

    public @NotNull BlockPos getPosition() {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull World getWorld() {
        return ((CraftLink<World>) world).getCraftHandler();
    }

    public CraftWorld getWorldImpl() {
        return (CraftWorld) getWorld();
    }

    public boolean setTypeAndData(net.minecraft.block.BlockState blockData, boolean applyPhysics) {
        if (!blockData.isAir() && blockData.getBlock() instanceof BlockEntityProvider
                && blockData.getBlock() != getNMSBlock()) {
            world.removeBlockEntity(position);
        }

        if (applyPhysics) {
            return world.setBlockState(position, blockData, 3);
        } else {
            net.minecraft.block.BlockState old = world.getBlockState(position);
            boolean success = world.setBlockState(position, blockData, 2 | 16 | 1024); // NOTIFY | NO_OBSERVER | NO_PLACE (custom)
            if (success) {
                world.updateListeners(position, old, blockData, 3);
            }
            return success;
        }
    }

    // Implementation

    @Override
    public byte getData() {
        throw new UnsupportedOperationException("CraftFabric doesn't support legacy block data!");
    }

    @Override
    public @NotNull BlockData getBlockData() {
        // TODO
        return null;
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {
        setBlockData(data, true);
    }

    @Override
    public @NotNull Block getRelative(int modX, int modY, int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    @Override
    public @NotNull Block getRelative(@NotNull BlockFace face) {
        return getRelative(face, 1);
    }

    @Override
    public @NotNull Block getRelative(@NotNull BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @Override
    public @NotNull Material getType() {
        return CraftMagicNumbers.getMaterial(getNMSBlock());
    }

    @Override
    public void setType(@NotNull Material type) {
        setType(type, true);
    }

    @Override
    public byte getLightLevel() {
        return (byte) world.getLightLevel(position);
    }

    @Override
    public byte getLightFromSky() {
        return (byte) world.getLightLevel(LightType.SKY, position);
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) world.getLightLevel(LightType.BLOCK, position);
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public @NotNull Location getLocation() {
        return new Location(getWorld(), position.getX(), position.getY(), position.getZ());
    }

    @Override
    public @Nullable Location getLocation(@Nullable Location location) {
        if (location != null) {
            location.setWorld(getWorld());
            location.setX(position.getX());
            location.setY(position.getY());
            location.setZ(position.getZ());
            location.setYaw(0);
            location.setPitch(0);
        }
        return location;
    }

    public BlockVector getVector() {
        return new BlockVector(getX(), getY(), getZ());
    }

    @Override
    public @NotNull Chunk getChunk() {
        return getWorld().getChunkAt(this);
    }

    @Override
    public void setBlockData(@NotNull BlockData data, boolean applyPhysics) {
        // TODO
    }

    @Override
    public void setType(@NotNull Material type, boolean applyPhysics) {
        Objects.requireNonNull(type, "type");
        setBlockData(type.createBlockData(), applyPhysics);
    }

    @Override
    public @Nullable BlockFace getFace(@NotNull Block block) {
        for (BlockFace face : BlockFace.values()) {
            if ((this.getX() + face.getModX() == block.getX()) && (this.getY() + face.getModY() == block.getY())
                    && (this.getZ() + face.getModZ() == block.getZ())) {
                return face;
            }
        }
        return null;
    }

    @Override
    public @NotNull BlockState getState() {
        // TODO
        return null;
    }

    @Override
    public @NotNull Biome getBiome() {
        return getWorld().getBiome(getX(), getY(), getZ());
    }

    @Override
    public void setBiome(@NotNull Biome biome) {
        getWorld().setBiome(getX(), getY(), getZ(), biome);
    }

    @Override
    public boolean isBlockPowered() {
        return world.getReceivedStrongRedstonePower(position) > 0;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return world.isReceivingRedstonePower(position);
    }

    @Override
    public boolean isBlockFacePowered(@NotNull BlockFace face) {
        return world.isEmittingRedstonePower(position, blockFaceToNotch(face));
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face) {
        int power = world.getEmittedRedstonePower(position, blockFaceToNotch(face));

        Block relative = getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, ((RedstoneWire) relative.getBlockData()).getPower()) > 0; // TODO: test if this works
        }

        return power > 0;
    }

    @Override
    public int getBlockPower(@NotNull BlockFace face) {
        int power = 0;
        int x = getX();
        int y = getY();
        int z = getZ();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x, y - 1, z), Direction.DOWN)) {
            power = world.getBlockState(new BlockPos(x, y - 1, z)).get(RedstoneWireBlock.POWER);
        }
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x, y + 1, z), Direction.UP)) {
            power = world.getBlockState(new BlockPos(x, y + 1, z)).get(RedstoneWireBlock.POWER);
        }
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x + 1, y, z), Direction.EAST)) {
            power = world.getBlockState(new BlockPos(x + 1, y, z)).get(RedstoneWireBlock.POWER);
        }
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x - 1, y, z), Direction.WEST)) {
            power = world.getBlockState(new BlockPos(x - 1, y, z)).get(RedstoneWireBlock.POWER);
        }
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x, y, z - 1), Direction.NORTH)) {
            power = world.getBlockState(new BlockPos(x, y, z - 1)).get(RedstoneWireBlock.POWER);
        }
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isEmittingRedstonePower(new BlockPos(x, y, z + 1), Direction.SOUTH)) {
            power = world.getBlockState(new BlockPos(x, y, z + 1)).get(RedstoneWireBlock.POWER);
        }
        return power > 0 ? power : (face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
    }

    @Override
    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return getNMS().isAir();
    }

    @Override
    public boolean isLiquid() {
        return (getType() == Material.WATER) || (getType() == Material.LAVA);
    }

    @Override
    public double getTemperature() {
        return world.getBiome(position).getTemperature();
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(getX(), getZ());
    }

    @Override
    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        return notchToPistonMoveReaction(getNMS().getPistonBehavior());
    }

    @Override
    public boolean breakNaturally() {
        return breakNaturally(new ItemStack(Material.AIR));
    }

    @Override
    public boolean breakNaturally(@NotNull ItemStack item) {
        return breakNaturally(item, true);
    }

    @Override
    public @NotNull Collection<ItemStack> getDrops() {
        return getDrops(new ItemStack(Material.AIR));
    }

    @Override
    public @NotNull Collection<ItemStack> getDrops(@NotNull ItemStack tool) {
        net.minecraft.block.BlockState blockState = getNMS();
        net.minecraft.item.ItemStack nms = CraftItemStack.asNMSCopy(tool);
        if (blockState.getMaterial().canBreakByHand() || nms.isEffectiveOn(blockState)) {
            if (world instanceof ServerWorld) {
                return net.minecraft.block.Block.getDroppedStacks(blockState, (ServerWorld) world, position, world.getBlockEntity(position), null, nms)
                        .stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList());
            }
            // TODO: client side?
        } else {
            return Collections.emptyList();
        }
        return null;
    }

    @Override
    public boolean isPassable() {
        return getNMS().getCollisionShape(world, position).isEmpty();
    }

    @Override
    public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        // TODO
        return null;
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        // TODO
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        // TODO
        return null;
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        // TODO
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        // TODO
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof CraftBlock)) return false;
        CraftBlock other = (CraftBlock) object;

        return this.position.equals(other.position) && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() ^ this.getWorld().hashCode();
    }

    @Override
    public String toString() {
        return "CraftBlock{pos=" + position + ",type=" + getType() + ",data=" + getNMS() + ",fluid=" + world.getFluidState(position) + '}';
    }

    @Override
    public @NotNull BlockState getState(boolean useSnapshot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect) {
        net.minecraft.block.Block block = getNMSBlock();
        boolean result = false;

        if (block != null && block != Blocks.AIR) {
            net.minecraft.block.Block.dropStacks(getNMS(), world, position, world.getBlockEntity(position), null, CraftItemStack.asNMSCopy(tool));
            if (triggerEffect) {
                world.playGlobalEvent(org.bukkit.Effect.STEP_SOUND.getId(), position, net.minecraft.block.Block.getRawIdFromState(block.getDefaultState()));
            }
            result = true;
        }

        return setTypeAndData(Blocks.AIR.getDefaultState(), true) && result;
    }

    @Override
    public @NotNull BlockSoundGroup getSoundGroup() {
        // TODO Auto-generated method stub
        return null;
    }
}
