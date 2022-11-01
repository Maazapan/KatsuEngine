package io.github.maazapan.katsuengine.engine.block.types.furnitures;

import io.github.maazapan.katsuengine.engine.block.CustomBlock;
import io.github.maazapan.katsuengine.engine.block.types.BlockType;

public class FurnitureBlock extends CustomBlock {

    private boolean chair;
    private double chairPosY;

    public FurnitureBlock(String id) {
        super(id);
        this.chairPosY = -1.6;
        this.chair = false;
        this.setType(BlockType.FURNITURE);
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
}

