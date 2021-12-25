package com.mrtold.buildbin.building;

import com.mrtold.buildbin.BuildBin;
import com.mrtold.buildbin.building.version.StoredBlock_v1_17_R1;
import com.mrtold.buildbin.utils.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mr_Told
 */
public class Building implements Serializable {

    @Serial
    private static transient final long serialVersionUID = -5458418440395543524L;

    final String name;
    final Vector vec;
    final List<Set<StoredBlock>> layers;

    transient int taskId = Integer.MIN_VALUE;

    public Building(String name, Region region, Location viewer) {
        this.name = name;
        Block first = region.getFirst();
        Block second = region.getSecond();
        World world = viewer.getWorld();
        if (world == null) {
            vec = null;
            layers = null;
            return;
        }

        int xMin = Math.min(first.getX(), second.getX());
        int xMax = Math.max(first.getX(), second.getX());
        int yMin = Math.min(first.getY(), second.getY());
        int yMax = Math.max(first.getY(), second.getY());
        int zMin = Math.min(first.getZ(), second.getZ());
        int zMax = Math.max(first.getZ(), second.getZ());
        int layerArea = (xMax - xMin + 1) * (zMax - zMin + 1) + 1;

        layers = new ArrayList<>(zMax - zMin);
        Vector base = new Vector(xMin, yMin, zMin);
        vec = new Vector(viewer.getBlockX(), viewer.getBlockY(), viewer.getBlockZ()).subtract(base);

        for(int y = yMin; y <= yMax; y++) {
            int finalY = y;
            int id = Bukkit.getScheduler().runTaskLater(BuildBin.getInstance(), () -> {

                Set<StoredBlock> layer = new HashSet<>(layerArea, 1f);
                for(int x = xMin; x <= xMax; x++) {
                    for(int z = zMin; z <= zMax; z++)
                        layer.add(new StoredBlock_v1_17_R1(world, x, finalY, z, base));
                }
                layers.add(layer);

            }, (long) ((y - yMin) * 0.000158 * layerArea)).getTaskId();
            if (y == yMax)
                taskId = id;
        }
    }

    public boolean created() {
        return taskId == Integer.MIN_VALUE ||
                (!Bukkit.getScheduler().isQueued(taskId) && !Bukkit.getScheduler().isCurrentlyRunning(taskId));
    }

    public void placeBuild(Player player) {
        Location loc = player.getLocation();
        Bukkit.getScheduler().runTaskAsynchronously(BuildBin.getInstance(), () -> {
            while (!created()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }

            if (created()) place(loc, player);
        });
    }

    private void place(Location playerLoc, Player player) {
        World world = playerLoc.getWorld();
        if (layers == null || layers.size() == 0) {
            player.sendMessage(ChatColor.RED + "This building was not initialized correctly.");
            return;
        }
        if (world == null) {
            player.sendMessage(ChatColor.RED + "Your world is null");
            return;
        }

        Location deltaLoc =
                new Location(world, playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ())
                        .subtract(vec);
        player.sendMessage("Pasting building at " +
                deltaLoc.getBlockX() + ", " +
                deltaLoc.getBlockY() + ", " +
                deltaLoc.getBlockZ());

        placeLayerRec(0, deltaLoc);
    }

    private void placeLayerRec(int i, Location deltaLoc) {
        Bukkit.getScheduler().runTaskLater(BuildBin.getInstance(), () -> {

            for (StoredBlock block : layers.get(i))
                block.place(deltaLoc);

            if (i < layers.size() - 1)
                placeLayerRec(i + 1, deltaLoc);
        }, 1);
    }

    public String getName() {
        return name;
    }

}
