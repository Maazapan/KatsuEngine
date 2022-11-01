package io.github.maazapan.katsuengine.engine.cosmetics.hats.loader;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.cosmetics.hats.HatCosmetic;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HatLoader {

    private final KatsuEngine plugin;

    public HatLoader(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();

        try {
            for (String id : config.getConfigurationSection("cosmetics.hat-cosmetic").getKeys(false)) {
                ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("cosmetics.hat-cosmetic." + id + ".material")));
                ItemMeta itemMeta = itemStack.getItemMeta();

                if (config.isSet("cosmetics.hat-cosmetic." + id + ".display_name")) {
                    itemMeta.setDisplayName(KatsuUtils.colored(config.getString("cosmetics.hat-cosmetic." + id + ".display_name")));
                }

                if (config.isSet("cosmetics.hat-cosmetic." + id + ".lore")) {
                    itemMeta.setLore(KatsuUtils.colored(config.getStringList("cosmetics.hat-cosmetic." + id + ".lore")));
                }

                if (config.isSet("cosmetics.hat-cosmetic." + id + ".custom_model_data")) {
                    itemMeta.setCustomModelData(config.getInt("cosmetics.hat-cosmetic." + id + ".custom_model_data"));
                }
                itemStack.setItemMeta(itemMeta);

                NBTItem nbtItem = new NBTItem(itemStack);
                nbtItem.setString("katsu_hat", id);
                nbtItem.applyNBT(itemStack);

                HatCosmetic hatCosmetic = new HatCosmetic(id, itemStack);
             //   plugin.getHatManager().getHatCosmeticMap().put(id, hatCosmetic);
            }
            plugin.getLogger().info("Cosmetics loaded successfully.");

        } catch (Exception e) {
            plugin.getLogger().warning("HatLoader: Error while loading hat.");
            e.printStackTrace();
        }
    }
}
