package io.github.maazapan.katsuengine.integrations;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.integrations.update.UpdateHook;
import io.github.maazapan.katsuengine.integrations.worldguard.WorldGuardHook;
import io.github.maazapan.katsuengine.utils.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class IntegrationManager {

    private final KatsuEngine plugin;

    public IntegrationManager(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        FileConfiguration config = plugin.getConfig();

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            Plugin worldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard");
            WorldGuardHook.init(worldGuard);
        }

        if (config.getBoolean("config.plugin-updates")) {
            new UpdateHook(plugin, 106325).getVersion(version -> {
                if (plugin.getDescription().getVersion().equals(version)) {
                    plugin.getLogger().info("There is not a new update available.");

                } else {
                    plugin.getLogger().info("There is a new update available https://www.spigotmc.org/resources/106325");
                }
            });
        }

        Metrics metrics = new Metrics(plugin, 16687);
    }
}
