package com.mrtold.buildbin.commands;

import com.mrtold.buildbin.BuildBin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mr_Told
 */
public abstract class PlayerCommand implements CommandExecutor {

    final protected BuildBin plugin;

    public PlayerCommand(BuildBin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return failure("This command is only might be used by a player", commandSender);
        return onCommand((Player) commandSender, strings);
    }

    public abstract boolean onCommand(Player sender, String[] args);

    protected boolean failure(String msg, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + msg);
        return true;
    }

    protected boolean success(String msg, CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + msg);
        return true;
    }

}
