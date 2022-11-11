package io.github.maazapan.katsuengine.engine.block.types.furnitures.manager;

import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.BlockType;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.FurnitureBlock;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import io.github.maazapan.katsuengine.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class FurnitureManager {

    private final BlockManager blockManager;
    private final KatsuEngine plugin;

    public FurnitureManager(KatsuEngine plugin) {
        this.blockManager = plugin.getBlockManager();
        this.plugin = plugin;
    }

    /**
     * Place furniture in location.
     *
     * @param id The id of the furniture.
     */
    public void placeFurniture(String id, Player player, Block block) {
        KatsuBlock katsuBlock = blockManager.getKatsuBlock(id);

        if (katsuBlock != null) {
            Location location = block.getLocation();

            /*
            if (katsuBlock.getType() == BlockType.FURNITURE) {
                block.setType(Material.BARRIER);

            } else if (katsuBlock.getType() == BlockType.STRING) {
                block.setType(Material.TRIPWIRE);
            }

             */

            /*
           - Set Custom nbt data at block.
           */
            NBTBlock nbtBlock = new NBTBlock(block);
            nbtBlock.getData().setString("katsu_block", katsuBlock.getType().toString());
            nbtBlock.getData().setString("katsu_id", id);

            /*
             - Create item-frame and put the item in frame.
             */
            block.getWorld().spawn(KatsuUtils.centerLocation(location.clone().add(0, -0.5, 0)), ItemFrame.class, (ItemFrame itemFrame) -> {
                NBTEntity nbtEntity = new NBTEntity(itemFrame);
                nbtEntity.getPersistentDataContainer().setString("katsu_block", katsuBlock.getType().toString());
                nbtEntity.getPersistentDataContainer().setString("katsu_id", id);

                itemFrame.setVisible(false);
                itemFrame.setFixed(false);
                itemFrame.setPersistent(true);
                itemFrame.setItemDropChance(0);

                itemFrame.setFacingDirection(BlockFace.UP, true);

                ItemStack itemStack = new ItemBuilder(katsuBlock.getItemStack().clone()).setName(null).toItemStack();
                itemFrame.setItem(itemStack, false);

                if (katsuBlock.isRotate()) {
                    itemFrame.setRotation(KatsuUtils.rotateItemFrame(player.getLocation().getYaw()));
                }
            });

            if (katsuBlock.getPlaceSound() != null) {
                String[] sound = katsuBlock.getPlaceSound().split(";");
                player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
            }
        } else {
            player.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&cThe furniture was not found, it may have been removed"));
        }
    }

    /**
     * Remove furniture from block location.
     *
     * @param block Break Block
     */
    public void removeFurniture(Block block, Player player) {
        NBTBlock nbtBlock = new NBTBlock(block);

        String furnitureID = nbtBlock.getData().getString("katsu_id");
        KatsuBlock katsuBlock = blockManager.getKatsuBlock(furnitureID);

        KatsuUtils.getItemFrame(block.getLocation()).remove();
        nbtBlock.getData().removeKey("katsu_block");
        nbtBlock.getData().removeKey("katsu_id");

        block.setType(Material.AIR);

        if (katsuBlock != null) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), katsuBlock.getItemStack());
            }

            if (katsuBlock.getRemoveSound() != null) {
                String[] sound = katsuBlock.getRemoveSound().split(";");
                player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
            }

            if (katsuBlock.getParticle() != null) {
                String[] particle = katsuBlock.getParticle().split(";");
                int amount = Integer.parseInt(particle[1]);

                double x = Double.parseDouble(particle[2]);
                double y = Double.parseDouble(particle[3]);
                double z = Double.parseDouble(particle[4]);

                if (Arrays.stream(Material.values()).map(Enum::toString).anyMatch(material -> material.equalsIgnoreCase(particle[0].toUpperCase()))) {
                    player.getWorld().spawnParticle(Particle.BLOCK_DUST, KatsuUtils.centerLocation(block.getLocation()), amount, x, y, z, Material.valueOf(particle[0].toUpperCase()).createBlockData());
                    return;
                }
                player.getWorld().spawnParticle(Particle.valueOf(particle[0]), KatsuUtils.centerLocation(block.getLocation()), amount, x, y, z);
            }

            if (KatsuUtils.getStandChair(block.getLocation()) != null) {
                ArmorStand armorStand = KatsuUtils.getStandChair(block.getLocation());
                armorStand.remove();
            }
        }
    }

    /**
     * Remove furniture from entity location.
     *
     * @param entity ItemFrame
     * @param player Player remove
     */
    public void removeFurniture(Entity entity, Player player) {
        NBTEntity nbtEntity = new NBTEntity(entity);

        String furnitureID = nbtEntity.getPersistentDataContainer().getString("katsu_id");
        KatsuBlock katsuBlock = blockManager.getKatsuBlock(furnitureID);

        entity.remove();

        if (katsuBlock != null) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                entity.getWorld().dropItem(entity.getLocation().add(0.5, 0.2, 0.5), katsuBlock.getItemStack());
            }

            if (katsuBlock.getRemoveSound() != null) {
                String[] sound = katsuBlock.getRemoveSound().split(";");
                player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
            }

            if (katsuBlock.getParticle() != null) {
                String[] particle = katsuBlock.getParticle().split(";");
                int amount = Integer.parseInt(particle[1]);

                double x = Double.parseDouble(particle[2]);
                double y = Double.parseDouble(particle[3]);
                double z = Double.parseDouble(particle[4]);

                if (Arrays.stream(Material.values()).map(Enum::toString).anyMatch(material -> material.equalsIgnoreCase(particle[0].toUpperCase()))) {
                    player.getWorld().spawnParticle(Particle.BLOCK_DUST, entity.getLocation().add(0, 0.5, 0), amount, x, y, z, Material.valueOf(particle[0].toUpperCase()).createBlockData());
                    return;
                }
                player.getWorld().spawnParticle(Particle.valueOf(particle[0]), entity.getLocation(), amount, x, y, z);
            }

            if (KatsuUtils.getStandChair(entity.getLocation()) != null) {
                ArmorStand armorStand = KatsuUtils.getStandChair(entity.getLocation());
                armorStand.remove();
            }
        }
    }

    public void createChair(Player player, Block block) {
        FurnitureBlock furnitureBlock = getFurniture(block);

        block.getWorld().spawn(KatsuUtils.centerLocation(block.getLocation().add(0, furnitureBlock.getChairPosY(), 0)), ArmorStand.class, (ArmorStand stand) -> {
            NBTEntity nbtEntity = new NBTEntity(stand);
            nbtEntity.getPersistentDataContainer().setString("katsu_chair", "chair");

            stand.setVisible(false);
            stand.setSmall(true);
            stand.setBasePlate(false);
            stand.setGravity(false);

            stand.addPassenger(player);
        });
    }

    public FurnitureBlock getFurniture(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);

        if (nbtBlock.getData().hasKey("katsu_block")) {
            return (FurnitureBlock) blockManager.getKatsuBlock(nbtBlock.getData().getString("katsu_id"));
        }
        return null;
    }


    public boolean isFurniture(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(itemStack);

        if (nbtItem.hasKey("katsu_block")) {
            KatsuBlock katsuBlock = blockManager.getKatsuBlock(nbtItem.getString("katsu_id"));
            return katsuBlock.getType() != BlockType.NORMAL;
        }
        return false;
    }

    public boolean isFurniture(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);

        if (nbtBlock.getData().hasKey("katsu_block")) {
            KatsuBlock katsuBlock = blockManager.getKatsuBlock(nbtBlock.getData().getString("katsu_id"));
            return katsuBlock.getType() != BlockType.NORMAL;
        }
        return false;
    }
}
