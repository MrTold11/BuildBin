package com.mrtold.buildbin.utils;

import com.mrtold.buildbin.BuildBin;
import com.mrtold.buildbin.building.Building;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Mr_Told
 */
public class StorageMaster {

    final Logger logger = Bukkit.getLogger();
    final File pathFile = new File("plugins" + File.separator + "BuildBin");
    public static final String extension = ".mcbld";

    public void save(Building building, Runnable onSuccess, Runnable onFail) {
        Bukkit.getScheduler().runTaskAsynchronously(BuildBin.getInstance(), () -> {
            boolean ok = false;
            while (!building.created()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
            if (building.created())
                ok = saveFile(building);

            boolean finalOk = ok;
            Bukkit.getScheduler().runTask(BuildBin.getInstance(), () -> {
                if (finalOk) onSuccess.run();
                else onFail.run();
            });
        });
    }

    private boolean saveFile(Building building) {
        checkPluginDir();

        try (BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(
                        new File(pathFile, building.getName() + extension)
                )))) {
            out.writeObject(building);
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "[BuildBin] An exception occurred during building save: ", e);
            return false;
        }
    }

    private void checkPluginDir() {
        if (!pathFile.exists()) {
            if (!pathFile.mkdirs())
                logger.log(Level.WARNING, "Could not create BuildBin plugin folder!");
        }
    }

    public void loadAndPlace(String name, Player sender, Runnable onFail) {
        Bukkit.getScheduler().runTaskAsynchronously(BuildBin.getInstance(), () -> {

            try (BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(new FileInputStream(
                            new File(pathFile, name + extension)
                    )))) {
                Building building = (Building) in.readObject();
                Bukkit.getScheduler().runTask(BuildBin.getInstance(), () -> building.placeBuild(sender));
            } catch (ClassNotFoundException | IOException e) {
                if (e instanceof ClassNotFoundException)
                    logger.log(Level.WARNING, "[BuildBin] An exception occurred during building load: ", e);
                Bukkit.getScheduler().runTask(BuildBin.getInstance(), onFail);
            }
        });
    }

}
