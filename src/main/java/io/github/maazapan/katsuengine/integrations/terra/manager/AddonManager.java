package io.github.maazapan.katsuengine.integrations.terra.manager;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.Structure;
import io.github.maazapan.katsuengine.integrations.terra.structure.KatsuStructure;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddonManager {

    private final List<KatsuStructure> structures = new ArrayList<>();
    private final List<String> worlds = new ArrayList<>();

    private final Platform platform;
    private final BaseAddon addon;

    public AddonManager(Platform platform, BaseAddon addon) {
        this.platform = platform;
        this.addon = addon;
    }

    private void loadTerraWorlds() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/KatsuEngine", "config.yml"));
        worlds.addAll(config.getStringList("config.integrations.terra-generation.terra-worlds"));
    }

    /**
     * Registers structure to terra addon.
     */
    public void registerStructures() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/KatsuEngine", "config.yml"));

        if (!config.getBoolean("config.integrations.terra-generation.enable")) return;
        try {
            this.loadTerraWorlds();

            for (String furnitureID : config.getConfigurationSection("config.integrations.terra-generation.furniture-models").getKeys(false)) {
                /*
                 - Creating a terra structure and add it to the list of structures.
                 */
                String structureKey = config.getString("config.integrations.terra-generation.furniture-models." + furnitureID + ".structure_key");
                int probability = config.getInt("config.integrations.terra-generation.furniture-models." + furnitureID + ".probability");
                RegistryKey registryKey = addon.key(structureKey);

                structures.add(new KatsuStructure(furnitureID, probability, registryKey));
            }

            /*
             - Registering all the structures to the terra addon.
             */
            platform.getEventManager().getHandler(FunctionalEventHandler.class)
                    .register(addon, ConfigPackPreLoadEvent.class).then(event -> {
                        ConfigPack pack = event.getPack();
                        CheckedRegistry<Structure> structureRegistry = pack.getOrCreateRegistry(Structure.class);

                        for (KatsuStructure structure : structures) {
                            structureRegistry.register(structure);
                            System.out.println("Registered structure: " + structure.getRegistryKey().toString());
                        }
                    });
            System.out.println("KatsuAddon is successfully enabled!");

        } catch (Exception e) {
            System.out.println("Error while registering structures!");
            e.printStackTrace();
        }
    }

    public List<String> getWorlds() {
        return worlds;
    }
}
