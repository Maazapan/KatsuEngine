package io.github.maazapan.katsuengine.manager;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.commands.KatsuCommand;
import io.github.maazapan.katsuengine.engine.block.listener.BlockListener;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.manager.FurnitureListener;
import io.github.maazapan.katsuengine.integrations.IntegrationManager;
import io.github.maazapan.katsuengine.listener.PlayerListener;
import io.github.maazapan.katsuengine.manager.files.FileManager;

public class LoaderManager {

    private final KatsuEngine plugin;
    private FileManager fileManager;
    private IntegrationManager integrationManager;

    public LoaderManager(KatsuEngine plugin) {
        this.plugin = plugin;
        this.enable();
    }

    public void enable() {
        this.registerConfig();
        this.registerCommands();
        this.registerListener();
        this.registerIntegrations();
    }

    private void registerConfig() {
        fileManager = new FileManager(plugin);

        plugin.saveDefaultConfig();
        fileManager.create();
    }

    private void registerIntegrations() {
        integrationManager = new IntegrationManager(plugin);
        integrationManager.setup();
    }

    private void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(new BlockListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FurnitureListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
    }

    private void registerCommands() {
        plugin.getCommand("katsu").setExecutor(new KatsuCommand(plugin));
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public IntegrationManager getIntegrationManager() {
        return integrationManager;
    }
}
