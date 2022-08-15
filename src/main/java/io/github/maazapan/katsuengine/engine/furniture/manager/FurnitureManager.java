package io.github.maazapan.katsuengine.engine.furniture.manager;

import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.furniture.Furniture;
import io.github.maazapan.katsuengine.engine.furniture.block.FurnitureBlock;
import io.github.maazapan.katsuengine.engine.furniture.loader.FurnitureLoader;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class FurnitureManager {

    private final Map<String, Furniture> furnitureMap = new HashMap<>();
    private final Map<UUID, FurnitureBlock> furnitureBlockMap = new HashMap<>();

    private final KatsuEngine plugin;

    public FurnitureManager(KatsuEngine plugin) {
        this.plugin = plugin;
        this.loadFurniture();
    }

    public void loadFurniture() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> new FurnitureLoader(plugin).load(), 1);
    }

    public void placeFurniture(String id, Player player, Block block) {
        Furniture furniture = furnitureMap.get(id);
        Location location = block.getLocation().add(0, -1.7, 0);

        block.setType(furniture.getHitBlock());

        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("katsu_furniture", id);

        if (furniture.isRotateEnabled()) {
            location.setYaw(player.getLocation().getYaw() + 180f);
        }

        ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(
                KatsuUtils.centerLocation(location), EntityType.ARMOR_STAND);

        NBTEntity nbtEntity = new NBTEntity(armorStand);
        nbtEntity.getPersistentDataContainer().setString("katsu_furniture", id);

        armorStand.setHelmet(furniture.getItem());
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
    }


    public void placeFurniture(String id, Location location){
        Furniture furniture = furnitureMap.get(id);

        Block block = location.getWorld().getBlockAt(location);
        block.setType(furniture.getHitBlock());

        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("katsu_furniture", id);

        if(furniture.isRotateEnabled()){
            location.setYaw(new Random().nextInt(100) + 180f);
        }

        ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(
                KatsuUtils.centerLocation(location.add(0,-1.7,0)), EntityType.ARMOR_STAND);

        NBTEntity nbtEntity = new NBTEntity(armorStand);
        nbtEntity.getPersistentDataContainer().setString("katsu_furniture", id);

        armorStand.setHelmet(furniture.getItem());
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
    }


    public void removeFurniture(Location location, Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);

        Furniture furniture = furnitureMap.get(nbtBlock.getData().getString("katsu_furniture"));
        location.getWorld().dropItem(location.add(0.5, 0, 0.5), furniture.getItem());

        location.getWorld().getNearbyEntities(location, 0.28, 0.2, 0.28).stream().filter(entity -> entity instanceof ArmorStand).forEach(entity -> {
            NBTEntity nbtEntity = new NBTEntity(entity);
            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_furniture")) {
                entity.remove();
            }
        });
        nbtBlock.getData().removeKey("katsu_furniture");
    }

    public void chairBlock(Player player, Block block, Furniture furniture) {
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(
                KatsuUtils.centerLocation(block.getLocation().add(0, furniture.getChairPosY(), 0)), EntityType.ARMOR_STAND);

        armorStand.setInvisible(true);
        armorStand.setGravity(false);

        NBTEntity nbtEntity = new NBTEntity(armorStand);
        nbtEntity.getPersistentDataContainer().setString("katsu_chair", "chair");

        armorStand.setPassenger(player);
    }

    public Furniture getFurniture(String id) {
        return furnitureMap.get(id);
    }

    public ItemStack getFurnitureItem(String id) {
        return furnitureMap.get(id).getItem();
    }

    public Map<String, Furniture> getFurnitureMap() {
        return furnitureMap;
    }

    public Map<UUID, FurnitureBlock> getFurnitureBlockMap() {
        return furnitureBlockMap;
    }
}
