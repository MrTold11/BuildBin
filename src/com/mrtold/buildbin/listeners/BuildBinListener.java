package com.mrtold.buildbin.listeners;

import com.mrtold.buildbin.BuildBin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * @author Mr_Told
 */
public class BuildBinListener implements Listener {

    BuildBin plugin;

    public BuildBinListener(BuildBin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void playerQuitEvent(PlayerQuitEvent e) {
        plugin.clearRegion(e.getPlayer().getUniqueId());
    }

    @EventHandler
    void interactEvent(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.STICK) return;
        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        boolean isFirstBlock = e.getAction() == Action.LEFT_CLICK_BLOCK;
        Location loc = block.getLocation();
        plugin.pointPos(e.getPlayer().getUniqueId(), block, isFirstBlock);
        e.getPlayer().sendMessage(ChatColor.AQUA + (isFirstBlock ? "First" : "Second") + " position set: " +
                loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        e.setCancelled(true);
    }

}
