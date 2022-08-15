package io.github.maazapan.katsuengine.api;

import io.github.maazapan.katsuengine.KatsuEngine;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class KatsuAPI {

    public static void placeFurniture(String id, Player player, Block block) {
        KatsuEngine.getInstance().getFurnitureManager().placeFurniture(id, player, block);
    }

    public static void placeFurniture(String id, Location location) {
        KatsuEngine.getInstance().getFurnitureManager().placeFurniture(id, location);
    }

    public static void test() {
        System.out.println("test");
    }
}
