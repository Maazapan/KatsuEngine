package io.github.maazapan.katsuengine.manager.files;

import io.github.maazapan.katsuengine.KatsuEngine;

public class FileManager {

    private final KatsuEngine plugin;
    private FileCreator blocksYML;

    public FileManager(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    public void create() {
        this.blocksYML = new FileCreator("blocks.yml", plugin.getDataFolder(), plugin);
    }

    public FileCreator getBlocksYML() {
        return blocksYML;
    }
}
