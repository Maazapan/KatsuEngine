package io.github.maazapan.katsuengine.engine.block.types.furnitures;

import de.tr7zw.changeme.nbtapi.NBTBlock;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.types.BlockType;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class FurnitureBlock extends KatsuBlock {

    private boolean seat;
    private double seatPosY;

    private Material hitBlock;

    public FurnitureBlock(String id) {
        super(id);
        this.setType(BlockType.FURNITURE);
        this.hitBlock = Material.BARRIER;
        this.seatPosY = -1.6;
        this.seat = false;
    }

    public boolean isSeat() {
        return seat;
    }

    public double getSeatPosY() {
        return seatPosY;
    }

    public void setSeat(boolean seat) {
        this.seat = seat;
    }

    public void setSeatPosY(double seatPosY) {
        this.seatPosY = seatPosY;
    }

    public void setHitBlock(Material hitBlock) {
        this.hitBlock = hitBlock;
    }

    public Material getHitBlock() {
        return hitBlock;
    }
    public NBTBlock getNTBBlock(Block block) {
        return new NBTBlock(block);
    }
}

