package io.github.maazapan.katsuengine.engine.block;

import io.github.maazapan.katsuengine.engine.block.types.BlockType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class KatsuBlock {

    private final String id;
    private ItemStack itemStack;

    private BlockType type;

    private int removeTime;
    private boolean rotate;

    private String removeSound;

    private String placeSound;
    private String craftPermission;
    private String particle;

    private List<String> patternRecipe;
    private List<String> recipe;
    private List<String> actions;

    public KatsuBlock(String id) {
        this.id = id;
        this.type = BlockType.NORMAL;
        this.removeTime = 20;
        this.rotate = false;
        this.patternRecipe = new ArrayList<>();
        this.recipe = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public BlockType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public int getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(int removeTime) {
        this.removeTime = removeTime;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public String getRemoveSound() {
        return removeSound;
    }

    public void setRemoveSound(String removeSound) {
        this.removeSound = removeSound;
    }

    public String getPlaceSound() {
        return placeSound;
    }

    public void setPlaceSound(String placeSound) {
        this.placeSound = placeSound;
    }

    public String getCraftPermission() {
        return craftPermission;
    }

    public void setCraftPermission(String craftPermission) {
        this.craftPermission = craftPermission;
    }

    public List<String> getPatternRecipe() {
        return patternRecipe;
    }

    public void setPatternRecipe(List<String> patternRecipe) {
        this.patternRecipe = patternRecipe;
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public List<String> getRecipe() {
        return recipe;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setRecipe(List<String> recipe) {
        this.recipe = recipe;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
