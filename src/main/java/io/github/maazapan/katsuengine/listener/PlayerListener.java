package io.github.maazapan.katsuengine.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.manager.gui.GUI;
import io.github.maazapan.katsuengine.utils.KatsuUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListener implements Listener {

    private final KatsuEngine plugin;

    public PlayerListener(KatsuEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStatus(final PlayerResourcePackStatusEvent event) {
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        switch (status) {
            /*
             - Execute if the texture pack is downloaded correctly.
             */
            case SUCCESSFULLY_LOADED:
                if (config.getBoolean("config.download-message")) {
                    player.sendMessage(KatsuUtils.colored(plugin.getPrefix() + config.getString("messages.success-downloaded")));
                }
                break;
            /*
             - Execute if the player click on declined texture.
             */
            case DECLINED: {
                if (config.getBoolean("config.resource-pack.force-pack")) {
                    StringBuilder builder = new StringBuilder();

                    for (String a : config.getStringList("messages.declined-texture-kick")) {
                        builder.append(a).append("\n");
                    }
                    player.kickPlayer(KatsuUtils.colored(builder.toString()));
                    return;
                }
                player.sendMessage(KatsuUtils.colored(plugin.getPrefix() + config.getString("messages.declined-texture")));
            }
            break;
            /*
             - Execute if the texture pack is bad.
             */
            case FAILED_DOWNLOAD:
                player.sendMessage(KatsuUtils.colored(plugin.getPrefix() + config.getString("messages.failed-download")));
                break;
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (config.getBoolean("config.resource-pack.enable")) {
            try {
                player.setResourcePack(config.getString("config.resource-pack.resource-link"));

            } catch (Exception exception) {
                plugin.getLogger().warning("Ha occurred error download the texture pack.");
                plugin.getLogger().warning("Make sure the link in the config is correct, the texture pack needs to be in .zip format.");
                exception.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof GUI) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(event.getCurrentItem());

                GUI gui = (GUI) holder;
                gui.handlerMenu(event);

                if (nbtItem.hasKey("mailbox-inventory-item")) event.setCancelled(true);
            }
        }
    }
}
