package io.github.maazapan.katsuengine.engine.block.types.furnitures.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.api.event.KatsuBlockInteractEvent;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.FurnitureBlock;
import io.github.maazapan.katsuengine.integrations.worldguard.WorldGuardHook;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FurnitureListener implements Listener {

    private final FurnitureManager furnitureManager;
    private final BlockManager blockManager;

    private final KatsuEngine plugin;

    public FurnitureListener(KatsuEngine plugin) {
        this.furnitureManager = plugin.getFurnitureManager();
        this.blockManager = plugin.getBlockManager();
        new PacketFurniture(plugin).register();
        this.plugin = plugin;
    }

    /**
     * Check player is break furniture block
     * This event is only used if the player is in creative.
     *
     * @param event BlockBreakEvent
     */
    @EventHandler
    public void onBreakFurniture(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (furnitureManager.isFurniture(block)) {
            if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canBreak(player, block.getLocation())) return;

            if (!event.isCancelled()) {
                furnitureManager.removeFurniture(block, player);
            }
        }
    }

    /**
     * Check player is place furniture block.
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onPlaceFurniture(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() != EquipmentSlot.HAND) return;

            ItemStack itemStack = event.getItem();
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            /*
             - Check player is placing furniture block on ground.
             */
            if (itemStack != null && itemStack.getType() != Material.AIR && block != null) {
                if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canPlace(player, block.getLocation())) return;

                Block relative = block.isPassable() ? block : block.getRelative(event.getBlockFace());
                NBTItem nbtItem = new NBTItem(itemStack);

                if (furnitureManager.isFurniture(itemStack)) {
                    event.setCancelled(true);

                    blockManager.sendAnimation(player, event.getHand());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> furnitureManager.placeFurniture(nbtItem.getString("katsu_id"), player, relative), 2);

                    if (player.getGameMode() != GameMode.CREATIVE) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    }
                }
            }
        }
    }


    /**
     * Check player is interacted with furniture block.
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onInteractFurniture(KatsuBlockInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        /*
        - Check if the block is furniture and spawn chair if it is enabled.
        */
        if (furnitureManager.isFurniture(block)) {
            if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canInteract(player, block.getLocation())) return;
            FurnitureBlock furnitureBlock = furnitureManager.getFurniture(block);

            if (furnitureBlock.isSeat() && !player.isSneaking() && player.getVehicle() == null) {
                event.setCancelled(true);
                furnitureManager.createSeat(player, block);
            }
        }
    }

    /**
     * Disable all furniture item-frame break event.
     *
     * @param event ItemFrameBreakEvent
     */
    @EventHandler
    public void onBreakHanging(HangingBreakEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            NBTEntity nbtEntity = new NBTEntity(event.getEntity());

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_block")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Disable player interact at furniture item-frame.
     *
     * @param event PlayerInteractAtEntityEvent
     */
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame) {
            NBTEntity nbtEntity = new NBTEntity(event.getRightClicked());

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_block")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Check player is dismount at furniture chair.
     * and remove armor-stand invisible.
     *
     * @param event EntityDismountEvent
     */
    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof ArmorStand && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ArmorStand armorStand = (ArmorStand) event.getDismounted();
            NBTEntity nbtEntity = new NBTEntity(armorStand);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_chair")) {
                Location location = armorStand.getLocation().add(0,1.0D,0)
                        .add(armorStand.getLocation().getDirection().multiply(1.0));

                player.teleport(location);
                armorStand.remove();
            }
        }
    }

    /**
     * Check player is damage at item-frame
     * and drop furniture block.
     *
     * @param event HangingBreakByEntityEvent
     */
    @EventHandler
    public void onHangingDamage(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player && event.getEntity() instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            Player player = (Player) event.getRemover();

            NBTEntity nbtEntity = new NBTEntity(itemFrame);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_block")) {
                if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canBreak(player, itemFrame.getLocation())) return;
                furnitureManager.removeFurniture(itemFrame, player);
            }
        }
    }

    /**
     * Check if the projectile is hit a furniture block
     * cancel the event.
     *
     * @param event ProjectileHitEvent
     */
    @EventHandler
    public void onHitProjectile(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof ItemFrame) {
            NBTEntity nbtEntity = new NBTEntity(event.getHitEntity());

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_block")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Check player is damage item-frame if is a furniture
     * drop furniture block.
     *
     * @param event EntityDamageByEntityEvent
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame && event.getDamager() instanceof Player) {
            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            Player player = (Player) event.getDamager();
            NBTEntity nbtEntity = new NBTEntity(itemFrame);

            if (nbtEntity.getPersistentDataContainer().hasKey("katsu_block")) {
                if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canBreak(player, itemFrame.getLocation())) return;
                event.setCancelled(true);

                furnitureManager.removeFurniture(itemFrame, player);
            }
        }
    }

    public static class PacketFurniture {

        private final ProtocolManager protocol;
        private final KatsuEngine plugin;

        private final FurnitureManager furnitureManager;

        private final Map<UUID, BukkitTask> taskMap;

        public PacketFurniture(KatsuEngine plugin) {
            this.plugin = plugin;
            this.furnitureManager = plugin.getFurnitureManager();
            this.protocol = ProtocolLibrary.getProtocolManager();
            this.taskMap = new HashMap<>();
        }

        public void register() {
            this.onFurnitureBreak();
        }

        /**
         * Check player is breaking furniture, and
         * execute task to break furniture.
         */
        private void onFurnitureBreak() {
            protocol.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {

                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                    Player player = event.getPlayer();

                    EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                    Block block = player.getTargetBlockExact(5);

                    if (player.getGameMode() == GameMode.CREATIVE || block == null) return;
                    if (WorldGuardHook.hasWorldGuard() && !WorldGuardHook.canBreak(player, block.getLocation())) return;

                    if (furnitureManager.isFurniture(block) && block.getType() == Material.BARRIER) {
                        KatsuBlock katsuBlock = furnitureManager.getFurniture(block);
                        NBTBlock nbtBlock = new NBTBlock(block);
                        UUID blockUUID = nbtBlock.getData().getUUID("katsu_uuid");

                        if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK && taskMap.containsKey(blockUUID)) {
                            taskMap.get(blockUUID).cancel();
                            taskMap.remove(blockUUID);
                            return;
                        }

                        if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK && !taskMap.containsKey(blockUUID)) {
                            BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, () -> furnitureManager.removeFurniture(block, player), katsuBlock.getRemoveTime());
                            taskMap.put(blockUUID, bukkitTask);
                        }
                    }
                }
            });
        }
    }
}
