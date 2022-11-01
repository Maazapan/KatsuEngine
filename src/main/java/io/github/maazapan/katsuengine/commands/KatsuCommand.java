package io.github.maazapan.katsuengine.commands;

import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.gui.FurnitureGUI;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
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
        BlockManager blockManager = plugin.getBlockManager();
        FileConfiguration config = plugin.getConfig();

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

                            if (!blockManager.getKatsuBlockMap().containsKey(id)) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fdoes not exist."));
                                return true;
                            }

                            if (Bukkit.getPlayer(args[3]) == null) {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe player &e" + args[3] + " &fis not online on the server."));
                                return true;
                            }

                            Player target = Bukkit.getPlayer(args[3]);
                            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fis now in player inventory."));
                            target.getInventory().addItem(blockManager.getKatsuBlock(id).getItemStack());
                            break;

                        case "menu":
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                new FurnitureGUI(player, plugin).addPages().init();
                            }
                            break;

                        default:
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                new FurnitureGUI(player, plugin).addPages().init();

                            } else {
                                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu help &ffor help."));
                            }
                            break;
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.saveDefaultConfig();

                plugin.getLoaderManager().getFileManager().getBlocksYML().reload();
                plugin.getLoaderManager().getFileManager().getBlocksYML().saveDefault();

                blockManager.getKatsuBlockMap().clear();
                blockManager.loadBlocks();

                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fConfiguration is &esuccess &freloaded."));

            } else if (args[0].equalsIgnoreCase("help")) {
                List<String> listHelp = new ArrayList<>();

                listHelp.add(" ");
                listHelp.add("                  #FF1D1D&lKatsu Engine &8(&7 " + plugin.getDescription().getVersion() + "&8)");
                listHelp.add(" ");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu reload &7: &fReload plugin configuration.");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu help &7: &fSee information about commands.");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu furniture get <id> <player> &7: &fGet furniture block.");
                listHelp.add(" ");

                listHelp.forEach(help -> sender.sendMessage(KatsuUtils.colored(help)));

            } else {
                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + KatsuUtils.colored("&fPlease complete command use &e/katsu help")));
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            List<String> commandsList = Arrays.asList("furniture", "help", "reload");
            List<String> subCommands = Arrays.asList("get", "menu");

            List<String> availableFurniture = new ArrayList<>(plugin.getBlockManager().getKatsuBlockMap().keySet());
            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName)
                                                 .collect(Collectors.toList());

            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("get")) {
                    if (args.length > 3) {
                        return onlinePlayers;
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
