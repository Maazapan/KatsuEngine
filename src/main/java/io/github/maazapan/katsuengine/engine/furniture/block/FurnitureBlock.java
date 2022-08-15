package io.github.maazapan.katsuengine.engine.furniture.block;

import io.github.maazapan.katsuengine.engine.furniture.Furniture;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.UUID;

public class FurnitureBlock extends Furniture {

    private final UUID uuid;

    private Location location;
    private ArmorStand armorStand;

    public FurnitureBlock(UUID uuid, String id) {
        super(id);
        this.uuid = uuid;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String toString(){
        return "FurnitureBlock{" + "uuid=" + uuid + ", id='" + getId() + '\'' + ", location=" + location +
                ", armorStand=" + armorStand + '}';
    }
}

