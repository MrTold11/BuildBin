package com.mrtold.buildbin.commands;

import com.mrtold.buildbin.BuildBin;
import com.mrtold.buildbin.building.Building;
import com.mrtold.buildbin.utils.Region;
import com.mrtold.buildbin.utils.StorageMaster;
import org.bukkit.entity.Player;

/**
 * @author Mr_Told
 */
public class CopyBuild extends PlayerCommand {

    public CopyBuild(BuildBin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        Region region = plugin.getRegion(sender.getUniqueId());
        if (region == null || !region.validate()) return failure("Select two corners of a building first (use selection tool: /bin)", sender);
        if (args.length == 0) return false;
        if (args[0].length() < 1 || args[0].length() > 20) return failure("File name is too long/short", sender);

        plugin.getStorage().save(
                new Building(args[0], region, sender.getLocation()),
                () -> success("Building was saved to " + args[0] + StorageMaster.extension, sender),
                () -> failure("Something went wrong while saving, check server logs", sender));
        return true;
    }

}
