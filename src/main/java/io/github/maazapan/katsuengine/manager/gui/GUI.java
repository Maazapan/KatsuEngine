package io.github.maazapan.katsuengine.manager.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import io.github.maazapan.katsuengine.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GUI implements InventoryHolder {

    private final KatsuEngine plugin;
    private Inventory inventory;

    private final String path;

    public GUI(String path, KatsuEngine plugin) {
        this.plugin = plugin;
        this.path = path;
    }

    protected GUI create() {
        FileConfiguration config = plugin.getConfig();
        inventory = Bukkit.createInventory(this, this.getSlots(), KatsuUtils.colored(config.getString(path + ".title")));

        try {
            for (String key : config.getConfigurationSection(path + ".items").getKeys(false)) {
                ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(config.getString(path + ".items." + key + ".id")));

                if (config.isSet(path + ".items." + key + ".texture")) {
                    itemBuilder.setSkullBase64(config.getString(path + ".items." + key + ".texture"));
                }

                if (config.isSet(path + ".items." + key + ".owner")) {
                    itemBuilder.setSkullOwner(config.getString(path + ".items." + key + ".owner"));
                }

                if (config.isSet(path + ".items." + key + ".display_name")) {
                    itemBuilder.setName(config.getString(path + ".items." + key + ".display_name"));
                }

                if (config.isSet(path + ".items." + key + ".lore")) {
                    itemBuilder.setLore(config.getStringList(path + ".items." + key + ".lore"));
                }

                if (config.isSet(path + ".items." + key + ".custom_model_data")) {
                    itemBuilder.setModelData(config.getInt(path + ".items." + key + ".custom_model_data"));
                }

                NBTItem nbtItem = new NBTItem(itemBuilder.toItemStack());

                if (config.isSet(path + ".items." + key + ".actions")) {
                    nbtItem.setObject("katsu-actions", config.getStringList(path + ".items." + key + ".actions"));

                } else {
                    nbtItem.setString("katsu-inventory-item", "actions");
                }
                nbtItem.applyNBT(itemBuilder.toItemStack());

                if (config.isSet(path + ".items." + key + ".slots")) {
                    for (Integer slots : config.getIntegerList(path + ".items." + key + ".slots")) {
                        inventory.setItem(slots, itemBuilder.toItemStack());
                    }
                } else {
                    inventory.setItem(config.getInt(path + ".items." + key + ".slot"), itemBuilder.toItemStack());
                }
            }
        } catch (Exception exception) {
            plugin.getLogger().warning("Han occurred error in create custom menu.");
            exception.printStackTrace();
        }
        return this;
    }

    public abstract void handlerMenu(InventoryClickEvent event);

    public abstract void init();

    public abstract int getSlots();

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
