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

        if (args.length > 0) {
            /*
             - Get furniture with this command.
             + Command: /katsu furniture <id> <player>
             */
            if (args[0].equalsIgnoreCase("furniture")) {
                if (!sender.hasPermission("katsuengine.cmd.furniture")) {
                    this.sendNoPermission(sender, "katsuengine.cmd.furniture");
                    return true;
                }

                if (!(args.length > 2)) {
                    sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fPlease use &e/katsu furniture <id> <player>"));
                    return true;
                }

                String id = args[1];
                if (blockManager.getKatsuBlock(id) == null) {
                    sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fdoes not exist."));
                    return true;
                }

                if (Bukkit.getPlayer(args[2]) == null) {
                    sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe player &e" + args[2] + " &fis not online on the server."));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[2]);
                target.getInventory().addItem(blockManager.getKatsuBlock(id).getItemStack());
                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fThe furniture with id &e" + id + " &fis now in player inventory."));

            /*
             - Reload all plugin configurations.
             + Command: /katsu reload
             */
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("katsuengine.cmd.reload")) {
                    this.sendNoPermission(sender, "katsuengine.cmd.reload");
                    return true;
                }

                plugin.reloadConfig();
                plugin.saveDefaultConfig();

                plugin.getLoaderManager().getFileManager().getBlocksYML().reload();
                plugin.getLoaderManager().getFileManager().getBlocksYML().saveDefault();

                blockManager.getKatsuBlockMap().clear();
                blockManager.loadBlocks();

                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + "&fConfiguration is &esuccess &freloaded."));

            /*
             - Show information about plugin commands.
             + Command: /katsu help
             */
            } else if (args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission("katsuengine.cmd.help")) {
                    this.sendNoPermission(sender, "katsuengine.cmd.help");
                    return true;
                }

                List<String> listHelp = new ArrayList<>();

                listHelp.add(" ");
                listHelp.add("                  #FF1D1D&lKatsu Engine &8(&7 " + plugin.getDescription().getVersion() + "&8)");
                listHelp.add(" ");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu reload &7: &fReload plugin configuration.");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu help &7: &fSee information about commands.");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu menu &7: &fOpen furniture menu.");
                listHelp.add(" &8▸ &a✔ &8| &e/katsu furniture get <id> <player> &7: &fGet furniture block.");
                listHelp.add(" ");

                listHelp.forEach(help -> sender.sendMessage(KatsuUtils.colored(help)));

            /*
             - Open furniture menu.
             + Command: /katsu menu
             */
            } else if (args[0].equalsIgnoreCase("menu")) {
                if (!sender.hasPermission("katsuengine.cmd.menu")) {
                    this.sendNoPermission(sender, "katsuengine.cmd.menu");
                    return true;
                }

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    new FurnitureGUI(player, plugin).addPages().init();

                } else {
                    plugin.getLogger().info("You can't open the menu from the console.");
                }

            } else {
                sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + KatsuUtils.colored("&fPlease complete command use &e/katsu help")));
            }
        } else {
            sender.sendMessage(KatsuUtils.colored(plugin.getPrefix() + KatsuUtils.colored("&fPlease complete command use &e/katsu help")));
        }
        return false;
    }

    private void sendNoPermission(CommandSender sender, String permission) {
        FileConfiguration config = plugin.getConfig();
        sender.sendMessage(KatsuUtils.colored(config.getString("messages.no-permission").replaceAll("%permission%", permission)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return null;
        List<String> commandsList = Arrays.asList("furniture", "help", "reload", "menu");

        List<String> availableFurniture = new ArrayList<>(plugin.getBlockManager().getKatsuBlockMap().keySet());
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

        // /katsu furniture <id> <player>

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("furniture")) {
                if (args.length > 2) {
                    return onlinePlayers;
                }
                return availableFurniture;
            }

        }
        return commandsList;
    }
}
