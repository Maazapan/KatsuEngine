package io.github.maazapan.katsuengine.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KatsuUtils {

    public static Location centerLocation(Location location){
        return location.add(0.5, 0.5, 0.5);
    }

    public static String colored(String message){
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

    public static List<String> colored(List<String> list){
        return list.stream().map(KatsuUtils::colored).collect(Collectors.toList());
    }
}
