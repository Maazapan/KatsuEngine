package io.github.maazapan.katsuengine.engine.cosmetics.hats.manager;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.cosmetics.hats.HatCosmetic;
import io.github.maazapan.katsuengine.engine.cosmetics.hats.loader.HatLoader;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class HatManager {

    private final HashMap<String, HatCosmetic> hatCosmeticMap = new HashMap<>();
    private final KatsuEngine plugin;

    public HatManager(KatsuEngine plugin) {
        this.plugin = plugin;
        this.loadHats();
    }

    public void loadHats() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> new HatLoader(plugin).load(), 1);
    }

    public ItemStack getHatItemStackID(String id) {
        return hatCosmeticMap.get(id).getItemStack();
    }

    public HatCosmetic getHatCosmetic(String id) {
        return hatCosmeticMap.get(id);
    }

    public HashMap<String, HatCosmetic> getHatCosmeticMap() {
        return hatCosmeticMap;
    }
}
