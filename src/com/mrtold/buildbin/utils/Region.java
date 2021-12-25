package com.mrtold.buildbin.utils;

import org.bukkit.block.Block;

/**
 * Contains two blocks that define selected region
 * @author Mr_Told
 */
public class Region {

    Block first, second;

    public Region() {
        first = null;
        second = null;
    }

    public void setPos(boolean first, Block pos) {
        if (first) this.first = pos;
        else this.second = pos;
    }

    /**
     * Check if region could exist
     * @return true if it does
     */
    public boolean validate() {
        if (first == null || second == null) return false;
        return first.getWorld().equals(second.getWorld());
    }

    public Block getFirst() {
        return first;
    }

    public Block getSecond() {
        return second;
    }

}
