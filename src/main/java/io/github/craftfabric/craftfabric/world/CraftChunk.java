package io.github.craftfabric.craftfabric.world;

import com.google.common.base.Preconditions;
import io.github.craftfabric.craftfabric.block.CraftBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public abstract class CraftChunk implements Chunk {
    private static final byte[] emptyData = new byte[2048];
    private static final PalettedContainer<BlockState> emptyBlockIDs = new ChunkSection(0).getContainer();
    private static final byte[] emptySkyLight = new byte[2048];

    static {
        Arrays.fill(emptySkyLight, (byte) 0xFF);
    }

    private final ServerWorld worldServer;
    private final ChunkPos position;
    private WeakReference<net.minecraft.world.chunk.WorldChunk> weakChunk;

    public CraftChunk(net.minecraft.world.chunk.WorldChunk chunk) {
        this.weakChunk = new WeakReference<>(chunk);

        worldServer = (ServerWorld) getHandle().getWorld();
        position = getHandle().getPos();
    }

    static void validateChunkCoordinates(int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(0 <= y && y <= 255, "y out of range (expected 0-255, got %s)", y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }

    public World getWorld() {
        // TODO
        return null;
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.chunk.WorldChunk getHandle() {
        net.minecraft.world.chunk.WorldChunk chunk = weakChunk.get();
        if (chunk == null) {
            chunk = worldServer.getChunk(position.x, position.z); // TODO: is this the right method?
            weakChunk = new WeakReference<>(chunk);
        }
        return chunk;
    }

    void breakLink() {
        weakChunk.clear();
    }

    public int getX() {
        return position.x;
    }

    public int getZ() {
        return position.z;
    }

    public Block getBlock(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);
        return new CraftBlock(worldServer, new BlockPos((getX() << 4) | x, y, (getZ() << 4) | z));
    }

    public Entity[] getEntities() {
        /* TODO
        int count = 0, index = 0;
        WorldChunk chunk = getHandle();

        for (int i = 0; i < 16; i++) {
            count += chunk.entitySections[i].size();
        }

        Entity[] entities = new Entity[count];

        for (int i = 0; i < 16; i++) {

            for (Object obj : chunk.entitySections[i].toArray()) {
                if (!(obj instanceof net.minecraft.entity.Entity)) {
                    continue;
                }

                entities[index++] = ((net.minecraft.entity.Entity) obj).getBukkitEntity();
            }
        }

        return entities;
        */
        return null;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }
}
