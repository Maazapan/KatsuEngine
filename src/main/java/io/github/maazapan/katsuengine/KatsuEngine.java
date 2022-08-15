package io.github.maazapan.katsuengine;

import io.github.maazapan.katsuengine.commands.KatsuCommand;
import io.github.maazapan.katsuengine.engine.cosmetics.hats.manager.HatManager;
import io.github.maazapan.katsuengine.engine.furniture.manager.FurnitureManager;
import io.github.maazapan.katsuengine.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class KatsuEngine extends JavaPlugin {

    private static KatsuEngine instance;

    private FurnitureManager furnitureManager;
    private HatManager hatManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        furnitureManager = new FurnitureManager(this);
        hatManager = new HatManager(this);

        this.saveDefaultConfig();
        this.getCommand("katsu").setExecutor(new KatsuCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HatManager getHatManager() {
        return hatManager;
    }

    public FurnitureManager getFurnitureManager() {
        return furnitureManager;
    }

    public String getPrefix() {
        return getConfig().getString("config.prefix");
    }

    public static KatsuEngine getInstance() {
        return instance;
    }
}
