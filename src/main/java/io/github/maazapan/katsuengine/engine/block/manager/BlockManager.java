package io.github.maazapan.katsuengine.engine.block.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockManager {

    private final Map<String, KatsuBlock> katsuBlockMap = new HashMap<>();
    private final KatsuEngine plugin;

    public BlockManager(KatsuEngine plugin) {
        this.plugin = plugin;
        this.loadBlocks();
        this.loadRecipes();
    }

    public void loadBlocks() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> new BlockLoader(plugin).load(), 1);
    }

    public void loadRecipes() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::registerRecipes, 20);
    }

    /**
     * Register all recipes at server.
     */
    public void registerRecipes() {
        List<KatsuBlock> furnitureList = katsuBlockMap.values().stream()
                                                 .filter(furniture -> !furniture.getPatternRecipe().isEmpty() && !furniture.getRecipe().isEmpty())
                                                 .collect(Collectors.toList());
        try {
            for (KatsuBlock furniture : furnitureList) {
                NamespacedKey key = new NamespacedKey(plugin, furniture.getId());
                ShapedRecipe recipe = new ShapedRecipe(key, furniture.getItemStack());

                List<String> pattern = furniture.getPatternRecipe().stream().map(s -> s.replaceAll(" ", "")).collect(Collectors.toList());
                recipe.shape(pattern.get(0), pattern.get(1), pattern.get(2));

                for (String s : furniture.getRecipe()) {
                    String[] split = s.split(";");
                    recipe.setIngredient(split[0].charAt(0), Material.valueOf(split[1]));
                }
                Bukkit.addRecipe(recipe);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Han error occurred on register custom recipe.");
            e.printStackTrace();
        }
    }

    /**
     * Send custom place animation.
     *
     * @param player        Player
     * @param equipmentSlot Hand slot
     */
    public void sendAnimation(Player player, EquipmentSlot equipmentSlot) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer animation = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);

        animation.getIntegers().write(0, player.getEntityId());
        animation.getIntegers().write(1, (equipmentSlot == EquipmentSlot.HAND) ? 0 : 3);

        protocolManager.sendServerPacket(player, animation);
    }

    public Map<String, KatsuBlock> getKatsuBlockMap() {
        return katsuBlockMap;
    }

    public KatsuBlock getKatsuBlock(String id) {
        return katsuBlockMap.get(id);
    }

    public KatsuBlock getKatsuBlock(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);

        if (nbtBlock.getData().hasKey("katsu_block")) {
            return getKatsuBlock(nbtBlock.getData().getString("katsu_id"));
        }
        return null;
    }

    public KatsuBlock getKatsuBlock(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) return null;
        NBTItem nbtItem = new NBTItem(itemStack);

        if (nbtItem.hasKey("katsu_block")) {
            return getKatsuBlock(nbtItem.getString("katsu_id"));
        }
        return null;
    }

    public String getKatsuBlockID(ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(itemStack);

            if (nbtItem.hasKey("katsu_block")) {
                return nbtItem.getString("katsu_id");
            }
        }
        return null;
    }

    public FurnitureBlock getFurnitureBlock(String id) {
        KatsuBlock katsuBlock = katsuBlockMap.get(id);

        if (katsuBlock.getType() == BlockType.FURNITURE) {
            return (FurnitureBlock) katsuBlock;
        }
        return null;
    }
}
