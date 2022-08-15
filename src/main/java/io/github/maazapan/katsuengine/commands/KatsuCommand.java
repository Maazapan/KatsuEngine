package io.github.maazapan.katsuengine.commands;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.cosmetics.hats.manager.HatManager;
import io.github.maazapan.katsuengine.engine.furniture.manager.FurnitureManager;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KatsuCommand implements CommandExecutor, TabCompleter {

    private final KatsuEngine plugin;

    public KatsuCommand(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HatManager hatManager = plugin.getHatManager();
        FurnitureManager furnitureManager = plugin.getFurnitureManager();


        if (args.length > 0) {
            /*
             - /katsu furniture get <id> <player>
             */
            if (args[0].equalsIgnoreCase("furniture")) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "get":
                            if (!(args.length > 3)) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu furniture get <id> <player>"));
                                return true;
                            }
                            String id = args[2];

                            if (!furnitureManager.getFurnitureMap().containsKey(id)) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fdoes not exist."));
                                return true;
                            }

                            if (Bukkit.getPlayer(args[3]) == null) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe player &e" + args[3] + " &fis not online on the server."));
                                return true;
                            }

                            Player target = Bukkit.getPlayer(args[3]);
                            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fis now in player inventory."));
                            target.getInventory().addItem(furnitureManager.getFurnitureItem(id));
                            break;

                        case "list":
                            break;

                        case "menu":
                            break;

                        default:
                            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu help &ffor help."));
                            break;
                    }
                }

                /*
                  - /katsu hat get <id> <player>
                 */
            } else if (args[0].equalsIgnoreCase("hat")) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "get":
                            if (!(args.length > 3)) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu hat get <id> <player>"));
                                return true;
                            }
                            String id = args[2];

                            if (!hatManager.getHatCosmeticMap().containsKey(id)) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe hat with id &e" + id + " &fdoes not exist."));
                                return true;
                            }

                            if (Bukkit.getPlayer(args[3]) == null) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe player &e" + args[3] + " &fis not online on the server."));
                                return true;
                            }

                            Player target = Bukkit.getPlayer(args[3]);
                            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe hat with id &e" + id + " &fis now in player inventory."));
                            target.getInventory().addItem(hatManager.getHatItemStackID(id));
                            break;

                        case "list":
                            break;

                        case "menu":
                            break;

                        default:
                            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu help &ffor help."));
                            return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.saveDefaultConfig();
                furnitureManager.loadFurniture();

                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fConfiguration is &esuccess &freloaded."));
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            List<String> commandsList = Arrays.asList("hat", "furniture", "help", "reload");
            List<String> subCommands = Arrays.asList("get", "list", "menu");

            List<String> availableHats = new ArrayList<>(plugin.getHatManager().getHatCosmeticMap().keySet());
            List<String> availableFurniture = new ArrayList<>(plugin.getFurnitureManager().getFurnitureMap().keySet());

            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName)
                                                 .collect(Collectors.toList());

            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("get")) {
                    if (args.length > 3) {
                        return onlinePlayers;
                    }
                    if (args[0].equalsIgnoreCase("hat")) {
                        return availableHats;
                    }
                    if (args[0].equalsIgnoreCase("furniture")) {
                        return availableFurniture;
                    }
                }
            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("furniture") || args[0].equalsIgnoreCase("hat")) {
                    return subCommands;
                }
            }
            return commandsList;
        }
        return null;
    }
}
