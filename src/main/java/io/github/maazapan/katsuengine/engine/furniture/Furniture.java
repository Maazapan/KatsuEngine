package io.github.maazapan.katsuengine.engine.furniture;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Furniture {

    private final String id;
    private ItemStack item;

    private Material hitBlock;

    private boolean chairEnabled;
    private boolean rotateEnabled;

    private double chairPosY;
    private int posX, posY, posZ;

    public Furniture(String id) {
        this.id = id;
        this.chairEnabled = false;
        this.rotateEnabled = true;
        this.chairPosY = 0.0;
        this.hitBlock = Material.BARRIER;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isChairEnabled() {
        return chairEnabled;
    }

    public void setChairEnabled(boolean chairEnabled) {
        this.chairEnabled = chairEnabled;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean isRotateEnabled() {
        return rotateEnabled;
    }

    public void setRotateEnabled(boolean rotateEnabled) {
        this.rotateEnabled = rotateEnabled;
    }

    public double getChairPosY() {
        return chairPosY;
    }

    public void setChairPosY(double chairPosY) {
        this.chairPosY = chairPosY;
    }

    public Material getHitBlock() {
        return hitBlock;
    }

    public void setHitBlock(Material hitBlock) {
        this.hitBlock = hitBlock;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }
}
