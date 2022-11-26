package io.github.maazapan.katsuengine.utils;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KatsuUtils {

    public static Location centerLocation(Location location) {
        return location.add(0.5, 0.5, 0.5);
    }

    public static String colored(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }
            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colored(List<String> list) {
        return list.stream().map(KatsuUtils::colored).collect(Collectors.toList());
    }

    public static ItemFrame getItemFrame(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
            if (entity instanceof ItemFrame
                    && entity.getLocation().getBlockX() == location.getBlockX()
                    && entity.getLocation().getBlockY() == location.getBlockY()
                    && entity.getLocation().getBlockZ() == location.getBlockZ()) {
                NBTEntity nbt = new NBTEntity(entity);
                return nbt.getPersistentDataContainer().hasKey("katsu_block") ? (ItemFrame) entity : null;
            }
        }
        return null;
    }

    public static ArmorStand getFurnitureSeat(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
            if (entity instanceof ArmorStand) {
                NBTEntity nbt = new NBTEntity(entity);

                if (nbt.getPersistentDataContainer().hasKey("katsu_chair")) {
                    return (ArmorStand) entity;
                }
            }
        }
        return null;
    }

    public static Rotation rotateItemFrame(float y) {
        if (y < 0.0F) y += 360.0F;
        y %= 360.0F;
        int i = (int) ((y + 8) / 22.5);

        if (i == 15 || i == 0 || i == 1 || i == 16) return (Rotation.FLIPPED);
            // North-West
        else if (i == 2) return (Rotation.FLIPPED_45);
            // North
        else if (i == 3 || i == 4 || i == 5) return (Rotation.COUNTER_CLOCKWISE);
            // North-East
        else if (i == 6) return (Rotation.COUNTER_CLOCKWISE_45);
            // South-East
        else if (i == 10) return (Rotation.CLOCKWISE_45);
            // South
        else if (i == 11 || i == 12 || i == 13) return (Rotation.CLOCKWISE);
            // South-West
        else if (i == 14) return (Rotation.CLOCKWISE_135);
            // East
        else if (i == 7 || i == 8 || i == 9) return (Rotation.NONE);

        return null;
    }
}
