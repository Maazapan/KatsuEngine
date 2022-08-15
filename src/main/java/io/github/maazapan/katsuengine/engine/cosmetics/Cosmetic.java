package io.github.maazapan.katsuengine.engine.cosmetics;

import org.bukkit.inventory.ItemStack;

public abstract class Cosmetic {

    private final String id;
    private final ItemStack itemStack;

    public Cosmetic(String id, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.id = id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public abstract void init();
}
