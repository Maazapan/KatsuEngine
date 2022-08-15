package io.github.maazapan.katsuengine.engine.furniture.loader;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.furniture.Furniture;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FurnitureLoader {

    private final KatsuEngine plugin;

    public FurnitureLoader(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    /**
     * Load all furniture items from config.yml
     */
    public void load() {
        FileConfiguration config = plugin.getConfig();

        try {
            for (String id : config.getConfigurationSection("furniture").getKeys(false)) {
                ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("furniture." + id + ".itemstack.material")));
                ItemMeta itemMeta = itemStack.getItemMeta();

                if (config.isSet("furniture." + id + ".itemstack.display_name")) {
                    itemMeta.setDisplayName(KatsuUtils.colored(config.getString("furniture." + id + ".itemstack.display_name")));
                }

                if (config.isSet("furniture." + id + ".itemstack.lore")) {
                    itemMeta.setLore(KatsuUtils.colored(config.getStringList("furniture." + id + ".itemstack.lore")));
                }

                if (config.isSet("furniture." + id + ".itemstack.custom_model_data")) {
                    itemMeta.setCustomModelData(config.getInt("furniture." + id + ".itemstack.custom_model_data"));
                }
                itemStack.setItemMeta(itemMeta);

                NBTItem nbtItem = new NBTItem(itemStack);
                nbtItem.setString("katsu_furniture", id);
                nbtItem.applyNBT(itemStack);

                Furniture furniture = new Furniture(id, itemStack);

                if (config.isSet("furniture." + id + ".furniture-chair")) {
                    furniture.setChairEnabled(config.getBoolean("furniture." + id + ".furniture-chair.enable"));
                    furniture.setChairPosY(config.getDouble("furniture." + id + ".furniture-chair.posY"));
                }

                if(config.isSet("furniture." + id + ".rotate")){
                    furniture.setRotateEnabled(config.getBoolean("furniture." + id + ".rotate"));
                }

                if(config.isSet("furniture." + id + ".hit-block")){
                    furniture.setHitBlock(Material.valueOf(config.getString("furniture." + id + ".hit-block")));
                }

                plugin.getFurnitureManager().getFurnitureMap().put(id, furniture);
            }
            plugin.getLogger().info("Furniture loaded successfully.");

        } catch (Exception e) {
            plugin.getLogger().warning("FurnitureLoader: Error while loading furniture");
            e.printStackTrace();
        }
    }
}
