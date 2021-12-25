package com.mrtold.buildbin.building.version;

import com.mrtold.buildbin.building.StoredBlock;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.bukkit.util.Vector;

/**
 * @author Mr_Told
 */
public final class StoredBlock_v1_17_R1 extends StoredBlock {

    public StoredBlock_v1_17_R1(World world, int x, int y, int z, Vector base) {
        super(world, x, y, z, base);
    }

    protected TileEntity getTileEntity(Location loc) {
        if (loc.getWorld() == null) return null;

        return ((CraftWorld) loc.getWorld()).getHandle()
                .getTileEntity(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public BlockData getBlockData() {
        return CraftBlockData.newData(null, data);
    }

}
