package io.github.maazapan.katsuengine.integrations.terra.structure;

import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;

public class KatsuStructure implements Structure, Keyed<KatsuStructure> {

    private final String furnitureID;
    private final RegistryKey key;

    private final int probability;

    public KatsuStructure(String furnitureID, int probability, RegistryKey key) {
        this.furnitureID = furnitureID;
        this.probability = probability;
        this.key = key;
    }

    @Override
    public boolean generate(Vector3Int vector3Int, WritableWorld writableWorld, Random random, Rotation rotation) {
        /*
         - Generating furniture in the terra worlds.
         */
        if (random.nextInt(100) < probability) {
            Location location = new Location(Bukkit.getWorld("terra"), vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
         //   Bukkit.getScheduler().runTask(KatsuEngine.getInstance(), () -> KatsuEngine.getInstance().getFurnitureManager().placeFurniture(furnitureID, location));
        }
        return true;
    }

    public RegistryKey getRegistryKey() {
        return key;
    }

    public int getProbability() {
        return probability;
    }
}
