package com.anatevka.dungeongen;

import com.anatevka.utilities.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetRegionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            double[] doubles = new double[6];

            if (args.length >= 7) {
                ItemStack block = new ItemStack(Material.STONE);
                try {
                    block.setType(Material.matchMaterial(args[6]));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "Item \"" + args[6] + "\" does not exist, defaulting to Stone");
                }

                for (int i = 0; i < args.length - 1; i++) {
                    double num;

                    try {
                        num = Double.parseDouble(args[i]);
                    } catch (NumberFormatException e) {
                        num = 0.0;
                    }

                    doubles[i] = num;
                }

                Location loc1 = new Location(Bukkit.getWorld("world"), doubles[0], doubles[1], doubles[2]);
                Location loc2 = new Location(Bukkit.getWorld("world"), doubles[3], doubles[4], doubles[5]);

                WorldUtil.setRegion(Bukkit.getWorld("world"), loc1, loc2, block.getType());

                player.sendMessage(ChatColor.YELLOW + "Area from " + loc1.toString() + " to " + loc2.toString() + " filled with " + block.getType().toString());
            }
        }
        return false;
    }
}
