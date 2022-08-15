package io.github.maazapan.katsuengine.listener;

import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.furniture.Furniture;
import io.github.maazapan.katsuengine.engine.furniture.manager.FurnitureManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;


public class PlayerListener implements Listener {

    private final KatsuEngine plugin;

    public PlayerListener(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        FurnitureManager manager = plugin.getFurnitureManager();
        ItemStack itemStackHand = event.getItemInHand();

        if (itemStackHand.getType() != Material.AIR && !event.isCancelled()) {
            NBTItem nbtItem = new NBTItem(itemStackHand);

            if (nbtItem.hasKey("katsu_furniture")) {
                manager.placeFurniture(nbtItem.getString("katsu_furniture"), player, block);
            }
            if (nbtItem.hasKey("katsu_hat")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            NBTBlock nbtBlock = new NBTBlock(block);

            FurnitureManager manager = plugin.getFurnitureManager();

            if (nbtBlock.getData().hasKey("katsu_furniture") && !event.isCancelled()) {
                manager.removeFurniture(block.getLocation(), block);
                event.getClickedBlock().setType(Material.AIR);
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            NBTBlock nbtBlock = new NBTBlock(block);

            FurnitureManager manager = plugin.getFurnitureManager();

            if (nbtBlock.getData().hasKey("katsu_furniture") && !event.isCancelled()) {
                Furniture furniture = manager.getFurniture(nbtBlock.getData().getString("katsu_furniture"));

                if (furniture.isChairEnabled() && block.getLocation().distance(player.getLocation()) < 3.0) {
                    manager.chairBlock(player, block, furniture);
                }
            }
        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            NBTEntity nbtEntity = new NBTEntity(armorStand);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_furniture") && !event.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            NBTEntity nbtEntity = new NBTEntity(armorStand);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_furniture") && !event.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof ArmorStand && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ArmorStand armorStand = (ArmorStand) event.getDismounted();

            NBTEntity nbtEntity = new NBTEntity(armorStand);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_chair")) {
                armorStand.remove();
                player.teleport(player.getLocation().add(0, 0.6, 0));
            }
        }
    }
}
