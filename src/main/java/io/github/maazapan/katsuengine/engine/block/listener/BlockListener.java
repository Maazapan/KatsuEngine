package io.github.maazapan.katsuengine.engine.block.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.api.event.KatsuBlockInteractEvent;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.manager.FurnitureManager;
import io.github.maazapan.katsuengine.integrations.worldguard.WorldGuardHook;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;

public class BlockListener implements Listener {

    private final BlockManager blockManager;

    public BlockListener(KatsuEngine plugin) {
        this.blockManager = plugin.getBlockManager();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();

        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock noteBlock = (NoteBlock) block.getBlockData();

            noteBlock.setInstrument(Instrument.BASS_DRUM);
            noteBlock.setPowered(false);
            noteBlock.setNote(Note.flat(1, Note.Tone.E));

            block.setBlockData(noteBlock);
        }
    }

    /**
     * Check player is place custom furniture
     *
     * @param event PlayerPlaceFurniture
     */
    @EventHandler
    public void onPlaceBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.isCancelled()) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            if (block == null) return;
            NBTBlock nbtBlock = new NBTBlock(block);
            /*
             - Check the furniture is clicked is a furniture and chair is enabled.
             */
            if (nbtBlock.getData().hasKey("katsu_block")) {
                KatsuBlockInteractEvent interactEvent = new KatsuBlockInteractEvent(player, block);

                if (!interactEvent.isCancelled()) {
                    Bukkit.getPluginManager().callEvent(interactEvent);
                }
            }
        }
    }

    /**
     * Check player is copy block with middle click
     * if the player is on creative.
     *
     * @param event InventoryCreativeEvent
     */
    @EventHandler
    public void onMiddleClick(InventoryCreativeEvent event) {
        if (event.getClick() != ClickType.CREATIVE) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getCursor().getType() == Material.BARRIER) {
            RayTraceResult rayTraceResult = player.rayTraceBlocks(6.0);

            Block block = rayTraceResult.getHitBlock();
            KatsuBlock katsuBlock = blockManager.getKatsuBlock(block);

            if (katsuBlock != null) {
                for (int i = 0; i < 8; i++) {
                    if (blockManager.getKatsuBlockID(player.getInventory().getItem(i)).equals(katsuBlock.getId())) {
                        player.getInventory().setHeldItemSlot(i);
                        event.setCancelled(true);
                        return;
                    }
                }
                event.setCursor(katsuBlock.getItemStack());
            }
        }
    }

    /**
     * Check if the player is craft furniture, and
     * check furniture permission.
     *
     * @param event CraftItemEvent
     */
    @EventHandler
    public void onCraftFurniture(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        Player player = (Player) event.getWhoClicked();

        if (result.getType() != Material.AIR) {
            NBTItem nbt = new NBTItem(result);

            if (nbt.hasKey("katsu_block")) {
                KatsuBlock furniture = blockManager.getKatsuBlock(result);

                if (furniture.getCraftPermission() == null) return;
                if (!player.hasPermission(furniture.getCraftPermission())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}