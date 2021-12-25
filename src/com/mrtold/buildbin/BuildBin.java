package com.mrtold.buildbin;

import com.mrtold.buildbin.commands.BinCommand;
import com.mrtold.buildbin.commands.CopyBuild;
import com.mrtold.buildbin.commands.PasteBuild;
import com.mrtold.buildbin.listeners.BuildBinListener;
import com.mrtold.buildbin.utils.Region;
import com.mrtold.buildbin.utils.StorageMaster;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class BuildBin extends JavaPlugin {

    Logger log = Bukkit.getLogger();
    private static BuildBin instance = null;

    private final Map<UUID, Region> points = new HashMap<>();
    private final StorageMaster storage = new StorageMaster();

    @Override
    public void onEnable(){
        instance = this;
        Objects.requireNonNull(getCommand("bin")).setExecutor(new BinCommand(this));
        Objects.requireNonNull(getCommand("pastebuild")).setExecutor(new PasteBuild(this));
        Objects.requireNonNull(getCommand("copybuild")).setExecutor(new CopyBuild(this));
        getServer().getPluginManager().registerEvents(new BuildBinListener(this), this);

        log.info("BuildBin started");
    }

    public StorageMaster getStorage() {
        return storage;
    }

    public void pointPos(UUID uuid, Block block, boolean first) {
        points.putIfAbsent(uuid, new Region());
        points.get(uuid).setPos(first, block);
    }

    public void clearRegion(UUID uuid) {
        points.remove(uuid);
    }

    public Region getRegion(UUID uuid) {
        return points.get(uuid);
    }

    public static BuildBin getInstance() {
        return instance;
    }

}
