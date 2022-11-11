package io.github.maazapan.katsuengine.engine.block.types.furnitures;

import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.types.BlockType;
import org.bukkit.Material;

public class FurnitureBlock extends KatsuBlock {

    private boolean chair;
    private double chairPosY;

    private Material hitBlock;

    public FurnitureBlock(String id) {
        super(id);
        this.setType(BlockType.FURNITURE);
        this.hitBlock = Material.BARRIER;
        this.chairPosY = -1.6;
        this.chair = false;
    }

    public void setChair(boolean chair) {
        this.chair = chair;
    }

    public boolean isChair() {
        return chair;
    }

    public void setChairPosY(double chairPosY) {
        this.chairPosY = chairPosY;
    }

    public double getChairPosY() {
        return chairPosY;
    }

    public void setHitBlock(Material hitBlock) {
        this.hitBlock = hitBlock;
    }

    public Material getHitBlock() {
        return hitBlock;
    }
}

