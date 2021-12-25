package com.mrtold.buildbin.building;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.util.Vector;

import java.io.Serial;
import java.io.Serializable;
import java.util.logging.Level;

/**
 * @author Mr_Told
 */
public abstract class StoredBlock implements Serializable {

    @Serial
    private static transient final long serialVersionUID = -7350415604639743589L;

    protected final String data;
    protected final int x;
    protected final int y;
    protected final int z;
    protected final String nbt;

    public StoredBlock(World world, int x, int y, int z, Vector base) {
        Block b = world.getBlockAt(x, y, z);
        this.data = b.getBlockData().getAsString();
        this.x = b.getX() - base.getBlockX();
        this.y = b.getY() - base.getBlockY();
        this.z = b.getZ() - base.getBlockZ();
        TileEntity te = getTileEntity(new Location(world, x, y, z));
        this.nbt = te == null ? null : te.save(new NBTTagCompound()).toString();
    }

    public void place(Location delta) {
        if (delta.getWorld() == null) return;
        Location toLoc = getLocation(delta.getWorld()).add(delta);
        Block to = delta.getWorld().getBlockAt(toLoc);
        BlockData blockData = getBlockData();

        if (blockData instanceof Bisected && ((Bisected) blockData).getHalf() == Bisected.Half.BOTTOM) {
            Block topBlock = delta.getWorld().getBlockAt(new Location(delta.getWorld(),
                    toLoc.getBlockX(),
                    toLoc.getBlockY() + 1,
                    toLoc.getBlockZ()));
            BlockData topData = blockData.clone();
            ((Bisected) topData).setHalf(Bisected.Half.TOP);
            topBlock.setBlockData(topData, false);
        } else if (blockData instanceof Bed) {
            Block secondBlock = delta.getWorld().getBlockAt(new Location(delta.getWorld(),
                    toLoc.getBlockX(),
                    toLoc.getBlockY(),
                    toLoc.getBlockZ()).add(((Directional) blockData).getFacing().getDirection()
                    .multiply(((Bed) blockData).getPart() == Bed.Part.FOOT ? 1 : -1)));
            BlockData secondData = secondBlock.getBlockData();

            if (!(secondData instanceof Bed) || ((Bed) secondData).getPart() == ((Bed) blockData).getPart()) {
                secondData = blockData.clone();
                ((Bed) secondData).setPart(((Bed) blockData).getPart() == Bed.Part.FOOT ? Bed.Part.HEAD : Bed.Part.FOOT);
                secondBlock.setBlockData(secondData, false);
            }
        }

        to.setBlockData(blockData, true);
        setNbt(toLoc);
    }

    private void setNbt(Location toLoc) {
        NBTTagCompound nbt = getNBT();
        if (nbt == null) return;

        TileEntity toTE = getTileEntity(toLoc);
        if (toTE != null)
            toTE.load(nbt);
    }

    public Location getLocation(World w) {
        return new Location(w, x, y, z);
    }


    public NBTTagCompound getNBT() {
        if (nbt == null) return null;
        try {
            return MojangsonParser.parse(nbt);
        } catch (CommandSyntaxException e) {
            Bukkit.getLogger().log(Level.WARNING, "[BuildBin] Exception while parsing block nbt: ", e);
        }
        return null;
    }

    protected abstract TileEntity getTileEntity(Location loc);

    protected abstract BlockData getBlockData();

}
