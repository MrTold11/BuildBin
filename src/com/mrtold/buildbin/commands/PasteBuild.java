package com.mrtold.buildbin.commands;

import com.mrtold.buildbin.BuildBin;
import com.mrtold.buildbin.utils.StorageMaster;
import org.bukkit.entity.Player;

/**
 * @author Mr_Told
 */
public class PasteBuild extends PlayerCommand {

    public PasteBuild(BuildBin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        if (args.length == 0) return false;
        if (args[0].length() < 1 || args[0].length() > 20) return failure("File name is too long/short", sender);

        plugin.getStorage().loadAndPlace(
                args[0],
                sender,
                () -> failure("File " + args[0] + StorageMaster.extension + " does not exist or was corrupted", sender));
        return true;
    }

}
