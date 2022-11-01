package io.github.maazapan.katsuengine.integrations;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.integrations.worldguard.WorldGuardHook;
import io.github.maazapan.katsuengine.utils.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class IntegrationManager {

    private final KatsuEngine plugin;

    public IntegrationManager(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            Plugin worldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard");
            WorldGuardHook.init(worldGuard);
        }

        Metrics metrics = new Metrics(plugin, 16687);
    }
}
