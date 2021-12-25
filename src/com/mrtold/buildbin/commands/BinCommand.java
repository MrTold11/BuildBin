package com.mrtold.buildbin.commands;

import com.mrtold.buildbin.BuildBin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Mr_Told
 */
public class BinCommand extends PlayerCommand {

    public BinCommand(BuildBin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        if (sender.getInventory().contains(Material.STICK))
            return failure("Use stick from your inventory: LBM and RBM to select two corner blocks of a building", sender);
        sender.getInventory().addItem(new ItemStack(Material.STICK));
        return success("Use stick: LBM and RBM to select two corner blocks of a building", sender);
    }

}
