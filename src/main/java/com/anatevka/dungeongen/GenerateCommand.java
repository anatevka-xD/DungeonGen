package com.anatevka.dungeongen;

import com.anatevka.utilities.WorldUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        World world = Bukkit.getWorld("world");
        Location startLoc = new Location(world, 0.0, 0.0, 0.0);
        Location currentLoc = startLoc.clone();

        //Basic variables. Should take from command args eventually for testing
        int mazeSize = 11;      //X and Z dimensions of the maze
        int tileDistance = 6;   //Distance between tiles ends up being tileDistance - 1
        int tileSize = 3;       //Dimensions of each tile end up being tileSize * 2 + 1
        int tileHeight = 5;     //The tiles' height

        ArrayList<Location> locs = new ArrayList<>();       //Holds all room locations
        ArrayList<Location> explored = new ArrayList<>();   //Holds locations of all tiles that have been walked
        ArrayList<Location> finished = new ArrayList<>();   //Holds locations of all walked tiles that have no unwalked neighbors

        List<Location> corners = Arrays.asList(
                new Location(world,currentLoc.getX() + tileSize, currentLoc.getY() + tileHeight, currentLoc.getZ() + tileSize),            //Top corner of the 3d tile
                new Location(world,currentLoc.getX() - tileSize, currentLoc.getY(), currentLoc.getZ() - tileSize),                            //Bottom corner
                new Location(world,currentLoc.getX() + tileSize - 1, currentLoc.getY() + tileHeight, currentLoc.getZ() + tileSize - 1),    //Top inner corner
                new Location(world,currentLoc.getX() - tileSize + 1, currentLoc.getY() + 1, currentLoc.getZ() - tileSize + 1));            //Bottom inner corner

        List<Vector> directions = Arrays.asList(   //A vector for each cardinal direction
                new Vector(1.0, 0.0, 0.0),
                new Vector(0.0, 0.0, 1.0),
                new Vector(-1.0, 0.0, 0.0),
                new Vector(0.0, 0.0, -1.0));

        //Get all room locations
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                locs.add(currentLoc.clone());

                //Generate tile
                WorldUtil.setRegion(world, corners.get(0), corners.get(1), Material.STONE);
                WorldUtil.setRegion(world, corners.get(2), corners.get(3), Material.AIR);

                //Move to the next tile on the X axis
                currentLoc.setX(currentLoc.getX() + tileDistance);
            }
            //Return to the original X and move to the next tile on the Z axis
            currentLoc.setX(startLoc.getX());
            currentLoc.setZ(currentLoc.getZ() + tileDistance);
        }

        //Set up maze generator variables
        currentLoc = startLoc.clone();

        for (int i = 0; i < 1000; i++) {
            explored.add(currentLoc.clone());

            ArrayList<Location> adjacent = new ArrayList<>();

            for (Vector direction : directions) {
                Location adjacentLoc = currentLoc.clone().add(direction.clone().multiply(tileDistance));

                if (locs.contains(adjacentLoc) && !explored.contains(adjacentLoc) && !finished.contains(adjacentLoc)) {
                    adjacent.add(adjacentLoc.clone());
                }
            }

            if (adjacent.size() > 0) {                 //If there are adjacent unwalked tiles, populate to random adjacent tile
                Collections.shuffle(adjacent);
                Location door1 = currentLoc.clone();
                currentLoc = adjacent.get(0).clone();

                //Remove wall between tiles
                door1.add(currentLoc.clone().subtract(door1.clone()).toVector().multiply(0.5)).setY(1.0);
                Location door2 = door1.clone().add(new Location(world, 0.0, 5.0, 0.0));

                door1.setX(door1.getX() + tileSize - 1);
                door1.setZ(door1.getZ() + tileSize - 1);

                door2.setX(door2.getX() - tileSize + 1);
                door2.setZ(door2.getZ() - tileSize + 1);

                WorldUtil.setRegion(world, door1, door2, Material.AIR);
            } else {                                   //Otherwise, mark tile as finished and move on
                explored.remove(explored.size() - 1);
                finished.add(currentLoc.clone());

                if (explored.size() > 0) {
                    currentLoc = explored.get(explored.size() - 1).clone();
                    explored.remove(explored.size() - 1);
                }
            }

            if (explored.size() == 0) {
                break;
            }
        }

        return false;
    }
}
