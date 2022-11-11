package io.github.maazapan.katsuengine.engine.block.manager;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.types.BlockType;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.FurnitureBlock;
import io.github.maazapan.katsuengine.engine.block.types.normal.NormalBlock;
import io.github.maazapan.katsuengine.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class BlockLoader {

    private final KatsuEngine plugin;

    public BlockLoader(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getLoaderManager().getFileManager().getBlocksYML();

        try {
            for (String id : config.getConfigurationSection("katsu_blocks").getKeys(false)) {
                ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(config.getString("katsu_blocks." + id + ".itemstack.material")));
                BlockType blockType = BlockType.valueOf(config.getString("katsu_blocks." + id + ".block_type"));

                if (config.isSet("katsu_blocks." + id + ".itemstack.display_name")) {
                    itemBuilder.setName(config.getString("katsu_blocks." + id + ".itemstack.display_name"));
                }

                if (config.isSet("katsu_blocks." + id + ".itemstack.lore")) {
                    itemBuilder.setLore(config.getStringList("katsu_blocks." + id + ".itemstack.lore"));
                }

                if (config.isSet("katsu_blocks." + id + ".itemstack.custom_model_data")) {
                    itemBuilder.setModelData(config.getInt("katsu_blocks." + id + ".itemstack.custom_model_data"));
                }

                KatsuBlock katsuBlock = blockType == BlockType.NORMAL ? new NormalBlock(id) : new FurnitureBlock(id);
                katsuBlock.setType(blockType);

                NBTItem nbtItem = new NBTItem(itemBuilder.toItemStack());
                nbtItem.setString("katsu_block", blockType.toString());
                nbtItem.setString("katsu_id", id);
                nbtItem.applyNBT(itemBuilder.toItemStack());

                katsuBlock.setItemStack(itemBuilder.toItemStack());

                if (config.isSet("katsu_blocks." + id + ".rotate")) {
                    katsuBlock.setRotate(config.getBoolean("katsu_blocks." + id + ".rotate"));
                }

                if (config.isSet("katsu_blocks." + id + ".sounds.break")) {
                    katsuBlock.setRemoveSound(config.getString("katsu_blocks." + id + ".sounds.break"));
                }

                if (config.isSet("katsu_blocks." + id + ".sounds.place")) {
                    katsuBlock.setPlaceSound(config.getString("katsu_blocks." + id + ".sounds.place"));
                }

                if (config.isSet("katsu_blocks." + id + ".particles")) {
                    katsuBlock.setParticle(config.getString("katsu_blocks." + id + ".particles"));
                }

                if (config.isSet("katsu_blocks." + id + ".removeTime")) {
                    katsuBlock.setRemoveTime(config.getInt("katsu_blocks." + id + ".removeTime"));
                }

                if (config.isSet("katsu_blocks." + id + ".craft_recipe.craft_permission")) {
                    katsuBlock.setCraftPermission(config.getString("katsu_blocks." + id + ".craft_recipe.craft_permission"));
                }

                if (config.isSet("katsu_blocks." + id + ".craft_recipe.pattern")) {
                    katsuBlock.setPatternRecipe(config.getStringList("katsu_blocks." + id + ".craft_recipe.pattern"));
                }

                if (config.isSet("katsu_blocks." + id + ".craft_recipe.recipe")) {
                    List<String> recipeList = new ArrayList<>();

                    for (String key : config.getConfigurationSection("katsu_blocks." + id + ".craft_recipe.recipe").getKeys(false)) {
                        recipeList.add(key + ";" + config.getString("katsu_blocks." + id + ".craft_recipe.recipe." + key));
                    }
                    katsuBlock.setRecipe(recipeList);
                }

                if (blockType == BlockType.FURNITURE) {
                    FurnitureBlock furnitureBlock = (FurnitureBlock) katsuBlock;

                    if (config.isSet("katsu_blocks." + id + ".furniture_chair")) {
                        furnitureBlock.setChair(config.getBoolean("katsu_blocks." + id + ".furniture_chair.enable"));
                        furnitureBlock.setChairPosY(config.getDouble("furniture." + id + ".furniture_chair.posY"));
                    }
                    plugin.getBlockManager().getKatsuBlockMap().put(id, furnitureBlock);

                } else {
                    plugin.getBlockManager().getKatsuBlockMap().put(id, katsuBlock);
                }
            }
            plugin.getLogger().info("Success loaded " + plugin.getBlockManager().getKatsuBlockMap().values().size() + " custom blocks.");

        } catch (Exception exception) {
            plugin.getLogger().warning("Han occurred error on load blocks.");
            exception.printStackTrace();
        }
    }
}
