package com.anatevka.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WorldUtil {
    public static void setRegion(World world, Location loc1, Location loc2, Material material) {
        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();

        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();

        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double zDiff = z1 - z2;

        for (int i = 0; i <= Math.abs(xDiff); i++) {
            for (int j = 0; j <= Math.abs(yDiff); j++) {
                for (int k = 0; k <= Math.abs(zDiff); k++) {
                    double x = x1 - i * Math.signum(xDiff);
                    double y = y1 - j * Math.signum(yDiff);
                    double z = z1 - k * Math.signum(zDiff);

                    world.getBlockAt((int) x, (int) y, (int) z).setType(material);
                }
            }

        }
    }

}
