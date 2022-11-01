package io.github.maazapan.katsuengine;

import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.manager.FurnitureManager;
import io.github.maazapan.katsuengine.manager.LoaderManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class KatsuEngine extends JavaPlugin {

    private static KatsuEngine instance;

    private BlockManager blockManager;
    private FurnitureManager furnitureManager;

    private LoaderManager loaderManager;

    @Override
    public void onEnable() {
        instance = this;
        blockManager = new BlockManager(this);
        furnitureManager = new FurnitureManager(this);
        loaderManager = new LoaderManager(this);
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public String getPrefix() {
        return getConfig().getString("config.prefix");
    }

    public LoaderManager getLoaderManager() {
        return loaderManager;
    }

    public FurnitureManager getFurnitureManager() {
        return furnitureManager;
    }

    public static KatsuEngine getInstance() {
        return instance;
    }
}
